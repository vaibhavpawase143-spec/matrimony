package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversationSeeder {

    private static final int BATCH_SIZE = 10000;
    private static final ThreadLocalRandom RANDOM =
            ThreadLocalRandom.current();

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
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
            (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            )
            ON CONFLICT (user1_id,user2_id)
            DO NOTHING
            """;

    /**
     * Recommended:
     *
     * seedConversations(2_000_000);
     */
    public void seedConversations(long totalConversations) {

        log.info("======================================");
        log.info("Generating {} Conversations...",
                totalConversations);
        log.info("======================================");

        long inserted = getConversationCount();

        if (inserted >= totalConversations) {

            log.info("Conversations already generated.");
            printSeederSummary();
            return;
        }

        long remaining =
                totalConversations - inserted;

        log.info("Already Present : {}", inserted);
        log.info("Remaining       : {}", remaining);

        Long minUserId = jdbcTemplate.queryForObject(
                """
                SELECT MIN(id)
                FROM users
                WHERE is_active=true
                """,
                Long.class
        );

        Long maxUserId = jdbcTemplate.queryForObject(
                """
                SELECT MAX(id)
                FROM users
                WHERE is_active=true
                """,
                Long.class
        );

        while (remaining > 0) {

            int currentBatch =
                    (int) Math.min(BATCH_SIZE, remaining);

            List<ConversationRecord> batch =
                    generateBatch(
                            currentBatch,
                            minUserId,
                            maxUserId
                    );

            insertBatch(batch);

            inserted += currentBatch;
            remaining -= currentBatch;

            log.info(
                    "Generated : {} / {}",
                    inserted,
                    totalConversations
            );
        }

        log.info("======================================");
        log.info("Conversation Seeder Completed");
        log.info("======================================");

        printSeederSummary();
    }

    /**
     * Generate Batch
     */
    private List<ConversationRecord> generateBatch(
            int batchSize,
            long minUserId,
            long maxUserId) {

        List<ConversationRecord> batch =
                new ArrayList<>(batchSize);

        while (batch.size() < batchSize) {

            long user1 =
                    RANDOM.nextLong(
                            minUserId,
                            maxUserId + 1
                    );

            long user2 =
                    RANDOM.nextLong(
                            minUserId,
                            maxUserId + 1
                    );

            if (user1 == user2) {
                continue;
            }

            long first = Math.min(user1, user2);
            long second = Math.max(user1, user2);

            LocalDateTime now =
                    LocalDateTime.now()
                            .minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));

            batch.add(
                    new ConversationRecord(
                            first,
                            second,
                            now
                    )
            );
        }

        return batch;
    }
    /**
     * Insert Batch
     */
    private void insertBatch(
            List<ConversationRecord> batch) {

        jdbcTemplate.batchUpdate(
                INSERT_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index)
                            throws SQLException {

                        fillStatement(
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
     * Fill PreparedStatement
     */
    private void fillStatement(
            PreparedStatement ps,
            ConversationRecord record)
            throws SQLException {

        int i = 1;

        ps.setLong(i++, record.user1Id());
        ps.setLong(i++, record.user2Id());

        Timestamp timestamp =
                Timestamp.valueOf(record.createdAt());

        // created_at
        ps.setTimestamp(i++, timestamp);

        // created_by
        ps.setNull(i++, java.sql.Types.BIGINT);

        // updated_at
        ps.setTimestamp(i++, timestamp);

        // updated_by
        ps.setNull(i++, java.sql.Types.BIGINT);

        // is_deleted
        ps.setBoolean(i++, false);

        // deleted_at
        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        // deleted_by
        ps.setNull(i++, java.sql.Types.BIGINT);

        // deletion_reason
        ps.setNull(i++, java.sql.Types.VARCHAR);

        // version
        ps.setLong(i++, 0L);
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
     * Total Conversations
     */
    public long getConversationCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM conversations",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Remaining Conversations
     */
    public long getRemainingConversations(long targetConversations) {

        long existing = getConversationCount();

        return Math.max(0, targetConversations - existing);
    }

    /**
     * Seeder Summary
     */
    public void printSeederSummary() {

        long total = getConversationCount();

        log.info("======================================");
        log.info("CONVERSATION SEEDER SUMMARY");
        log.info("======================================");
        log.info("Total Conversations : {}", total);
        log.info("======================================");
    }

}