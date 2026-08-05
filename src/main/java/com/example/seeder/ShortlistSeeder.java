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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShortlistSeeder {

    private static final int BATCH_SIZE = 10_000;

    private static final ThreadLocalRandom RANDOM =
            ThreadLocalRandom.current();

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO shortlists
            (
                user_id,
                profile_id,
                is_active,
                created_at,
                updated_at,
                version
            )
            VALUES
            (
                ?, ?, ?, ?, ?, ?
            )
            ON CONFLICT (user_id, profile_id)
            DO NOTHING
            """;

    @Transactional
    public void seedShortlists(long totalShortlists) {

        log.info("======================================");
        log.info("SHORTLIST SEEDER STARTED");
        log.info("======================================");

        List<UserProfile> users = loadUserProfiles();

        if (users.size() < 2) {
            log.warn("Not enough users found.");
            return;
        }

        long inserted = getShortlistCount();

        if (inserted >= totalShortlists) {

            log.info("Shortlists already generated.");
            printSummary();
            return;
        }

        long remaining = totalShortlists - inserted;

        log.info("Users               : {}", users.size());
        log.info("Existing Shortlists : {}", inserted);
        log.info("Target Shortlists   : {}", totalShortlists);

        while (remaining > 0) {

            int currentBatch =
                    (int) Math.min(BATCH_SIZE, remaining);

            List<ShortlistRecord> batch =
                    generateBatch(
                            users,
                            currentBatch
                    );

            insertBatch(batch);

            inserted = getShortlistCount();
            remaining = totalShortlists - inserted;

            log.info(
                    "Generated : {} / {}",
                    inserted,
                    totalShortlists
            );
        }

        log.info("======================================");
        log.info("SHORTLIST SEEDER COMPLETED");
        log.info("======================================");

        printSummary();
    }

    /**
     * Load User & Profile IDs
     */
    private List<UserProfile> loadUserProfiles() {

        return jdbcTemplate.query(
                """
                SELECT
                    u.id AS user_id,
                    p.id AS profile_id
                FROM users u
                INNER JOIN profiles p
                    ON p.user_id = u.id
                WHERE u.is_active = true
                ORDER BY u.id
                """,
                (rs, rowNum) -> new UserProfile(
                        rs.getLong("user_id"),
                        rs.getLong("profile_id")
                )
        );
    }

    /**
     * Generate Batch
     */
    private List<ShortlistRecord> generateBatch(
            List<UserProfile> users,
            int batchSize) {

        List<ShortlistRecord> batch =
                new ArrayList<>(batchSize);

        Set<String> pairs = new HashSet<>();

        while (batch.size() < batchSize) {

            UserProfile sender =
                    users.get(RANDOM.nextInt(users.size()));

            UserProfile receiver =
                    users.get(RANDOM.nextInt(users.size()));

            if (sender.userId().equals(receiver.userId())) {
                continue;
            }

            String key =
                    sender.userId() + "-" + receiver.profileId();

            if (!pairs.add(key)) {
                continue;
            }

            LocalDateTime createdAt =
                    LocalDateTime.now()
                            .minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));

            batch.add(
                    new ShortlistRecord(
                            sender.userId(),
                            receiver.profileId(),
                            createdAt
                    )
            );
        }

        return batch;
    }
    /**
     * Insert Batch
     */
    private void insertBatch(
            List<ShortlistRecord> batch) {

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
            ShortlistRecord record)
            throws SQLException {

        int i = 1;

        Timestamp timestamp =
                Timestamp.valueOf(record.createdAt());

        ps.setLong(i++, record.userId());
        ps.setLong(i++, record.profileId());

        ps.setBoolean(i++, true);

        ps.setTimestamp(i++, timestamp);
        ps.setTimestamp(i++, timestamp);

        ps.setLong(i++, 0L);
    }

    /**
     * Shortlist Record
     */
    private record ShortlistRecord(
            Long userId,
            Long profileId,
            LocalDateTime createdAt
    ) {
    }
    /**
     * Total Shortlists
     */
    private long getShortlistCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM shortlists",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Remaining Shortlists
     */
    private long getRemainingShortlists(long targetShortlists) {

        long existing = getShortlistCount();

        return Math.max(0, targetShortlists - existing);
    }

    /**
     * Seeder Summary
     */
    private void printSummary() {

        log.info("======================================");
        log.info("SHORTLIST SEEDER SUMMARY");
        log.info("======================================");
        log.info("Total Shortlists : {}", getShortlistCount());
        log.info("======================================");
    }

    /**
     * User Profile
     */
    private record UserProfile(
            Long userId,
            Long profileId
    ) {
    }

}