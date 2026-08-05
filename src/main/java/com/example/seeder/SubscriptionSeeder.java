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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionSeeder {

    private static final int BATCH_SIZE = 10_000;

    private static final ThreadLocalRandom RANDOM =
            ThreadLocalRandom.current();

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
        INSERT INTO user_subscriptions
        (
            user_id,
            plan_id,
            start_date,
            end_date,
            is_active,
            status,
            refund_amount,
            refund_date,
            refund_reason,
            cancellation_reason,
            cancelled_at,
            created_at,
            created_by,
            updated_at,
            updated_by,
            is_deleted,
            version
        )
        VALUES
        (
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
        )
        """;

    @Transactional
    public void seedSubscriptions(int premiumPercentage) {

        log.info("======================================");
        log.info("SUBSCRIPTION SEEDER STARTED");
        log.info("======================================");

        List<Long> userIds = loadUserIds();

        if (userIds.isEmpty()) {

            log.warn("No active users found.");
            return;
        }

        Map<Long, Integer> plans =
                loadSubscriptionPlans();

        if (plans.isEmpty()) {

            log.warn("No subscription plans found.");
            return;
        }

        Set<Long> subscribedUsers =
                loadSubscribedUsers();

        long existing =
                subscribedUsers.size();

        log.info("Active Users           : {}", userIds.size());
        log.info("Existing Subscription  : {}", existing);

        List<Long> planIds =
                new ArrayList<>(plans.keySet());

        List<SubscriptionRecord> batch =
                new ArrayList<>(BATCH_SIZE);

        long inserted = 0;

        for (Long userId : userIds) {

            // Skip users who already have subscription
            if (subscribedUsers.contains(userId)) {
                continue;
            }

            // Premium percentage
            if (RANDOM.nextInt(100) >= premiumPercentage) {
                continue;
            }

            Long planId =
                    planIds.get(
                            RANDOM.nextInt(planIds.size())
                    );

            Integer duration =
                    plans.get(planId);

            LocalDateTime startDate =
                    LocalDateTime.now()
                            .minusDays(
                                    RANDOM.nextInt(365)
                            );

            LocalDateTime endDate =
                    startDate.plusDays(duration);

            String status;
            boolean active;

            int chance = RANDOM.nextInt(100);

            if (chance < 70 &&
                    endDate.isAfter(LocalDateTime.now())) {

                status = "ACTIVE";
                active = true;

            } else if (chance < 90) {

                status = "EXPIRED";
                active = false;

            } else {

                status = "CANCELLED";
                active = false;
            }

            LocalDateTime cancelledAt = null;
            String cancellationReason = null;

            if ("CANCELLED".equals(status)) {

                long days = Math.max(
                        1,
                        Duration.between(startDate, endDate).toDays()
                );

                cancelledAt =
                        startDate.plusDays(
                                RANDOM.nextLong(days)
                        );

                cancellationReason =
                        "User cancelled subscription";
            }

            LocalDateTime now =
                    LocalDateTime.now();

            batch.add(
                    new SubscriptionRecord(
                            userId,
                            planId,
                            startDate,
                            endDate,
                            active,
                            status,
                            cancellationReason,
                            cancelledAt,
                            now
                    )
            );

            if (batch.size() >= BATCH_SIZE) {

                insertBatch(batch);

                inserted += batch.size();

                // Mark inserted users to avoid duplicates
                for (SubscriptionRecord record : batch) {
                    subscribedUsers.add(record.userId());
                }

                log.info(
                        "Inserted {} subscriptions...",
                        inserted
                );

                batch.clear();
            }
        }
        // Insert remaining records
        if (!batch.isEmpty()) {

            insertBatch(batch);

            inserted += batch.size();

            for (SubscriptionRecord record : batch) {
                subscribedUsers.add(record.userId());
            }

            log.info(
                    "Inserted {} subscriptions...",
                    inserted
            );

            batch.clear();
        }

        log.info("======================================");
        log.info("New Subscriptions Created : {}", inserted);
        log.info("Total Subscriptions       : {}", getSubscriptionCount());
        log.info("======================================");
    }

    /**
     * Batch Insert
     */
    private void insertBatch(
            List<SubscriptionRecord> batch
    ) {

        jdbcTemplate.batchUpdate(
                INSERT_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index
                    ) throws SQLException {

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
            SubscriptionRecord record
    ) throws SQLException {

        int i = 1;

        ps.setLong(i++, record.userId());
        ps.setLong(i++, record.planId());

        ps.setTimestamp(
                i++,
                Timestamp.valueOf(record.startDate())
        );

        ps.setTimestamp(
                i++,
                Timestamp.valueOf(record.endDate())
        );

        ps.setBoolean(i++, record.active());

        ps.setString(
                i++,
                record.status()
        );

        // refund_amount
        ps.setNull(i++, java.sql.Types.NUMERIC);

        // refund_date
        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        // refund_reason
        ps.setNull(i++, java.sql.Types.VARCHAR);

        // cancellation_reason
        if (record.cancellationReason() == null) {

            ps.setNull(
                    i++,
                    java.sql.Types.VARCHAR
            );

        } else {

            ps.setString(
                    i++,
                    record.cancellationReason()
            );
        }

        // cancelled_at
        if (record.cancelledAt() == null) {

            ps.setNull(
                    i++,
                    java.sql.Types.TIMESTAMP
            );

        } else {

            ps.setTimestamp(
                    i++,
                    Timestamp.valueOf(record.cancelledAt())
            );
        }

        ps.setTimestamp(
                i++,
                Timestamp.valueOf(record.createdAt())
        );

        // created_by
        ps.setNull(
                i++,
                java.sql.Types.BIGINT
        );

        ps.setTimestamp(
                i++,
                Timestamp.valueOf(record.createdAt())
        );

        // updated_by
        ps.setNull(
                i++,
                java.sql.Types.BIGINT
        );

        ps.setBoolean(
                i++,
                false
        );

        ps.setLong(
                i++,
                0L
        );
    }
    /**
     * Load Active User IDs
     */
    private List<Long> loadUserIds() {

        return jdbcTemplate.query(
                """
                SELECT id
                FROM users
                WHERE is_active = true
                ORDER BY id
                """,
                (rs, rowNum) -> rs.getLong("id")
        );
    }

    /**
     * Load Subscription Plans
     */
    private Map<Long, Integer> loadSubscriptionPlans() {

        Map<Long, Integer> plans = new HashMap<>();

        jdbcTemplate.query(
                """
                SELECT id, duration
                FROM subscription_plans
                ORDER BY id
                """,
                (rs, rowNum) -> {

                    plans.put(
                            rs.getLong("id"),
                            rs.getInt("duration")
                    );

                    return null;
                }
        );

        return plans;
    }

    /**
     * Load Users Who Already Have Subscription
     */
    private Set<Long> loadSubscribedUsers() {

        return new HashSet<>(

                jdbcTemplate.query(
                        """
                        SELECT user_id
                        FROM user_subscriptions
                        """,
                        (rs, rowNum) ->
                                rs.getLong("user_id")
                )

        );
    }

    /**
     * Current Subscription Count
     */
    private long getSubscriptionCount() {

        Long count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_subscriptions
                """,
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Subscription Record
     */
    private record SubscriptionRecord(

            Long userId,
            Long planId,

            LocalDateTime startDate,
            LocalDateTime endDate,

            boolean active,
            String status,

            String cancellationReason,
            LocalDateTime cancelledAt,

            LocalDateTime createdAt

    ) {
    }

}