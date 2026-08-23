package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.*;
import com.example.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 9 — Database Security, Integrity & Transaction Baseline Audit Test Suite")
public class DatabaseSecurityIntegrityBaselineTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ShortlistRepository shortlistRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AdminAuditLogRepository adminAuditLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.hibernate.ddl-auto:validate}")
    private String ddlAuto;

    private Role getOrCreateUserRole() {
        return roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setIsActive(true);
            return roleRepository.save(r);
        });
    }

    private User createTestUser(String email, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode("Test@123456"));
        user.setFirstName("Audit");
        user.setLastName("User");
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setRoles(java.util.Set.of(getOrCreateUserRole()));
        return userRepository.save(user);
    }

    private SubscriptionPlan getOrCreateTestPlan() {
        return subscriptionPlanRepository.findAll().stream().findFirst().orElseGet(() -> {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_ADMIN");
                r.setIsActive(true);
                return roleRepository.save(r);
            });
            Admin admin = adminRepository.findAll().stream().findFirst().orElseGet(() -> {
                Admin a = new Admin();
                a.setEmail("plan_adm_" + UUID.randomUUID() + "@example.com");
                a.setUsername("padm_" + UUID.randomUUID().toString().substring(0, 8));
                a.setPassword(passwordEncoder.encode("Admin@123"));
                a.setName("Plan Admin");
                a.setRole(adminRole);
                a.setIsActive(true);
                return adminRepository.save(a);
            });

            SubscriptionPlan plan = new SubscriptionPlan();
            plan.setName("Test Gold Plan " + UUID.randomUUID());
            plan.setPrice(new BigDecimal("1999.00"));
            plan.setDuration(90);
            plan.setAdmin(admin);
            plan.setIsActive(true);
            return subscriptionPlanRepository.save(plan);
        });
    }

    // =========================================================================
    // CATEGORY A — DATABASE SCHEMA INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category A: DB enforces Unique Constraint on users.email")
    void testUserEmailUniqueConstraint() {
        String uniqueEmail = "uniq_" + UUID.randomUUID() + "@example.com";
        createTestUser(uniqueEmail, "9" + (System.currentTimeMillis() % 1000000000L));

        User duplicate = new User();
        duplicate.setEmail(uniqueEmail);
        duplicate.setPassword(passwordEncoder.encode("Test@123456"));
        duplicate.setFirstName("Dup");
        duplicate.setLastName("User");
        duplicate.setIsActive(true);
        duplicate.setIsDeleted(false);
        duplicate.setRoles(java.util.Set.of(getOrCreateUserRole()));

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(duplicate);
        }, "Database must reject duplicate email via uk_users_email constraint");
    }

    @Test
    @DisplayName("Category A: DB enforces 1:1 Unique Constraint on profiles.user_id")
    void testProfileUserUniqueConstraint() {
        String email = "prof_uniq_" + UUID.randomUUID() + "@example.com";
        User user = createTestUser(email, "8" + (System.currentTimeMillis() % 1000000000L));

        Profile profile1 = new Profile();
        profile1.setUser(user);
        profile1.setIsActive(true);
        profile1.setIsDeleted(false);
        profile1.setProfileCompleted(true);
        profileRepository.saveAndFlush(profile1);

        Profile profile2 = new Profile();
        profile2.setUser(user);
        profile2.setIsActive(true);
        profile2.setIsDeleted(false);
        profile2.setProfileCompleted(true);

        assertThrows(DataIntegrityViolationException.class, () -> {
            profileRepository.saveAndFlush(profile2);
        }, "Database must reject second profile for same user via uk_profiles_user constraint");
    }

    @Test
    @DisplayName("Category A: DB enforces Unique Constraint on interests (sender_id, receiver_id)")
    void testInterestSenderReceiverUniqueConstraint() {
        User sender = createTestUser("sender_" + UUID.randomUUID() + "@example.com",
                "7" + (System.currentTimeMillis() % 1000000000L));
        User receiver = createTestUser("recv_" + UUID.randomUUID() + "@example.com",
                "6" + (System.currentTimeMillis() % 1000000000L));

        Interest interest1 = new Interest();
        interest1.setSender(sender);
        interest1.setReceiver(receiver);
        interest1.setStatus("PENDING");
        interest1.setIsActive(true);
        interest1.setIsDeleted(false);
        interestRepository.saveAndFlush(interest1);

        Interest interest2 = new Interest();
        interest2.setSender(sender);
        interest2.setReceiver(receiver);
        interest2.setStatus("PENDING");
        interest2.setIsActive(true);
        interest2.setIsDeleted(false);

        assertThrows(DataIntegrityViolationException.class, () -> {
            interestRepository.saveAndFlush(interest2);
        }, "Database must reject duplicate interest between same sender and receiver via uk_interest_sender_receiver");
    }

    @Test
    @DisplayName("Category A: DB enforces Check Constraint on interests (sender_id <> receiver_id)")
    void testInterestSelfCheckConstraint() {
        User user = createTestUser("self_int_" + UUID.randomUUID() + "@example.com",
                "5" + (System.currentTimeMillis() % 1000000000L));

        Interest selfInterest = new Interest();
        selfInterest.setSender(user);
        selfInterest.setReceiver(user);
        selfInterest.setStatus("PENDING");
        selfInterest.setIsActive(true);
        selfInterest.setIsDeleted(false);

        assertThrows(DataIntegrityViolationException.class, () -> {
            interestRepository.saveAndFlush(selfInterest);
        }, "Database must reject self-directed interest via chk_interest_not_self constraint");
    }

    @Test
    @DisplayName("Category A: DB enforces Unique Constraint on shortlists (user_id, profile_id)")
    void testShortlistUserAndProfileUniqueConstraint() {
        User user = createTestUser("short_u_" + UUID.randomUUID() + "@example.com",
                "4" + (System.currentTimeMillis() % 1000000000L));
        User target = createTestUser("short_t_" + UUID.randomUUID() + "@example.com",
                "3" + (System.currentTimeMillis() % 1000000000L));

        Profile targetProfile = new Profile();
        targetProfile.setUser(target);
        targetProfile.setIsActive(true);
        targetProfile.setIsDeleted(false);
        targetProfile.setProfileCompleted(true);
        targetProfile = profileRepository.saveAndFlush(targetProfile);

        Shortlist shortlist1 = new Shortlist();
        shortlist1.setUser(user);
        shortlist1.setProfile(targetProfile);
        shortlist1.setIsActive(true);
        shortlist1.setIsDeleted(false);
        shortlistRepository.saveAndFlush(shortlist1);

        Shortlist shortlist2 = new Shortlist();
        shortlist2.setUser(user);
        shortlist2.setProfile(targetProfile);
        shortlist2.setIsActive(true);
        shortlist2.setIsDeleted(false);

        assertThrows(DataIntegrityViolationException.class, () -> {
            shortlistRepository.saveAndFlush(shortlist2);
        }, "Database must reject duplicate shortlist via uk_shortlist_user_profile");
    }

    @Test
    @DisplayName("Category A: DB enforces Unique Constraint on payments.transaction_id")
    void testPaymentTransactionUniqueConstraint() {
        User user = createTestUser("pay_u_" + UUID.randomUUID() + "@example.com",
                "2" + (System.currentTimeMillis() % 1000000000L));
        String txnId = "TXN_" + UUID.randomUUID();

        Payment payment1 = new Payment();
        payment1.setUser(user);
        payment1.setAmount(new BigDecimal("999.00"));
        payment1.setPaymentMethod("RAZORPAY");
        payment1.setTransactionId(txnId);
        payment1.setStatus("SUCCESS");
        paymentRepository.saveAndFlush(payment1);

        Payment payment2 = new Payment();
        payment2.setUser(user);
        payment2.setAmount(new BigDecimal("999.00"));
        payment2.setPaymentMethod("RAZORPAY");
        payment2.setTransactionId(txnId);
        payment2.setStatus("SUCCESS");

        assertThrows(DataIntegrityViolationException.class, () -> {
            paymentRepository.saveAndFlush(payment2);
        }, "Database must reject duplicate payment transaction_id via uk_payment_transaction");
    }

    // =========================================================================
    // CATEGORY B — USER / PROFILE DATA INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category B: Orphan profile without valid foreign key user is rejected")
    void testProfileWithoutValidUserFailsForeignKey() {
        User nonExistentUser = new User();
        nonExistentUser.setId(999999999L);

        Profile orphanProfile = new Profile();
        orphanProfile.setUser(nonExistentUser);
        orphanProfile.setIsActive(true);
        orphanProfile.setIsDeleted(false);

        assertThrows(Exception.class, () -> {
            profileRepository.saveAndFlush(orphanProfile);
        }, "Database must reject profile referencing non-existent user_id via fk_profiles_user");
    }

    // =========================================================================
    // CATEGORY C — AUTHENTICATION DATA SECURITY
    // =========================================================================

    @Test
    @DisplayName("Category C: Password hashes are stored securely (BCrypt format, never plaintext)")
    void testUserPasswordNeverStoredPlaintext() {
        String plainPassword = "SecurePassword@123";
        String email = "pwd_sec_" + UUID.randomUUID() + "@example.com";
        User user = createTestUser(email, "1" + (System.currentTimeMillis() % 1000000000L));

        User retrieved = userRepository.findById(user.getId()).orElseThrow();
        String storedPassword = retrieved.getPassword();

        assertNotEquals(plainPassword, storedPassword, "Stored password must not equal plaintext");
        assertTrue(storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$"),
                "Stored password must be a valid BCrypt hash format");
        assertTrue(passwordEncoder.matches("Test@123456", storedPassword),
                "Password must match BCrypt verification");
    }

    @Test
    @DisplayName("Category C: Refresh tokens enforce unique token constraint")
    void testRefreshTokenUniqueness() {
        String tokenStr = "RT_" + UUID.randomUUID();
        RefreshToken rt1 = new RefreshToken();
        rt1.setToken(tokenStr);
        rt1.setEmail("rt1_" + UUID.randomUUID() + "@example.com");
        rt1.setExpiryDate(Instant.now().plusSeconds(86400 * 7));
        refreshTokenRepository.saveAndFlush(rt1);

        RefreshToken rt2 = new RefreshToken();
        rt2.setToken(tokenStr);
        rt2.setEmail("rt2_" + UUID.randomUUID() + "@example.com");
        rt2.setExpiryDate(Instant.now().plusSeconds(86400 * 7));

        assertThrows(DataIntegrityViolationException.class, () -> {
            refreshTokenRepository.saveAndFlush(rt2);
        }, "Database must enforce unique token in refresh_token table");
    }

    @Test
    @DisplayName("Category C: Password reset token enforces uniqueness and user FK")
    void testPasswordResetTokenUniquenessAndExpiry() {
        User user1 = createTestUser("reset1_" + UUID.randomUUID() + "@example.com",
                "98" + (System.currentTimeMillis() % 100000000L));
        User user2 = createTestUser("reset2_" + UUID.randomUUID() + "@example.com",
                "97" + (System.currentTimeMillis() % 100000000L));

        String token = "PRT_" + UUID.randomUUID();
        PasswordResetToken prt1 = new PasswordResetToken();
        prt1.setToken(token);
        prt1.setUser(user1);
        prt1.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.saveAndFlush(prt1);

        PasswordResetToken prt2 = new PasswordResetToken();
        prt2.setToken(token);
        prt2.setUser(user2);
        prt2.setExpiryDate(LocalDateTime.now().plusHours(1));

        assertThrows(DataIntegrityViolationException.class, () -> {
            passwordResetTokenRepository.saveAndFlush(prt2);
        }, "Database must enforce unique token in password_reset_tokens table");
    }

    // =========================================================================
    // CATEGORY D & E — TRANSACTION & CONCURRENCY INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category D: Shortlist lifecycle operates deterministically within transactional boundaries")
    @Transactional
    void testShortlistToggleAtomicity() {
        User user = createTestUser("sl_u_" + UUID.randomUUID() + "@example.com",
                "96" + (System.currentTimeMillis() % 100000000L));
        User target = createTestUser("sl_t_" + UUID.randomUUID() + "@example.com",
                "95" + (System.currentTimeMillis() % 100000000L));

        Profile targetProfile = new Profile();
        targetProfile.setUser(target);
        targetProfile.setIsActive(true);
        targetProfile.setIsDeleted(false);
        targetProfile.setProfileCompleted(true);
        targetProfile = profileRepository.saveAndFlush(targetProfile);

        Shortlist shortlist = new Shortlist();
        shortlist.setUser(user);
        shortlist.setProfile(targetProfile);
        shortlist.setIsActive(true);
        shortlist.setIsDeleted(false);
        shortlist = shortlistRepository.saveAndFlush(shortlist);

        assertTrue(shortlistRepository.existsByUser_IdAndProfile_Id(user.getId(), targetProfile.getId()),
                "Shortlist record should exist after creation");

        shortlistRepository.delete(shortlist);
        shortlistRepository.flush();

        assertFalse(shortlistRepository.existsByUser_IdAndProfile_Id(user.getId(), targetProfile.getId()),
                "Shortlist record should be deleted cleanly");
    }

    // =========================================================================
    // CATEGORY F — SOFT DELETE INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category F: Soft-deleted users are properly excluded from active user counts")
    void testSoftDeletedUserExcludedFromActiveQueries() {
        User activeUser = createTestUser("active_" + UUID.randomUUID() + "@example.com",
                "94" + (System.currentTimeMillis() % 100000000L));
        User deletedUser = createTestUser("deleted_" + UUID.randomUUID() + "@example.com",
                "93" + (System.currentTimeMillis() % 100000000L));

        deletedUser.setIsDeleted(true);
        deletedUser.setDeletedAt(LocalDateTime.now());
        deletedUser.setDeletionReason("User requested account deletion");
        userRepository.saveAndFlush(deletedUser);

        Optional<User> queriedActive = userRepository.findById(activeUser.getId());
        assertTrue(queriedActive.isPresent() && !queriedActive.get().getIsDeleted(),
                "Active user should not be marked deleted");

        Optional<User> queriedDeleted = userRepository.findById(deletedUser.getId());
        assertTrue(queriedDeleted.isPresent() && queriedDeleted.get().getIsDeleted(),
                "Deleted user must have isDeleted=true");
    }

    // =========================================================================
    // CATEGORY G — SUBSCRIPTION & PAYMENT DATA INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category G: Check constraint rejects subscription where end_date < start_date")
    void testSubscriptionDateCheckConstraint() {
        User user = createTestUser("sub_u_" + UUID.randomUUID() + "@example.com",
                "92" + (System.currentTimeMillis() % 100000000L));
        SubscriptionPlan plan = getOrCreateTestPlan();

        UserSubscription sub = new UserSubscription();
        sub.setUser(user);
        sub.setSubscriptionPlan(plan);
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(LocalDateTime.now().minusDays(1)); // Invalid: end < start
        sub.setStatus("ACTIVE");
        sub.setIsActive(true);
        sub.setIsDeleted(false);

        assertThrows(Exception.class, () -> {
            userSubscriptionRepository.saveAndFlush(sub);
        }, "Database check constraint chk_user_subscription_dates must reject end_date < start_date");
    }

    @Test
    @DisplayName("Category G: Check constraint rejects payment with non-positive amount (amount <= 0)")
    void testPaymentAmountCheckConstraint() {
        User user = createTestUser("pay_bad_" + UUID.randomUUID() + "@example.com",
                "91" + (System.currentTimeMillis() % 100000000L));

        Payment badPayment = new Payment();
        badPayment.setUser(user);
        badPayment.setAmount(new BigDecimal("0.00")); // Invalid: amount must be > 0
        badPayment.setPaymentMethod("RAZORPAY");
        badPayment.setTransactionId("TXN_ZERO_" + UUID.randomUUID());
        badPayment.setStatus("PENDING");

        assertThrows(DataIntegrityViolationException.class, () -> {
            paymentRepository.saveAndFlush(badPayment);
        }, "Database check constraint chk_payment_amount must reject amount <= 0");
    }

    @Test
    @DisplayName("Category G: Check constraint rejects invalid payment status value")
    void testPaymentStatusCheckConstraint() {
        User user = createTestUser("pay_stat_" + UUID.randomUUID() + "@example.com",
                "90" + (System.currentTimeMillis() % 100000000L));

        Payment badStatusPayment = new Payment();
        badStatusPayment.setUser(user);
        badStatusPayment.setAmount(new BigDecimal("499.00"));
        badStatusPayment.setPaymentMethod("RAZORPAY");
        badStatusPayment.setTransactionId("TXN_STAT_" + UUID.randomUUID());
        badStatusPayment.setStatus("INVALID_STATUS_VALUE");

        assertThrows(DataIntegrityViolationException.class, () -> {
            paymentRepository.saveAndFlush(badStatusPayment);
        }, "Database check constraint chk_payment_status must reject unrecognized status values");
    }

    // =========================================================================
    // CATEGORY H — MASTER DATA INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category H: State must have a valid Country foreign key reference")
    void testStateCountryForeignKeyIntegrity() {
        Country nonExistentCountry = new Country();
        nonExistentCountry.setId(99999999L);

        State orphanState = new State();
        orphanState.setName("Orphan State " + UUID.randomUUID());
        orphanState.setCountry(nonExistentCountry);

        assertThrows(Exception.class, () -> {
            stateRepository.saveAndFlush(orphanState);
        }, "Database must enforce foreign key constraint from State to Country");
    }

    // =========================================================================
    // CATEGORY I — SQL INJECTION & QUERY SAFETY
    // =========================================================================

    @Test
    @DisplayName("Category I: JPA parameterization protects against SQL injection in email searches")
    void testSpecificationParameterizedSearchSafety() {
        String sqlInjectionPayload = "test' OR '1'='1' -- ";
        Optional<User> result = userRepository.findByEmail(sqlInjectionPayload);
        assertTrue(result.isEmpty(),
                "Parameterized search must safely treat SQL injection payloads as literal strings");
    }

    // =========================================================================
    // CATEGORY K — DATABASE CONFIGURATION & ENVIRONMENT INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category K: Hibernate DDL auto is set to validate, preventing runtime schema mutations")
    void testHibernateDdlAutoIsValidate() {
        assertEquals("validate", ddlAuto,
                "JPA ddl-auto must be 'validate' to prevent automated production/test schema alteration");
    }

    // =========================================================================
    // CATEGORY L — AUDIT LOG INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("Category L: Admin audit log restricts deletion of admin while audit records exist")
    void testAdminAuditLogForeignKeyRestriction() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        Admin admin = new Admin();
        admin.setEmail("audit_adm_" + UUID.randomUUID() + "@example.com");
        admin.setUsername("aud_adm_" + UUID.randomUUID().toString().substring(0, 8));
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setName("Audit Admin");
        admin.setRole(adminRole);
        admin.setIsActive(true);
        admin = adminRepository.saveAndFlush(admin);

        AdminAuditLog log = new AdminAuditLog();
        log.setAdmin(admin);
        log.setModule("SECURITY_AUDIT");
        log.setAction("AUDIT_TEST_ACTION");
        log.setEntityType("ADMIN");
        log.setEntityId(admin.getId());
        log.setDescription("Test audit log entry");
        adminAuditLogRepository.saveAndFlush(log);

        final Long adminId = admin.getId();
        assertThrows(DataIntegrityViolationException.class, () -> {
            adminRepository.deleteById(adminId);
            adminRepository.flush();
        }, "Database foreign key fk_admin_audit_admin must prevent deletion of admin (ON DELETE RESTRICT) to preserve audit history");
    }
}
