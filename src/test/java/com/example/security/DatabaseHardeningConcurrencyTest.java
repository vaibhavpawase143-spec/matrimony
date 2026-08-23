package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.RefreshTokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 9 Hardening — Database Integrity, Concurrency & Security Remediations Test Suite")
public class DatabaseHardeningConcurrencyTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

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
        user.setFirstName("Hardening");
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
                a.setEmail("hard_adm_" + UUID.randomUUID() + "@example.com");
                a.setUsername("hadm_" + UUID.randomUUID().toString().substring(0, 8));
                a.setPassword(passwordEncoder.encode("Admin@123"));
                a.setName("Hardening Admin");
                a.setRole(adminRole);
                a.setIsActive(true);
                return adminRepository.save(a);
            });

            SubscriptionPlan plan = new SubscriptionPlan();
            plan.setName("Hardening Gold Plan " + UUID.randomUUID());
            plan.setPrice(new BigDecimal("1999.00"));
            plan.setDuration(90);
            plan.setAdmin(admin);
            plan.setIsActive(true);
            return subscriptionPlanRepository.save(plan);
        });
    }

    // =========================================================================
    // VULN-DB-01: ACTIVE SUBSCRIPTION CONCURRENCY HARDENING
    // =========================================================================

    @Test
    @DisplayName("VULN-DB-01: Partial Unique Index uq_active_user_subscription blocks duplicate ACTIVE subscriptions")
    void testConcurrentActiveSubscriptionCreationBlockedByDb() {
        User user = createTestUser("sub_race_" + UUID.randomUUID() + "@example.com", "88" + (System.currentTimeMillis() % 100000000L));
        SubscriptionPlan plan = getOrCreateTestPlan();

        UserSubscription sub1 = new UserSubscription();
        sub1.setUser(user);
        sub1.setSubscriptionPlan(plan);
        sub1.setStartDate(LocalDateTime.now());
        sub1.setEndDate(LocalDateTime.now().plusDays(30));
        sub1.setStatus("ACTIVE");
        sub1.setIsActive(true);
        sub1.setIsDeleted(false);
        userSubscriptionRepository.saveAndFlush(sub1);

        UserSubscription sub2 = new UserSubscription();
        sub2.setUser(user);
        sub2.setSubscriptionPlan(plan);
        sub2.setStartDate(LocalDateTime.now());
        sub2.setEndDate(LocalDateTime.now().plusDays(30));
        sub2.setStatus("ACTIVE");
        sub2.setIsActive(true);
        sub2.setIsDeleted(false);

        // Attempting to save a second ACTIVE subscription for the same user MUST violate uq_active_user_subscription
        assertThrows(DataIntegrityViolationException.class, () -> {
            userSubscriptionRepository.saveAndFlush(sub2);
        }, "Database partial unique index uq_active_user_subscription must strictly reject concurrent second active subscription");
    }

    @Test
    @DisplayName("VULN-DB-01: Allows multiple INACTIVE/EXPIRED subscriptions but only ONE ACTIVE subscription")
    void testMultipleInactiveSubscriptionsAllowed() {
        User user = createTestUser("sub_multi_" + UUID.randomUUID() + "@example.com", "87" + (System.currentTimeMillis() % 100000000L));
        SubscriptionPlan plan = getOrCreateTestPlan();

        // 1st Expired Subscription
        UserSubscription oldSub1 = new UserSubscription();
        oldSub1.setUser(user);
        oldSub1.setSubscriptionPlan(plan);
        oldSub1.setStartDate(LocalDateTime.now().minusMonths(3));
        oldSub1.setEndDate(LocalDateTime.now().minusMonths(2));
        oldSub1.setStatus("EXPIRED");
        oldSub1.setIsActive(false);
        oldSub1.setIsDeleted(false);
        userSubscriptionRepository.saveAndFlush(oldSub1);

        // 2nd Cancelled Subscription
        UserSubscription oldSub2 = new UserSubscription();
        oldSub2.setUser(user);
        oldSub2.setSubscriptionPlan(plan);
        oldSub2.setStartDate(LocalDateTime.now().minusMonths(2));
        oldSub2.setEndDate(LocalDateTime.now().minusMonths(1));
        oldSub2.setStatus("CANCELLED");
        oldSub2.setIsActive(false);
        oldSub2.setIsDeleted(false);
        userSubscriptionRepository.saveAndFlush(oldSub2);

        // 3rd Current Active Subscription
        UserSubscription currentActive = new UserSubscription();
        currentActive.setUser(user);
        currentActive.setSubscriptionPlan(plan);
        currentActive.setStartDate(LocalDateTime.now());
        currentActive.setEndDate(LocalDateTime.now().plusDays(30));
        currentActive.setStatus("ACTIVE");
        currentActive.setIsActive(true);
        currentActive.setIsDeleted(false);
        UserSubscription savedActive = userSubscriptionRepository.saveAndFlush(currentActive);

        assertNotNull(savedActive.getId(), "Current active subscription should be saved successfully");
    }

    // =========================================================================
    // VULN-DB-02: PRIMARY PHOTO CONCURRENCY HARDENING
    // =========================================================================

    @Test
    @DisplayName("VULN-DB-02: Partial Unique Index uq_primary_user_photo blocks duplicate non-deleted primary photos")
    void testConcurrentPrimaryPhotoCreationBlockedByDb() {
        User user = createTestUser("photo_race_" + UUID.randomUUID() + "@example.com", "86" + (System.currentTimeMillis() % 100000000L));

        UserPhoto photo1 = new UserPhoto();
        photo1.setUser(user);
        photo1.setPhotoType(PhotoType.PROFILE);
        photo1.setPhotoUrl("http://localhost:9090/uploads/photo1.jpg");
        photo1.setPrimaryPhoto(true);
        photo1.setIsDeleted(false);
        userPhotoRepository.saveAndFlush(photo1);

        UserPhoto photo2 = new UserPhoto();
        photo2.setUser(user);
        photo2.setPhotoType(PhotoType.PROFILE);
        photo2.setPhotoUrl("http://localhost:9090/uploads/photo2.jpg");
        photo2.setPrimaryPhoto(true);
        photo2.setIsDeleted(false);

        // Attempting to save a second non-deleted primary photo for the same user MUST violate uq_primary_user_photo
        assertThrows(DataIntegrityViolationException.class, () -> {
            userPhotoRepository.saveAndFlush(photo2);
        }, "Database partial unique index uq_primary_user_photo must strictly reject concurrent second primary photo");
    }

    @Test
    @DisplayName("VULN-DB-02: Allows multiple non-primary gallery photos for the same user")
    void testMultipleGalleryPhotosAllowed() {
        User user = createTestUser("gallery_" + UUID.randomUUID() + "@example.com", "85" + (System.currentTimeMillis() % 100000000L));

        UserPhoto primaryPhoto = new UserPhoto();
        primaryPhoto.setUser(user);
        primaryPhoto.setPhotoType(PhotoType.PROFILE);
        primaryPhoto.setPhotoUrl("http://localhost:9090/uploads/primary.jpg");
        primaryPhoto.setPrimaryPhoto(true);
        primaryPhoto.setIsDeleted(false);
        userPhotoRepository.saveAndFlush(primaryPhoto);

        UserPhoto gallery1 = new UserPhoto();
        gallery1.setUser(user);
        gallery1.setPhotoType(PhotoType.OTHER);
        gallery1.setPhotoUrl("http://localhost:9090/uploads/gallery1.jpg");
        gallery1.setPrimaryPhoto(false);
        gallery1.setIsDeleted(false);
        userPhotoRepository.saveAndFlush(gallery1);

        UserPhoto gallery2 = new UserPhoto();
        gallery2.setUser(user);
        gallery2.setPhotoType(PhotoType.OTHER);
        gallery2.setPhotoUrl("http://localhost:9090/uploads/gallery2.jpg");
        gallery2.setPrimaryPhoto(false);
        gallery2.setIsDeleted(false);
        userPhotoRepository.saveAndFlush(gallery2);

        assertEquals(3, userPhotoRepository.findByUserId(user.getId()).size(), "User should have 3 total photos (1 primary, 2 gallery)");
    }

    // =========================================================================
    // VULN-DB-03: REFRESH TOKEN ORPHAN INTEGRITY HARDENING
    // =========================================================================

    @Test
    @DisplayName("VULN-DB-03: Purge orphan & expired tokens reliably without affecting valid active users/admins")
    void testPurgeOrphanAndExpiredTokens() {
        User validUser = createTestUser("valid_rt_" + UUID.randomUUID() + "@example.com", "84" + (System.currentTimeMillis() % 100000000L));

        // 1. Valid Active User Token
        RefreshToken validToken = RefreshToken.builder()
                .email(validUser.getEmail())
                .token("VALID_" + UUID.randomUUID())
                .expiryDate(Instant.now().plusSeconds(86400 * 7))
                .build();
        refreshTokenRepository.saveAndFlush(validToken);

        // 2. Expired Token
        RefreshToken expiredToken = RefreshToken.builder()
                .email(validUser.getEmail() + ".expired")
                .token("EXPIRED_" + UUID.randomUUID())
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();
        refreshTokenRepository.saveAndFlush(expiredToken);

        // 3. Orphan Token (Non-existent email)
        RefreshToken orphanToken = RefreshToken.builder()
                .email("nonexistent_orphan_" + UUID.randomUUID() + "@example.com")
                .token("ORPHAN_" + UUID.randomUUID())
                .expiryDate(Instant.now().plusSeconds(86400))
                .build();
        refreshTokenRepository.saveAndFlush(orphanToken);

        // Run automated purge
        int purgedCount = refreshTokenService.purgeOrphanAndExpiredTokens();
        assertTrue(purgedCount >= 2, "Purge should clean up at least the expired and orphan tokens");

        // Verify valid token is retained
        assertTrue(refreshTokenRepository.findByToken(validToken.getToken()).isPresent(), "Valid token must be preserved");
        // Verify orphan and expired tokens are deleted
        assertTrue(refreshTokenRepository.findByToken(expiredToken.getToken()).isEmpty(), "Expired token must be purged");
        assertTrue(refreshTokenRepository.findByToken(orphanToken.getToken()).isEmpty(), "Orphan token must be purged");
    }

    // =========================================================================
    // VULN-DB-04: PARTIAL INDEX PERFORMANCE VERIFICATION
    // =========================================================================

    @Test
    @DisplayName("VULN-DB-04: Verify all PostgreSQL partial indexes created by V136 migration are active in database")
    void testPartialIndexesExistAndAreValid() {
        List<?> indexNames = entityManager.createNativeQuery("""
            SELECT indexname
            FROM pg_indexes
            WHERE schemaname = 'public'
              AND indexname IN (
                  'uq_active_user_subscription',
                  'uq_primary_user_photo',
                  'idx_users_active_not_deleted',
                  'idx_users_email_not_deleted',
                  'idx_users_phone_not_deleted',
                  'idx_profiles_user_not_deleted',
                  'idx_interests_sender_active_not_deleted',
                  'idx_interests_receiver_active_not_deleted'
              )
        """).getResultList();

        System.out.println("=== Verified Hardened Indexes in DB: " + indexNames + " ===");
        assertTrue(indexNames.contains("uq_active_user_subscription"), "uq_active_user_subscription must exist in pg_indexes");
        assertTrue(indexNames.contains("uq_primary_user_photo"), "uq_primary_user_photo must exist in pg_indexes");
        assertTrue(indexNames.contains("idx_users_active_not_deleted"), "idx_users_active_not_deleted must exist in pg_indexes");
        assertTrue(indexNames.contains("idx_users_email_not_deleted"), "idx_users_email_not_deleted must exist in pg_indexes");
        assertTrue(indexNames.contains("idx_profiles_user_not_deleted"), "idx_profiles_user_not_deleted must exist in pg_indexes");
        assertTrue(indexNames.contains("idx_interests_sender_active_not_deleted"), "idx_interests_sender_active_not_deleted must exist in pg_indexes");
        assertTrue(indexNames.contains("idx_interests_receiver_active_not_deleted"), "idx_interests_receiver_active_not_deleted must exist in pg_indexes");
    }
}
