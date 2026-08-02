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
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeSeeder {

    private static final int BATCH_SIZE = 10000;
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
            INSERT INTO swipes
            (
                from_user_id,
                to_user_id,
                type,
                created_at
            )
            VALUES
            (
                ?, ?, ?, ?
            )
            ON CONFLICT (from_user_id, to_user_id)
            DO NOTHING
            """;

    /**
     * Recommended:
     * seedLikes(2_000_000);
     */
    public void seedLikes(long totalLikes) {

        log.info("======================================");
        log.info("Generating {} Likes...", totalLikes);
        log.info("======================================");

        long inserted = getLikeCount();

        if (inserted >= totalLikes) {

            log.info("Likes already generated.");
            printSeederSummary();
            return;
        }

        long remaining = totalLikes - inserted;

        log.info("Already Present : {}", inserted);
        log.info("Remaining       : {}", remaining);

        // Load active users with gender once
        List<UserGender> users = jdbcTemplate.query(
                """
                SELECT
                    p.user_id,
                    LOWER(g.name) AS gender
                FROM profiles p
                JOIN genders g
                  ON g.id = p.gender_id
                JOIN users u
                  ON u.id = p.user_id
                WHERE u.is_active = true
                """,
                (rs, rowNum) -> new UserGender(
                        rs.getLong("user_id"),
                        rs.getString("gender")
                )
        );

        Map<String, List<Long>> genderMap =
                users.stream()
                        .collect(Collectors.groupingBy(
                                UserGender::gender,
                                Collectors.mapping(
                                        UserGender::userId,
                                        Collectors.toList()
                                )
                        ));

        List<Long> maleUsers =
                genderMap.getOrDefault("male", List.of());

        List<Long> femaleUsers =
                genderMap.getOrDefault("female", List.of());

        if (maleUsers.isEmpty() || femaleUsers.isEmpty()) {

            log.warn("Male/Female users not found.");
            return;
        }

        while (remaining > 0) {

            int currentBatch =
                    (int) Math.min(BATCH_SIZE, remaining);

            List<LikeRecord> batch =
                    generateBatch(
                            currentBatch,
                            maleUsers,
                            femaleUsers
                    );

            insertBatch(batch);

            inserted += currentBatch;
            remaining -= currentBatch;

            log.info(
                    "Likes Generated : {} / {}",
                    inserted,
                    totalLikes
            );
        }

        log.info("======================================");
        log.info("Like Seeder Completed");
        log.info("======================================");

        printSeederSummary();
    }

    /**
     * Generate Batch
     */
    private List<LikeRecord> generateBatch(
            int batchSize,
            List<Long> maleUsers,
            List<Long> femaleUsers) {

        List<LikeRecord> batch =
                new ArrayList<>(batchSize);

        while (batch.size() < batchSize) {

            boolean maleSender =
                    RANDOM.nextBoolean();

            long fromUser;
            long toUser;

            if (maleSender) {

                fromUser =
                        maleUsers.get(
                                RANDOM.nextInt(maleUsers.size()));

                toUser =
                        femaleUsers.get(
                                RANDOM.nextInt(femaleUsers.size()));

            } else {

                fromUser =
                        femaleUsers.get(
                                RANDOM.nextInt(femaleUsers.size()));

                toUser =
                        maleUsers.get(
                                RANDOM.nextInt(maleUsers.size()));
            }

            if (fromUser == toUser) {
                continue;
            }

            LocalDateTime createdAt =
                    LocalDateTime.now()
                            .minusDays(RANDOM.nextInt(365))
                            .minusHours(RANDOM.nextInt(24))
                            .minusMinutes(RANDOM.nextInt(60));

            batch.add(
                    new LikeRecord(
                            fromUser,
                            toUser,
                            "LIKE",
                            createdAt
                    )
            );
        }

        return batch;
    }
    /**
     * Insert Batch
     */
    private void insertBatch(List<LikeRecord> batch) {

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
                });
    }

    /**
     * Fill PreparedStatement
     */
    private void fillStatement(
            PreparedStatement ps,
            LikeRecord record)
            throws SQLException {

        ps.setLong(1, record.fromUserId());
        ps.setLong(2, record.toUserId());
        ps.setString(3, record.type());
        ps.setTimestamp(
                4,
                Timestamp.valueOf(record.createdAt())
        );
    }

    /**
     * Total Likes
     */
    public long getLikeCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM swipes",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Seeder Summary
     */
    public void printSeederSummary() {

        long total = getLikeCount();

        log.info("======================================");
        log.info("LIKE SEEDER SUMMARY");
        log.info("======================================");
        log.info("Total Likes : {}", total);
        log.info("======================================");
    }

    /**
     * User + Gender
     */
    private record UserGender(
            Long userId,
            String gender
    ) {
    }

    /**
     * Like Record
     */
    private record LikeRecord(
            Long fromUserId,
            Long toUserId,
            String type,
            LocalDateTime createdAt
    ) {
    }

}
