package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSeeder {

    private static final int BATCH_SIZE = 10000;

    private final JdbcTemplate jdbcTemplate;
    private final MasterDataCache masterDataCache;
    private final RandomDataGenerator randomDataGenerator;

    private static final String INSERT_PROFILE_SQL = """
        INSERT INTO profiles (
        user_id,
        profile_type_id,
        manglik_status_id,
        family_type_id,
        family_status_id,
        family_value_id,
        religion_id,
        caste_id,
        sub_caste_id,
        country_id,
        state_id,
        city_id,
        mother_tongue_id,
        qualification_id,
        field_of_study_id,
        employed_id,
        disability_status_id,
        blood_group_id,
        marital_status_id,
        gender_id,
        education_level_id,
        occupation_id,
        height_id,
        weight_id,
        body_type_id,
        complexion_id,
        income_id,
        diet_id,
        smoking_id,
        drinking_id,
        date_of_birth,
        about,
        about_me,
        image_url,
        company_name,
        address,
        father_name,
        father_occupation,
        mother_name,
        mother_occupation,
        siblings_count,
        is_active,
        current_step,
        profile_completed,
        is_premium,
        premium_plan,
        premium_start_date,
        premium_end_date,
        boost_score,
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
        ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,
        ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
        )
        """;

    public void seedProfiles() {

        log.info("====================================");
        log.info("Generating Profiles...");
        log.info("====================================");

        long lastUserId = 0;
        long inserted = 0;

        while (true) {

            List<Long> userIds = jdbcTemplate.queryForList(
                    """
                    SELECT u.id
                    FROM users u
                    LEFT JOIN profiles p
                    ON p.user_id=u.id
                    WHERE u.is_active=true
                    AND p.user_id IS NULL
                    AND u.id>?
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

            log.info("{} Profiles Inserted", inserted);
        }

        log.info("====================================");
        log.info("Profile Seeder Completed");
        log.info("====================================");
    }

    private void insertBatch(List<Long> userIds) {

        jdbcTemplate.batchUpdate(
                INSERT_PROFILE_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index)
                            throws SQLException {

                        fillStatement(
                                ps,
                                userIds.get(index)
                        );
                    }

                    @Override
                    public int getBatchSize() {
                        return userIds.size();
                    }
                });
    }
    private void fillStatement(PreparedStatement ps, Long userId) throws SQLException {

        Long religionId = masterDataCache.randomReligion();
        Long casteId = masterDataCache.randomCaste(religionId);
        Long subCasteId = masterDataCache.randomSubCaste(casteId);

        Long countryId = masterDataCache.randomCountry();
        Long stateId = masterDataCache.randomState(countryId);
        Long cityId = masterDataCache.randomCity(stateId);

        Long genderId = masterDataCache.randomGender();

        Long motherTongueId = masterDataCache.randomMotherTongue();

        Long educationLevelId = masterDataCache.randomEducationLevel();
        Long qualificationId = masterDataCache.randomQualification();
        Long fieldOfStudyId = masterDataCache.randomFieldOfStudy();

        Long occupationId = masterDataCache.randomOccupation();
        Long incomeId = masterDataCache.randomIncome();

        Long heightId = masterDataCache.randomHeight();
        Long weightId = masterDataCache.randomWeight();

        Long bodyTypeId = masterDataCache.randomBodyType();
        Long complexionId = masterDataCache.randomComplexion();

        Long dietId = masterDataCache.randomDiet();
        Long smokingId = masterDataCache.randomSmoking();
        Long drinkingId = masterDataCache.randomDrinking();

        Long profileTypeId = masterDataCache.randomProfileType();
        Long manglikStatusId = masterDataCache.randomManglikStatus();

        Long familyTypeId = masterDataCache.randomFamilyType();
        Long familyStatusId = masterDataCache.randomFamilyStatus();
        Long familyValueId = masterDataCache.randomFamilyValue();

        Long employedId = masterDataCache.randomEmployed();
        Long disabilityStatusId = masterDataCache.randomDisabilityStatus();

        Long bloodGroupId = masterDataCache.randomBloodGroup();
        Long maritalStatusId = masterDataCache.randomMaritalStatus();

        LocalDateTime now = LocalDateTime.now();

        int i = 1;

        ps.setLong(i++, userId);
        ps.setLong(i++, profileTypeId);
        ps.setLong(i++, manglikStatusId);
        ps.setLong(i++, familyTypeId);
        ps.setLong(i++, familyStatusId);
        ps.setLong(i++, familyValueId);
        ps.setObject(i++, religionId, java.sql.Types.BIGINT);
        ps.setObject(i++, casteId, java.sql.Types.BIGINT);
        ps.setObject(i++, subCasteId, java.sql.Types.BIGINT);

        ps.setLong(i++, countryId);
        ps.setLong(i++, stateId);
        ps.setLong(i++, cityId);

        ps.setLong(i++, motherTongueId);

        ps.setLong(i++, qualificationId);
        ps.setLong(i++, fieldOfStudyId);

        ps.setLong(i++, employedId);
        ps.setLong(i++, disabilityStatusId);

        ps.setLong(i++, bloodGroupId);
        ps.setLong(i++, maritalStatusId);

        ps.setLong(i++, genderId);

        ps.setLong(i++, educationLevelId);
        ps.setLong(i++, occupationId);

        ps.setLong(i++, heightId);
        ps.setLong(i++, weightId);

        ps.setLong(i++, bodyTypeId);
        ps.setLong(i++, complexionId);

        ps.setLong(i++, incomeId);

        ps.setLong(i++, dietId);
        ps.setLong(i++, smokingId);
        ps.setLong(i++, drinkingId);

        ps.setDate(i++, Date.valueOf(
                randomDataGenerator.randomDateOfBirth(21, 30)));

        ps.setString(i++, randomDataGenerator.randomAbout());

        ps.setString(i++, randomDataGenerator.randomAboutMe());

        ps.setNull(i++, java.sql.Types.VARCHAR);

        ps.setString(i++, randomDataGenerator.randomCompany());

        ps.setString(i++, randomDataGenerator.randomStreetAddress());

        ps.setString(i++, randomDataGenerator.randomFatherName());

        ps.setString(i++, randomDataGenerator.randomFatherOccupation());

        ps.setString(i++, randomDataGenerator.randomMotherName());

        ps.setString(i++, randomDataGenerator.randomMotherOccupation());

        ps.setInt(i++, randomDataGenerator.randomSiblingsCount());

        // -------- Profile Status --------

        ps.setBoolean(i++, true);   // is_active

        ps.setInt(i++, 5);          // current_step

        ps.setBoolean(i++, true);   // profile_completed

        ps.setBoolean(i++, false);  // is_premium

        ps.setString(i++, "FREE");  // premium_plan

        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        ps.setInt(i++, randomDataGenerator.randomBoostScore());

        // -------- Audit Fields --------

        ps.setTimestamp(i++, Timestamp.valueOf(now));

        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setTimestamp(i++, Timestamp.valueOf(now));

        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setBoolean(i++, false);

        ps.setNull(i++, java.sql.Types.TIMESTAMP);

        ps.setNull(i++, java.sql.Types.BIGINT);

        ps.setNull(i++, java.sql.Types.VARCHAR);

        ps.setLong(i++, 0L);
    }
    /**
     * Optional method to know how many profiles already exist.
     */
    public long getProfileCount() {

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM profiles",
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Optional method to know how many active users are left
     * whose profiles are not generated.
     */
    public long getRemainingProfiles() {

        Long count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users u
                LEFT JOIN profiles p
                       ON p.user_id = u.id
                WHERE u.is_active = true
                  AND p.user_id IS NULL
                """,
                Long.class
        );

        return count == null ? 0 : count;
    }

    /**
     * Prints seeder statistics.
     */
    public void printSeederSummary() {

        long profiles = getProfileCount();
        long remaining = getRemainingProfiles();

        log.info("======================================");
        log.info("PROFILE SEEDER SUMMARY");
        log.info("======================================");
        log.info("Profiles Generated : {}", profiles);
        log.info("Remaining Profiles : {}", remaining);
        log.info("======================================");
    }

}