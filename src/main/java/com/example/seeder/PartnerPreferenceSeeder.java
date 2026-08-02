package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartnerPreferenceSeeder {

    private static final int BATCH_SIZE = 10000;

    private final JdbcTemplate jdbcTemplate;
    private final MasterDataCache masterDataCache;

    private static final String INSERT_SQL = """
        INSERT INTO partner_preferences (
            user_id,
            min_age,
            max_age,
            education_level_id,
            occupation_id,
            other_expectations,
            marital_status_id,
            smoking_id,
            drinking_id,
            diet_id,
            min_height,
            max_height,
            min_weight,
            max_weight,
            religion_id,
            caste_id,
            city_id,
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
        VALUES (
            ?,?,?,?,?,?,?,?,?,?,
            ?,?,?,?,?,?,?,?,?,?,
            ?,?,?,?,?,?,?
        )
        """;

    public void seedPartnerPreferences() {

        log.info("==========================================");
        log.info("Generating Partner Preferences...");
        log.info("==========================================");

        long lastUserId = 0;
        long inserted = 0;

        while (true) {

            List<Long> userIds = jdbcTemplate.queryForList(
                    """
                    SELECT u.id
                    FROM users u
                    LEFT JOIN partner_preferences pp
                           ON pp.user_id = u.id
                    WHERE u.is_active = true
                      AND pp.user_id IS NULL
                      AND u.id > ?
                    ORDER BY u.id
                    LIMIT ?
                    """,
                    Long.class,
                    lastUserId,
                    BATCH_SIZE
            );

            if (userIds.isEmpty()) {
                break;
            }

            insertBatch(userIds);

            inserted += userIds.size();

            lastUserId = userIds.get(userIds.size() - 1);

            log.info("Partner Preferences Inserted : {}", inserted);
        }

        log.info("==========================================");
        log.info("Partner Preference Seeder Completed");
        log.info("==========================================");
    }

    private void insertBatch(List<Long> userIds) {

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
                                userIds.get(index),
                                LocalDateTime.now()
                        );
                    }

                    @Override
                    public int getBatchSize() {
                        return userIds.size();
                    }
                });
    }

    private void fillStatement(
            PreparedStatement ps,
            Long userId,
            LocalDateTime now)
            throws SQLException {

        // Religion -> Caste

        Long religionId = masterDataCache.randomReligion();
        Long casteId = masterDataCache.randomCaste(religionId);

        // Country -> State -> City

        Long countryId = masterDataCache.randomCountry();
        Long stateId = masterDataCache.randomState(countryId);
        Long cityId = masterDataCache.randomCity(stateId);

        // Education

        Long educationLevelId = masterDataCache.randomEducationLevel();
        Long occupationId = masterDataCache.randomOccupation();
        Long maritalStatusId = masterDataCache.randomMaritalStatus();

        // Lifestyle

        Long dietId = masterDataCache.randomDiet();
        Long smokingId = masterDataCache.randomSmoking();
        Long drinkingId = masterDataCache.randomDrinking();

        // Height / Weight

        Long minHeightId = masterDataCache.randomHeight();
        Long maxHeightId = masterDataCache.randomHeight();

        Long minWeightId = masterDataCache.randomWeight();
        Long maxWeightId = masterDataCache.randomWeight();
        // Ensure valid ranges (min <= max)

        if (minHeightId != null && maxHeightId != null
                && minHeightId > maxHeightId) {

            Long temp = minHeightId;
            minHeightId = maxHeightId;
            maxHeightId = temp;
        }

        if (minWeightId != null && maxWeightId != null
                && minWeightId > maxWeightId) {

            Long temp = minWeightId;
            minWeightId = maxWeightId;
            maxWeightId = temp;
        }

        int i = 1;

        // User

        ps.setLong(i++, userId);

        // Age

        ps.setInt(i++, 21);
        ps.setInt(i++, 32);

        // Education

        ps.setObject(i++, educationLevelId, Types.BIGINT);
        ps.setObject(i++, occupationId, Types.BIGINT);

        // Other Expectations

        ps.setString(
                i++,
                "Looking for a caring, respectful and family oriented life partner."
        );

        // Marital Status

        ps.setObject(i++, maritalStatusId, Types.BIGINT);

        // Lifestyle

        ps.setObject(i++, smokingId, Types.BIGINT);
        ps.setObject(i++, drinkingId, Types.BIGINT);
        ps.setObject(i++, dietId, Types.BIGINT);

        // Height

        ps.setObject(i++, minHeightId, Types.BIGINT);
        ps.setObject(i++, maxHeightId, Types.BIGINT);

        // Weight

        ps.setObject(i++, minWeightId, Types.BIGINT);
        ps.setObject(i++, maxWeightId, Types.BIGINT);

        // Religion / Caste / City

        ps.setObject(i++, religionId, Types.BIGINT);
        ps.setObject(i++, casteId, Types.BIGINT);
        ps.setObject(i++, cityId, Types.BIGINT);

        // Status

        ps.setBoolean(i++, true);

        // Created

        ps.setTimestamp(i++, Timestamp.valueOf(now));
        ps.setNull(i++, Types.BIGINT);

        // Updated

        ps.setTimestamp(i++, Timestamp.valueOf(now));
        ps.setNull(i++, Types.BIGINT);

        // Deleted

        ps.setBoolean(i++, false);
        ps.setNull(i++, Types.TIMESTAMP);
        ps.setNull(i++, Types.BIGINT);
        ps.setNull(i++, Types.VARCHAR);

        // Version

        ps.setLong(i++, 0L);
    }
    /**
     * Total Partner Preferences Generated
     */
    public long getPartnerPreferenceCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM partner_preferences",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Remaining Active Users
     */
    public long getRemainingPartnerPreferences() {

        Long count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users u
                LEFT JOIN partner_preferences pp
                       ON pp.user_id = u.id
                WHERE u.is_active = true
                  AND pp.user_id IS NULL
                """,
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Print Seeder Summary
     */
    public void printSeederSummary() {

        long generated = getPartnerPreferenceCount();
        long remaining = getRemainingPartnerPreferences();

        log.info("======================================");
        log.info("PARTNER PREFERENCE SEEDER SUMMARY");
        log.info("======================================");
        log.info("Partner Preferences Generated : {}", generated);
        log.info("Remaining Active Users        : {}", remaining);
        log.info("======================================");
    }

}