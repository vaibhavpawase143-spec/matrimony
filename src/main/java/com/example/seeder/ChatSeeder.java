package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatSeeder {

    private final JdbcTemplate jdbcTemplate;

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final int BATCH_SIZE = 10_000;

    private static final List<String> SAMPLE_MESSAGES = List.of(
            "Hi 👋",
            "Hello",
            "How are you?",
            "Nice profile 😊",
            "Where are you from?",
            "What do you do?",
            "Good Morning ☀️",
            "Good Night 🌙",
            "Have a nice day!",
            "Thank you.",
            "Let's connect.",
            "Can we talk?",
            "Hope you're doing well.",
            "Happy to connect with you.",
            "What are your hobbies?",
            "Do you like travelling?",
            "Nice to meet you.",
            "Let's know each other better.",
            "Have you completed your profile?",
            "Looking forward to hearing from you."
    );

    private static final String INSERT_CONVERSATION_SQL = """
            INSERT INTO conversations
            (
                user1_id,
                user2_id,
                created_at,
                created_by,
                updated_at,
                updated_by,
                is_deleted,
                deleted_at,
                deleted_by,
                deletion_reason,
                version
            )
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(user1_id,user2_id)
            DO NOTHING
            """;

    private static final String INSERT_MESSAGE_SQL = """
            INSERT INTO messages
            (
                conversation_id,
                sender_id,
                reply_to_id,
                content,
                message_type,
                status,
                media_url,
                media_type,
                reaction,
                pinned,
                starred,
                deleted_for_everyone,
                deleted_for_users,
                deleted_at,
                is_deleted,
                created_at,
                seen_at
            )
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    @Transactional
    public void seedChats(long totalConversations,
                          int minMessages,
                          int maxMessages) {

        log.info("======================================");
        log.info("CHAT SEEDER STARTED");
        log.info("======================================");

        List<Long> userIds = loadActiveUserIds();

        if (userIds.size() < 2) {
            log.warn("Not enough users.");
            return;
        }

        long existing = getConversationCount();

        if (existing >= totalConversations) {
            log.info("Conversations already generated.");
            return;
        }

        long remaining = totalConversations - existing;

        log.info("Users              : {}", userIds.size());
        log.info("Existing Chats     : {}", existing);
        log.info("Target Chats       : {}", totalConversations);
        log.info("Remaining Chats    : {}", remaining);

        while (remaining > 0) {

            int currentBatch =
                    (int) Math.min(BATCH_SIZE, remaining);

            List<ConversationRecord> conversations =
                    generateConversationBatch(
                            userIds,
                            currentBatch
                    );

            insertConversationBatch(conversations);

            existing = getConversationCount();
            remaining = totalConversations - existing;

            log.info(
                    "Generated Conversations : {}",
                    existing
            );
        }

        generateMessagesForConversations(
                minMessages,
                maxMessages
        );

        log.info("======================================");
        log.info("CHAT SEEDER COMPLETED");
        log.info("======================================");
    }

    private List<Long> loadActiveUserIds() {

        return jdbcTemplate.query(
                """
                SELECT id
                FROM users
                WHERE is_active=true
                ORDER BY id
                """,
                (rs, rowNum) -> rs.getLong(1)
        );
    }

    private List<ConversationRecord> generateConversationBatch(
            List<Long> userIds,
            int batchSize) {

        List<ConversationRecord> batch =
                new ArrayList<>(batchSize);

        Set<String> generatedPairs = new HashSet<>();

        while (batch.size() < batchSize) {

            long user1 =
                    userIds.get(
                            RANDOM.nextInt(userIds.size())
                    );

            long user2 =
                    userIds.get(
                            RANDOM.nextInt(userIds.size())
                    );

            if (user1 == user2) {
                continue;
            }

            long first = Math.min(user1, user2);
            long second = Math.max(user1, user2);

            String key = first + "-" + second;

            if (!generatedPairs.add(key)) {
                continue;
            }

            LocalDateTime createdAt =
                    LocalDateTime.now()
                            .minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));

            batch.add(
                    new ConversationRecord(
                            first,
                            second,
                            createdAt
                    )
            );
        }

        return batch;
    }
    /**
     * Insert Conversation Batch
     */
    private void insertConversationBatch(
            List<ConversationRecord> batch) {

        jdbcTemplate.batchUpdate(
                INSERT_CONVERSATION_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index)
                            throws SQLException {

                        fillConversationStatement(
                                ps,
                                batch.get(index)
                        );
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                }
        );
    }

    /**
     * Fill Conversation Statement
     */
    private void fillConversationStatement(
            PreparedStatement ps,
            ConversationRecord record)
            throws SQLException {

        int i = 1;

        Timestamp ts =
                Timestamp.valueOf(record.createdAt());

        ps.setLong(i++, record.user1Id());
        ps.setLong(i++, record.user2Id());

        ps.setTimestamp(i++, ts);
        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setTimestamp(i++, ts);
        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setBoolean(i++, false);

        ps.setNull(i++, java.sql.Types.TIMESTAMP);
        ps.setNull(i++, java.sql.Types.BIGINT);
        ps.setNull(i++, java.sql.Types.VARCHAR);

        ps.setLong(i++, 0L);
    }

    /**
     * Generate Messages For All Conversations
     */
    private void generateMessagesForConversations(
            int minMessages,
            int maxMessages) {

        log.info("Generating Messages...");

        long totalMessages = getMessageCount();

        while (true) {

            List<ConversationData> conversations =
                    loadConversationBatch();

            if (conversations.isEmpty()) {
                break;
            }

            List<MessageRecord> messageBatch =
                    new ArrayList<>(BATCH_SIZE * 10);

            for (ConversationData conversation : conversations) {

                int messageCount =
                        RANDOM.nextInt(
                                minMessages,
                                maxMessages + 1
                        );

                LocalDateTime time =
                        conversation.createdAt();

                boolean senderFlag = true;

                for (int i = 0; i < messageCount; i++) {

                    Long senderId =
                            senderFlag
                                    ? conversation.user1Id()
                                    : conversation.user2Id();

                    senderFlag = !senderFlag;

                    time = time.plusMinutes(
                            RANDOM.nextInt(1, 15)
                    );

                    messageBatch.add(
                            new MessageRecord(
                                    conversation.id(),
                                    senderId,
                                    SAMPLE_MESSAGES.get(
                                            RANDOM.nextInt(
                                                    SAMPLE_MESSAGES.size()
                                            )
                                    ),
                                    time
                            )
                    );
                }
            }

            insertMessageBatch(messageBatch);

            totalMessages = getMessageCount();

            log.info(
                    "Messages Generated : {}",
                    totalMessages
            );
        }
    }

    /**
     * Load Conversations Without Messages
     */
    private List<ConversationData> loadConversationBatch() {

        return jdbcTemplate.query(
                """
                SELECT c.id,
                       c.user1_id,
                       c.user2_id,
                       c.created_at
                FROM conversations c
                LEFT JOIN messages m
                       ON c.id = m.conversation_id
                WHERE m.id IS NULL
                LIMIT ?
                """,
                ps -> ps.setInt(1, BATCH_SIZE),
                (rs, rowNum) -> new ConversationData(
                        rs.getLong("id"),
                        rs.getLong("user1_id"),
                        rs.getLong("user2_id"),
                        rs.getTimestamp("created_at")
                                .toLocalDateTime()
                )
        );
    }

    /**
     * Insert Message Batch
     */
    private void insertMessageBatch(
            List<MessageRecord> batch) {

        jdbcTemplate.batchUpdate(
                INSERT_MESSAGE_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index)
                            throws SQLException {

                        fillMessageStatement(
                                ps,
                                batch.get(index)
                        );
                    }

                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                }
        );
    }

    /**
     * Fill Message Statement
     */
    private void fillMessageStatement(
            PreparedStatement ps,
            MessageRecord record)
            throws SQLException {

        int i = 1;

        ps.setLong(i++, record.conversationId());
        ps.setLong(i++, record.senderId());

        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setString(i++, record.content());

        ps.setString(i++, "TEXT");
        ps.setString(i++, "SENT");

        ps.setNull(i++, java.sql.Types.VARCHAR);
        ps.setNull(i++, java.sql.Types.VARCHAR);
        ps.setNull(i++, java.sql.Types.VARCHAR);

        ps.setBoolean(i++, false);
        ps.setBoolean(i++, false);
        ps.setBoolean(i++, false);

        ps.setNull(i++, java.sql.Types.VARCHAR);
        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        ps.setBoolean(i++, false);

        LocalDateTime created = record.createdAt();
        LocalDateTime seenAt = created.plusSeconds(
                RANDOM.nextInt(30, 600)
        );

        ps.setTimestamp(i++, Timestamp.valueOf(created));
        ps.setTimestamp(i++, Timestamp.valueOf(seenAt));
    }
    /**
     * Total Conversations
     */
    private long getConversationCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM conversations",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Total Messages
     */
    private long getMessageCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM messages",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Seeder Summary
     */
    private void printSummary() {

        log.info("======================================");
        log.info("CHAT SEEDER SUMMARY");
        log.info("======================================");
        log.info("Total Conversations : {}", getConversationCount());
        log.info("Total Messages      : {}", getMessageCount());
        log.info("======================================");
    }

    /**
     * Conversation Record
     */
    private record ConversationRecord(
            Long user1Id,
            Long user2Id,
            LocalDateTime createdAt
    ) {
    }

    /**
     * Conversation Data
     */
    private record ConversationData(
            Long id,
            Long user1Id,
            Long user2Id,
            LocalDateTime createdAt
    ) {
    }

    /**
     * Message Record
     */
    private record MessageRecord(
            Long conversationId,
            Long senderId,
            String content,
            LocalDateTime createdAt
    ) {
    }

}