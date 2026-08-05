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
public class InterestSeeder {

    private static final int BATCH_SIZE = 10000;
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
        INSERT INTO interests
        (
            sender_id,
            receiver_id,
            status,
            is_active,
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
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
        )
        ON CONFLICT (sender_id, receiver_id)
        DO NOTHING
        """;

    /**
     * Generate Interests
     *
     * Recommended:
     * seedInterests(2_000_000);
     */
    public void seedInterests(long totalInterests) {

        log.info("=========================================");
        log.info("Generating {} Interests...", totalInterests);
        log.info("=========================================");

        long inserted = getInterestCount();

        if (inserted >= totalInterests) {

            log.info("Interests already generated.");
            printSeederSummary();

            return;
        }

        long remaining = totalInterests - inserted;

        log.info("Already Present : {}", inserted);
        log.info("Remaining       : {}", remaining);

        long minUserId = jdbcTemplate.queryForObject(
                """
                SELECT MIN(id)
                FROM users
                WHERE is_active = true
                """,
                Long.class
        );

        long maxUserId = jdbcTemplate.queryForObject(
                """
                SELECT MAX(id)
                FROM users
                WHERE is_active = true
                """,
                Long.class
        );

        while (remaining > 0) {

            int currentBatch =
                    (int) Math.min(BATCH_SIZE, remaining);

            List<InterestRecord> batch =
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
                    totalInterests
            );
        }

        log.info("=========================================");
        log.info("Interest Seeder Completed");
        log.info("=========================================");

        printSeederSummary();
    }

    /**
     * Generate One Batch
     */
    private List<InterestRecord> generateBatch(
            int batchSize,
            long minUserId,
            long maxUserId) {

        List<InterestRecord> records =
                new ArrayList<>(batchSize);

        while (records.size() < batchSize) {

            long senderId =
                    RANDOM.nextLong(minUserId, maxUserId + 1);

            long receiverId =
                    RANDOM.nextLong(minUserId, maxUserId + 1);

            if (senderId == receiverId) {
                continue;
            }

            LocalDateTime createdAt =
                    LocalDateTime.now()
                            .minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));

            records.add(
                    new InterestRecord(
                            senderId,
                            receiverId,
                            randomStatus(),
                            createdAt
                    )
            );
        }

        return records;
    }
    /**
     * Insert Batch
     */
    private void insertBatch(List<InterestRecord> records) {

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
                                records.get(index)
                        );
                    }

                    @Override
                    public int getBatchSize() {
                        return records.size();
                    }
                });
    }

    /**
     * Fill PreparedStatement
     */
    private void fillStatement(
            PreparedStatement ps,
            InterestRecord record)
            throws SQLException {

        int i = 1;

        ps.setLong(i++, record.senderId());
        ps.setLong(i++, record.receiverId());

        ps.setString(i++, record.status());

        ps.setBoolean(i++, true);

        Timestamp timestamp =
                Timestamp.valueOf(record.createdAt());

        ps.setTimestamp(i++, timestamp);
        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setTimestamp(i++, timestamp);
        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setBoolean(i++, false);

        ps.setNull(i++, java.sql.Types.TIMESTAMP);
        ps.setNull(i++, java.sql.Types.BIGINT);
        ps.setNull(i++, java.sql.Types.VARCHAR);

        ps.setLong(i++, 0L);
    }

    /**
     * Random Interest Status
     *
     * 70% Pending
     * 20% Accepted
     * 10% Rejected
     */
    private String randomStatus() {

        int value = RANDOM.nextInt(100);

        if (value < 70) {
            return "PENDING";
        }

        if (value < 90) {
            return "ACCEPTED";
        }

        return "REJECTED";
    }

    /**
     * Batch Record
     */
    private record InterestRecord(
            long senderId,
            long receiverId,
            String status,
            LocalDateTime createdAt
    ) {
    }
    /**
     * Total Interests
     */
    public long getInterestCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM interests",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Print Seeder Summary
     */
    public void printSeederSummary() {

        long total = getInterestCount();

        log.info("======================================");
        log.info("INTEREST SEEDER SUMMARY");
        log.info("======================================");
        log.info("Total Interests : {}", total);
        log.info("======================================");
    }

}
