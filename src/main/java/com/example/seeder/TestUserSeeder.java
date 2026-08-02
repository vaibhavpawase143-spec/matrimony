package com.example.seeder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestUserSeeder {

    private static final Long ROLE_USER_ID = 3L;

    /**
     * Password = 123456
     */
    private static final String PASSWORD_HASH =
            "$2a$10$wzlpqIdfZYI7jpBLqhWULuHzHES9y7CL9lOIkk1diwgzinNhWmc3S";

    private final JdbcTemplate jdbcTemplate;
    private final RandomDataGenerator randomDataGenerator;
    private final MasterDataCache masterDataCache;

    @Value("${app.seeder.batch-size:10000}")
    private int batchSize;

    public void seedUsers(int totalUsers) {

        Long existingUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users",
                Long.class
        );

        if (existingUsers == null) {
            existingUsers = 0L;
        }

        if (existingUsers >= totalUsers) {

            log.info("=========================================");
            log.info("User Seeder Skipped");
            log.info("Existing Users : {}", existingUsers);
            log.info("Target Users   : {}", totalUsers);
            log.info("=========================================");

            return;
        }

        int start = existingUsers.intValue() + 1;

        log.info("=========================================");
        log.info("Starting User Seeder");
        log.info("Existing Users : {}", existingUsers);
        log.info("Target Users   : {}", totalUsers);
        log.info("Starting From  : {}", start);
        log.info("Batch Size     : {}", batchSize);
        log.info("=========================================");

        while (start <= totalUsers) {

            int end = Math.min(start + batchSize - 1, totalUsers);

            List<UserSeedData> users = new ArrayList<>();

            for (int i = start; i <= end; i++) {
                users.add(createUser(i));
            }

            batchInsertUsers(users);

            batchInsertRoles(users);

            log.info("Inserted {} / {}", end, totalUsers);

            start = end + 1;
        }

        log.info("=========================================");
        log.info("User Seeder Completed");
        log.info("=========================================");
    }

    /**
     * Creates one fake user.
     */
    private UserSeedData createUser(int index) {

        LocalDateTime now = LocalDateTime.now();

        Long id = jdbcTemplate.queryForObject(
                "SELECT nextval('users_id_seq')",
                Long.class
        );

        boolean male = randomDataGenerator.randomBoolean();

        String firstName = male
                ? randomDataGenerator.randomMaleFirstName()
                : randomDataGenerator.randomFemaleFirstName();

        String middleName = male
                ? randomDataGenerator.randomMaleFirstName()
                : randomDataGenerator.randomFemaleFirstName();

        String lastName = randomDataGenerator.randomLastName();

        String email = randomDataGenerator.randomEmail(
                firstName,
                lastName,
                index
        );

        String phone = randomDataGenerator.randomMobile();

        return new UserSeedData(

                id,

                firstName,
                middleName,
                lastName,

                email,
                phone,

                PASSWORD_HASH,

                true,
                true,
                true,

                Timestamp.valueOf(now),
                Timestamp.valueOf(now),

                false,

                false,

                0,

                Timestamp.valueOf(now),
                Timestamp.valueOf(now)
        );
    }

    @Data
    @AllArgsConstructor
    private static class UserSeedData {

        private Long id;

        private String firstName;
        private String middleName;
        private String lastName;

        private String email;
        private String phone;

        private String password;

        private boolean active;

        private boolean emailVerified;
        private boolean phoneVerified;

        private Timestamp emailVerifiedAt;
        private Timestamp phoneVerifiedAt;

        private boolean online;

        private boolean blocked;

        private Integer reportCount;

        private Timestamp createdAt;
        private Timestamp updatedAt;
    }

    private void batchInsertUsers(List<UserSeedData> users) {

        String sql = """
            INSERT INTO users
            (
                id,
                first_name,
                middle_name,
                last_name,
                email,
                phone,
                password,

                is_active,
                email_verified,
                phone_verified,

                email_verified_at,
                phone_verified_at,

                otp,
                otp_expiry,

                is_online,
                last_seen,
                last_login,
                last_heartbeat,

                is_blocked,
                report_count,

                session_id,

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
                ?,?,?,?,?,?,?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?,?,
                ?
            )
            """;

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index
                    ) throws java.sql.SQLException {

                        UserSeedData u = users.get(index);

                        int i = 1;

                        ps.setLong(i++, u.getId());

                        ps.setString(i++, u.getFirstName());
                        ps.setString(i++, u.getMiddleName());
                        ps.setString(i++, u.getLastName());

                        ps.setString(i++, u.getEmail());
                        ps.setString(i++, u.getPhone());

                        ps.setString(i++, u.getPassword());

                        ps.setBoolean(i++, u.isActive());

                        ps.setBoolean(i++, u.isEmailVerified());
                        ps.setBoolean(i++, u.isPhoneVerified());

                        ps.setTimestamp(i++, u.getEmailVerifiedAt());
                        ps.setTimestamp(i++, u.getPhoneVerifiedAt());

                        ps.setNull(i++, java.sql.Types.VARCHAR);      // otp
                        ps.setNull(i++, java.sql.Types.TIMESTAMP);    // otp_expiry

                        ps.setBoolean(i++, u.isOnline());

                        ps.setNull(i++, java.sql.Types.TIMESTAMP);    // last_seen
                        ps.setNull(i++, java.sql.Types.TIMESTAMP);    // last_login
                        ps.setNull(i++, java.sql.Types.TIMESTAMP);    // last_heartbeat

                        ps.setBoolean(i++, u.isBlocked());

                        ps.setInt(i++, u.getReportCount());

                        ps.setNull(i++, java.sql.Types.VARCHAR);      // session_id

                        ps.setTimestamp(i++, u.getCreatedAt());
                        ps.setNull(i++, java.sql.Types.BIGINT);       // created_by

                        ps.setTimestamp(i++, u.getUpdatedAt());
                        ps.setNull(i++, java.sql.Types.BIGINT);       // updated_by

                        ps.setBoolean(i++, false);                    // is_deleted

                        ps.setNull(i++, java.sql.Types.TIMESTAMP);    // deleted_at
                        ps.setNull(i++, java.sql.Types.BIGINT);       // deleted_by
                        ps.setNull(i++, java.sql.Types.VARCHAR);      // deletion_reason

                        ps.setLong(i++, 0L);                          // version
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );
    }

    private void batchInsertRoles(List<UserSeedData> users) {

        String sql = """
            INSERT INTO users_roles
            (
                users_id,
                roles_id
            )
            VALUES
            (
                ?, ?
            )
            """;

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index
                    ) throws SQLException {

                        UserSeedData user = users.get(index);

                        ps.setLong(1, user.getId());

                        ps.setLong(2, ROLE_USER_ID);
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );
    }
}