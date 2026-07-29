package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestUserSeeder {

    private static final Long ROLE_USER_ID = 3L;

    private static final String DEFAULT_PASSWORD =
            "$2a$10$MmMmwTtRw0dPq8reYsO4zOLtqkSX13bkWI3yJC1m1nmXsop.SnQFS";


    private static final String INSERT_USER_SQL = """
        INSERT INTO users
        (
            first_name,
            middle_name,
            last_name,
            email,
            phone,
            password,
            is_active,
            email_verified,
            phone_verified,
            is_online,
            is_blocked,
            report_count,
            created_at,
            version,
            is_deleted
        )
        VALUES
        (
            ?,?,?,?,?,?,
            ?,?,?,?,
            ?,?,?,?,?
        )
        RETURNING id
        """;


    private static final String INSERT_USER_ROLE_SQL = """
        INSERT INTO users_roles
        (
            users_id,
            roles_id
        )
        VALUES
        (
            ?,?
        )
        """;
    private static final String INSERT_PROFILE_SQL = """
INSERT INTO profiles
(
    user_id,
    religion_id,
    caste_id,
    sub_caste_id,
    country_id,
    state_id,
    city_id,
    mother_tongue_id,
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
    gender_id,
    date_of_birth,
    about,
    about_me,
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
    boost_score,
    created_at,
    version,
    is_deleted
)
VALUES
(
    ?,?,?,?,?,?,
    ?,?,?,?,?,?,
    ?,?,?,?,?,?,
    ?,?,?,?,?,?,
    ?,?,?,?,?,?,
    ?,?,?,?,?,?,
    ?,?
)
""";

    private final JdbcTemplate jdbcTemplate;

    private final MasterDataCache masterDataCache;


    @Value("${app.seeder.total-users}")
    private int totalUsers;


    @Value("${app.seeder.batch-size}")
    private int batchSize;



    @Transactional
    public void seedUsers() {

        log.info("========================================");
        log.info("Starting Test User Seeder...");
        log.info("Total Users : {}", totalUsers);
        log.info("Batch Size  : {}", batchSize);
        log.info("========================================");


        for (int start = 1; start <= totalUsers; start += batchSize) {

            int end = Math.min(start + batchSize - 1, totalUsers);

            log.info("Processing Batch : {} -> {}", start, end);

            generateBatch(start, end);

            log.info("Completed Batch : {} -> {}", start, end);
        }


        log.info("Seeder Completed Successfully");
    }



    private void generateBatch(int start, int end) {


        List<Object[]> users = new ArrayList<>();


        for (int i = start; i <= end; i++) {


            String firstName = RandomDataGenerator.firstName();

            String middleName = RandomDataGenerator.middleName();

            String lastName = RandomDataGenerator.lastName();


            String email = RandomDataGenerator.email(i);

            String phone = RandomDataGenerator.phone(i);



            users.add(new Object[]{

                    firstName,
                    middleName,
                    lastName,

                    email,
                    phone,

                    DEFAULT_PASSWORD,

                    true,       // is_active
                    true,       // email_verified
                    true,       // phone_verified

                    false,      // is_online
                    false,      // is_blocked

                    0,          // report_count

                    LocalDateTime.now(),

                    0,          // version

                    false       // is_deleted
            });

        }



        // ===============================
        // INSERT USERS AND GET IDS
        // ===============================

        List<Long> generatedUserIds = new ArrayList<>();


        for (Object[] user : users) {


            Long id = jdbcTemplate.queryForObject(
                    INSERT_USER_SQL,
                    Long.class,
                    user
            );


            generatedUserIds.add(id);

        }



        // ===============================
        // INSERT USERS ROLES
        // ===============================


        List<Object[]> userRoles = new ArrayList<>();


        for (Long userId : generatedUserIds) {


            userRoles.add(new Object[]{

                    userId,
                    ROLE_USER_ID

            });

        }



        jdbcTemplate.batchUpdate(
                INSERT_USER_ROLE_SQL,
                userRoles
        );

        List<Object[]> profiles = new ArrayList<>();

        for (Long userId : generatedUserIds) {

            profiles.add(new Object[]{

                    userId,

                    masterDataCache.randomReligion(),
                    masterDataCache.randomCaste(),
                    masterDataCache.randomSubCaste(),

                    masterDataCache.randomCountry(),
                    masterDataCache.randomState(),
                    masterDataCache.randomCity(),

                    masterDataCache.randomMotherTongue(),

                    masterDataCache.randomEducationLevel(),
                    masterDataCache.randomOccupation(),

                    masterDataCache.randomHeight(),
                    masterDataCache.randomWeight(),

                    masterDataCache.randomBodyType(),
                    masterDataCache.randomComplexion(),

                    masterDataCache.randomIncome(),

                    masterDataCache.randomDiet(),
                    masterDataCache.randomSmoking(),
                    masterDataCache.randomDrinking(),

                    masterDataCache.randomGender(),

                    RandomDataGenerator.randomDate().toLocalDate(),

                    "I am a simple and hardworking person.",
                    "Looking for a compatible life partner.",

                    "TCS",

                    "Pune, Maharashtra",

                    "Rajesh",
                    "Business",

                    "Sunita",
                    "Teacher",

                    2,

                    true,        // is_active
                    5,           // current_step
                    true,        // profile_completed
                    false,       // is_premium

                    "FREE",

                    0,           // boost_score

                    LocalDateTime.now(),

                    0,

                    false

            });


        }
        jdbcTemplate.batchUpdate(
                INSERT_PROFILE_SQL,
                profiles
        );

    }

}