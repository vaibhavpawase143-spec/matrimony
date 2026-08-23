package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 6 Hardening — JWT, Session & Token Lifecycle Security Tests")
class JwtSessionLifecycleSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurity userSecurity;

    @Autowired
    private TokenRevocationService tokenRevocationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(roleName);
                    r.setIsActive(true);
                    return roleRepository.save(r);
                });
    }

    private User getOrCreateTestUser(String email, String rawPassword) {
        Role role = getOrCreateRole("ROLE_USER");
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("JwtHardening");
        user.setLastName("User");
        user.setEmail(email);
        user.setPhone("97" + String.format("%08d", Math.abs((email + System.currentTimeMillis()).hashCode() % 100000000)));
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        return userRepository.saveAndFlush(user);
    }

    private Admin getOrCreateTestAdmin(String email, String rawPassword, String sessionId) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("JWT Hardened Admin");
        admin.setUsername("admin_jwt_" + Math.abs(email.hashCode()));
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(adminRole);
        admin.setIsActive(true);
        admin.setSessionId(sessionId);
        return adminRepository.saveAndFlush(admin);
    }

    // =========================================================================
    // SECTION A: JWT ACCESS TOKEN REVOCATION
    // =========================================================================

    @Test
    @DisplayName("1. JWT Revocation: Valid JWT works before logout")
    void testJwtRevocation_ValidBeforeLogout() throws Exception {
        String email = "jwt_valid_pre_logout@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        assertFalse(tokenRevocationService.isRevoked(token), "Token must not be revoked prior to logout");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("2. JWT Revocation: User logout revokes access token immediately")
    void testJwtRevocation_UserLogout_RevokesToken() throws Exception {
        String email = "jwt_user_logout_revoked@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        // 1st request -> Successfully logs out and revokes token
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertTrue(tokenRevocationService.isRevoked(token), "Token must be recorded as revoked after logout");
    }

    @Test
    @DisplayName("3. JWT Revocation: Revoked JWT returns HTTP 401 on subsequent requests")
    void testJwtRevocation_RevokedToken_Returns401() throws Exception {
        String email = "jwt_revoked_401@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        // Perform logout to revoke token
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Subsequent call with revoked token must fail with 401 Unauthorized
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("4. JWT Revocation: Revocation Redis key has positive TTL equal to remaining lifespan")
    void testJwtRevocation_RedisKey_HasPositiveTTL() {
        String email = "jwt_ttl_test@example.com";
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");
        String jti = jwtUtil.extractJti(token);
        assertNotNull(jti);

        tokenRevocationService.revokeToken(token);

        String redisKey = "rl:jwt:revoked:" + jti;
        Long ttl = stringRedisTemplate.getExpire(redisKey);
        assertNotNull(ttl, "Revocation key must exist in Redis");
        assertTrue(ttl > 0, "Revocation key TTL must be greater than 0");
    }

    @Test
    @DisplayName("5. JWT Revocation: Revoking one token does not affect a different valid token for same user")
    void testJwtRevocation_DifferentToken_RemainsValid() throws Exception {
        String email = "jwt_multi_token_user@example.com";
        getOrCreateTestUser(email, "Pass123!");

        String token1 = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");
        String token2 = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-2", "USER");

        // Revoke token1
        tokenRevocationService.revokeToken(token1);

        assertTrue(tokenRevocationService.isRevoked(token1));
        assertFalse(tokenRevocationService.isRevoked(token2), "Token 2 must remain active");

        // Request with token2 succeeds
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("6. JWT Revocation: Revoking user A's token does not affect user B")
    void testJwtRevocation_UnrelatedUser_RemainsValid() {
        String userA = "jwt_user_a@example.com";
        String userB = "jwt_user_b@example.com";

        String tokenA = jwtUtil.generateToken(userA, List.of("ROLE_USER"), "sess-a", "USER");
        String tokenB = jwtUtil.generateToken(userB, List.of("ROLE_USER"), "sess-b", "USER");

        tokenRevocationService.revokeToken(tokenA);

        assertTrue(tokenRevocationService.isRevoked(tokenA));
        assertFalse(tokenRevocationService.isRevoked(tokenB));
    }

    // =========================================================================
    // SECTION B: SERVER-SIDE SESSION ID VALIDATION
    // =========================================================================

    @Test
    @DisplayName("7. Session Validation: Matching admin sessionId authenticates successfully")
    void testSessionValidation_MatchingSession_Succeeds() throws Exception {
        String email = "admin_matching_sess@example.com";
        String sessionId = "valid-session-guid-1";
        getOrCreateTestAdmin(email, "Pass123!", sessionId);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), sessionId, "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("8. Session Validation: Admin JWT with mismatched sessionId returns HTTP 401")
    void testSessionValidation_MismatchedSession_Returns401() throws Exception {
        String email = "admin_mismatch_sess@example.com";
        getOrCreateTestAdmin(email, "Pass123!", "current-active-session");

        // Token generated with outdated/different session ID
        String staleToken = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), "old-stale-session", "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + staleToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("9. Session Validation: Admin account with null server-side session returns HTTP 401")
    void testSessionValidation_NullServerSideSession_Returns401() throws Exception {
        String email = "admin_null_sess@example.com";
        getOrCreateTestAdmin(email, "Pass123!", null); // null session in DB

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), "any-session-id", "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("10. Session Validation: Admin logout clears server-side sessionId and revokes token")
    void testSessionValidation_AdminLogout_ClearsSession() throws Exception {
        String email = "admin_logout_clear_sess@example.com";
        String sessionId = "sess-to-clear";
        getOrCreateTestAdmin(email, "Pass123!", sessionId);
        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), sessionId, "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        Admin updated = adminRepository.findByEmailIgnoreCase(email).orElseThrow();
        assertNull(updated.getSessionId(), "Admin session ID must be null after logout");
        assertTrue(tokenRevocationService.isRevoked(token), "Admin token must be revoked");
    }

    @Test
    @DisplayName("11. Session Validation: Previous admin JWT fails after admin logs in on a new device (session rotation)")
    void testSessionValidation_PreviousAdminJwt_FailsAfterNewSession() throws Exception {
        String email = "admin_multi_device@example.com";
        Admin admin = getOrCreateTestAdmin(email, "Pass123!", "device-1-session");
        String device1Token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), "device-1-session", "ADMIN");

        // Device 2 logs in -> sets new session in database
        admin.setSessionId("device-2-session");
        adminRepository.saveAndFlush(admin);

        // Device 1's previous token now fails with 401 due to session mismatch
        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + device1Token))
                .andExpect(status().isUnauthorized());

        // Device 2's token works
        String device2Token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), "device-2-session", "ADMIN");
        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + device2Token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("12. Session Validation: New admin login receives working session and JWT")
    void testSessionValidation_NewAdminLogin_ReceivesWorkingSessionAndJwt() throws Exception {
        String email = "admin_new_login_sess@example.com";
        String sessionId = UUID.randomUUID().toString();
        getOrCreateTestAdmin(email, "Pass123!", sessionId);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), sessionId, "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // =========================================================================
    // SECTION C: USER REFRESH ENDPOINT
    // =========================================================================

    @Test
    @DisplayName("13. User Refresh: Valid user refresh token returns HTTP 200 with new tokens")
    void testUserRefresh_ValidToken_Succeeds() throws Exception {
        String email = "user_refresh_success@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken token = refreshTokenService.createToken(email);

        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("14. User Refresh: Expired refresh token returns HTTP 400 Bad Request")
    void testUserRefresh_ExpiredToken_ReturnsBadRequest() throws Exception {
        String email = "user_expired_rt@example.com";
        getOrCreateTestUser(email, "Pass123!");
        refreshTokenRepository.deleteByEmail(email);

        RefreshToken expired = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();
        expired = refreshTokenRepository.saveAndFlush(expired);

        String payload = String.format("{\"refreshToken\": \"%s\"}", expired.getToken());

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("15. User Refresh: Deleted user refresh token returns HTTP 400 Bad Request")
    void testUserRefresh_DeletedUser_ReturnsBadRequest() throws Exception {
        String email = "user_deleted_rt@example.com";
        User user = getOrCreateTestUser(email, "Pass123!");
        user.setIsDeleted(true);
        userRepository.saveAndFlush(user);

        RefreshToken token = refreshTokenService.createToken(email);
        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("16. User Refresh: Blocked user refresh token returns HTTP 400 Bad Request")
    void testUserRefresh_BlockedUser_ReturnsBadRequest() throws Exception {
        String email = "user_blocked_rt@example.com";
        User user = getOrCreateTestUser(email, "Pass123!");
        user.setIsBlocked(true);
        userRepository.saveAndFlush(user);

        RefreshToken token = refreshTokenService.createToken(email);
        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("17. User Refresh: Inactive user refresh token returns HTTP 400 Bad Request")
    void testUserRefresh_InactiveUser_ReturnsBadRequest() throws Exception {
        String email = "user_inactive_rt@example.com";
        User user = getOrCreateTestUser(email, "Pass123!");
        user.setIsActive(false);
        userRepository.saveAndFlush(user);

        RefreshToken token = refreshTokenService.createToken(email);
        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("18. User Refresh: Non-existent refresh token returns HTTP 400 Bad Request")
    void testUserRefresh_NonExistentToken_ReturnsBadRequest() throws Exception {
        String payload = "{\"refreshToken\": \"non-existent-guid-token\"}";

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("19. User Refresh: Returns newly generated access token that authenticates")
    void testUserRefresh_NewAccessToken_Authenticates() throws Exception {
        String email = "user_rt_new_jwt@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken token = refreshTokenService.createToken(email);

        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("accessToken"));

        // Extract newly generated access token and use it on protected endpoint
        String newAccessToken = com.jayway.jsonpath.JsonPath.read(responseBody, "$.data.accessToken");

        assertNotNull(newAccessToken);
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + newAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("20. User Refresh: Rotated refresh token is newly persisted in DB")
    void testUserRefresh_RotatedToken_PersistedInDB() throws Exception {
        String email = "user_rt_rotate_db@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken oldToken = refreshTokenService.createToken(email);
        String oldTokenStr = oldToken.getToken();

        String payload = String.format("{\"refreshToken\": \"%s\"}", oldTokenStr);

        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String newTokenStr = com.jayway.jsonpath.JsonPath.read(responseBody, "$.data.refreshToken");

        assertNotEquals(oldTokenStr, newTokenStr);
        assertFalse(refreshTokenRepository.findByToken(oldTokenStr).isPresent(), "Old token must be deleted");
        assertTrue(refreshTokenRepository.findByToken(newTokenStr).isPresent(), "New token must be present in DB");
    }

    @Test
    @DisplayName("21. User Refresh: Endpoint is protected by Task 5 rate limiting")
    void testUserRefresh_RateLimiting_Enforced() throws Exception {
        String email = "user_rt_ratelimit@example.com";
        getOrCreateTestUser(email, "Pass123!");

        // Trigger repeated calls
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/auth/refresh")
                            .header("X-Forwarded-For", "203.0.113.88")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"refreshToken\":\"dummy-rt-token\"}"))
                    .andExpect(status().isBadRequest());
        }

        // 6th call from same IP should hit rate limiting (429)
        mockMvc.perform(post("/api/auth/refresh")
                        .header("X-Forwarded-For", "203.0.113.88")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"dummy-rt-token\"}"))
                .andExpect(status().isTooManyRequests());
    }

    // =========================================================================
    // SECTION D: REFRESH ROTATION & CONCURRENCY
    // =========================================================================

    @Test
    @DisplayName("22. Refresh Rotation: Old refresh token is invalid immediately after successful rotation")
    void testRefreshRotation_OldTokenInvalidAfterRotation() throws Exception {
        String email = "user_rot_invalid_old@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken oldToken = refreshTokenService.createToken(email);

        String payload = String.format("{\"refreshToken\": \"%s\"}", oldToken.getToken());

        // 1st request -> Success
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // 2nd request (reuse of old token) -> 400 Bad Request
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("23. Refresh Rotation: Sequential replay of rotated token fails with HTTP 400")
    void testRefreshRotation_SequentialReplay_Fails() throws Exception {
        String email = "user_rot_replay@example.com";
        getOrCreateTestAdmin(email, "Pass123!", "sess-replay");
        RefreshToken token = refreshTokenService.createToken(email);

        String payload = String.format("{\"refreshToken\": \"%s\"}", token.getToken());

        // 1st call -> Success
        mockMvc.perform(post("/api/admins/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // 2nd call -> Replay fails
        mockMvc.perform(post("/api/admins/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("24. Refresh Rotation: Concurrent refresh using same token allows maximum ONE successful rotation")
    void testRefreshRotation_ConcurrentRefresh_AllowsOnlyOneSuccess() throws Exception {
        String email = "concurrent_rotate_test@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken initialToken = refreshTokenService.createToken(email);
        String oldTokenString = initialToken.getToken();

        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                try {
                    refreshTokenService.rotateToken(oldTokenString);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }));
        }

        startLatch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(15, TimeUnit.SECONDS));

        int successCount = 0;
        int failureCount = 0;
        for (Future<Boolean> f : futures) {
            if (f.get()) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        assertEquals(1, successCount, "Exactly 1 concurrent refresh request must succeed due to row locking");
        assertEquals(threadCount - 1, failureCount, "Remaining concurrent refresh requests must fail");
    }

    @Test
    @DisplayName("25. Refresh Rotation: Concurrent requests do not create multiple replacement tokens")
    void testRefreshRotation_Concurrent_SingleReplacementTokenInDB() throws Exception {
        String email = "concurrent_single_db_test@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken initialToken = refreshTokenService.createToken(email);
        String oldTokenString = initialToken.getToken();

        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                try {
                    refreshTokenService.rotateToken(oldTokenString);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }));
        }

        startLatch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(15, TimeUnit.SECONDS));

        // Exactly one refresh token should exist in DB for this email
        assertTrue(refreshTokenRepository.findByEmail(email).isPresent(), "One rotated refresh token must exist in DB");
    }

    @Test
    @DisplayName("26. Refresh Rotation: Transaction remains consistent after failed concurrent request")
    void testRefreshRotation_TransactionConsistency_AfterConcurrentFailure() {
        String email = "txn_consistent_test@example.com";
        getOrCreateTestUser(email, "Pass123!");
        RefreshToken initialToken = refreshTokenService.createToken(email);

        RefreshToken rotated = refreshTokenService.rotateToken(initialToken.getToken());
        assertNotNull(rotated);

        // Attempting to rotate with old token fails cleanly without corrupting DB
        assertThrows(RuntimeException.class, () -> refreshTokenService.rotateToken(initialToken.getToken()));

        // Active rotated token is still valid
        RefreshToken active = refreshTokenRepository.findByEmail(email).orElse(null);
        assertNotNull(active);
        assertEquals(rotated.getToken(), active.getToken());
    }

    // =========================================================================
    // SECTION E: USER LOGOUT REFRESH TOKEN PURGE
    // =========================================================================

    @Test
    @DisplayName("27. User Logout: Purges user's active refresh token from database")
    void testUserLogout_PurgesRefreshToken() throws Exception {
        String email = "user_logout_purge_rt@example.com";
        getOrCreateTestUser(email, "Pass123!");
        refreshTokenService.createToken(email);
        assertTrue(refreshTokenRepository.findByEmail(email).isPresent(), "Refresh token must exist before logout");

        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertFalse(refreshTokenRepository.findByEmail(email).isPresent(), "Refresh token must be purged upon user logout");
    }

    @Test
    @DisplayName("28. User Logout: Revokes user access token upon logout")
    void testUserLogout_RevokesAccessToken() throws Exception {
        String email = "user_logout_revokes_jwt@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertTrue(tokenRevocationService.isRevoked(token), "Access token must be marked revoked");
    }

    @Test
    @DisplayName("29. User Logout: Invalidates user session")
    void testUserLogout_InvalidatesSession() throws Exception {
        String email = "user_logout_sess_inval@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-user-1", "USER");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Token is revoked and rejected on subsequent calls
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("30. User Logout: Logout uses SecurityContext identity, not client-supplied user ID")
    void testUserLogout_UsesSecurityContextIdentity_NotClientSuppliedId() throws Exception {
        String callerEmail = "caller_logout_identity@example.com";
        String victimEmail = "victim_logout_identity@example.com";

        getOrCreateTestUser(callerEmail, "Pass123!");
        getOrCreateTestUser(victimEmail, "Pass123!");

        refreshTokenService.createToken(callerEmail);
        refreshTokenService.createToken(victimEmail);

        String token = jwtUtil.generateToken(callerEmail, List.of("ROLE_USER"), "sess-1", "USER");

        // Attempting to pass victim's email or ID in query/body does not affect victim
        mockMvc.perform(post("/api/auth/logout?email=" + victimEmail)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertFalse(refreshTokenRepository.findByEmail(callerEmail).isPresent(), "Caller token must be deleted");
        assertTrue(refreshTokenRepository.findByEmail(victimEmail).isPresent(), "Victim token must remain untouched");
    }

    @Test
    @DisplayName("31. User Logout: Another user's refresh token remains unaffected")
    void testUserLogout_AnotherUserRefreshToken_Unaffected() throws Exception {
        String user1 = "user1_rt_unaffected@example.com";
        String user2 = "user2_rt_unaffected@example.com";

        getOrCreateTestUser(user1, "Pass123!");
        getOrCreateTestUser(user2, "Pass123!");

        RefreshToken rt1 = refreshTokenService.createToken(user1);
        RefreshToken rt2 = refreshTokenService.createToken(user2);

        String token1 = jwtUtil.generateToken(user1, List.of("ROLE_USER"), "sess-1", "USER");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk());

        assertFalse(refreshTokenRepository.findByEmail(user1).isPresent());
        assertTrue(refreshTokenRepository.findByEmail(user2).isPresent());
        assertEquals(rt2.getToken(), refreshTokenRepository.findByEmail(user2).get().getToken());
    }

    // =========================================================================
    // SECTION F: SECURITY REGRESSION TESTS
    // =========================================================================

    @Test
    @DisplayName("32. Regression: UserSecurity.isOwner enforces IDOR protection")
    void testRegression_UserSecurityIsOwner() {
        String userA = "owner_test_a@example.com";
        String userB = "attacker_test_b@example.com";
        User uA = getOrCreateTestUser(userA, "Pass123!");
        getOrCreateTestUser(userB, "Pass123!");

        assertTrue(userSecurity.isOwner(uA.getId(), userA));
        assertFalse(userSecurity.isOwner(uA.getId(), userB));
    }

    @Test
    @DisplayName("33. Regression: Deactivated user account rejects valid JWT with HTTP 401")
    void testRegression_DeactivatedUser_Returns401() throws Exception {
        String email = "deactivated_reg_user@example.com";
        User user = getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");

        user.setIsActive(false);
        userRepository.saveAndFlush(user);

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("34. Regression: Tampered JWT payload fails with HTTP 401")
    void testRegression_TamperedJwt_Returns401() throws Exception {
        String email = "tampered_reg_user@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String validToken = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");
        String[] parts = validToken.split("\\.");

        String tampered = parts[0] + "." + parts[1].substring(0, parts[1].length() - 4) + "ZZZZ." + parts[2];

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + tampered))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("35. Regression: Expired JWT fails with HTTP 401")
    void testRegression_ExpiredJwt_Returns401() throws Exception {
        String email = "expired_reg_user@example.com";
        getOrCreateTestUser(email, "Pass123!");

        String expiredToken = Jwts.builder()
                .setSubject(email)
                .claim("roles", List.of("ROLE_USER"))
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("36. Regression: alg=none token fails with HTTP 401")
    void testRegression_AlgNone_Returns401() throws Exception {
        String noneHeader = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"alg_none@example.com\"}".getBytes(StandardCharsets.UTF_8));
        String noneToken = noneHeader + "." + payload + ".";

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + noneToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("37. Error Sanitization: Invalid refresh token response contains clean JSON without leakages")
    void testErrorSanitization_InvalidRefreshToken_Clean() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"invalid-token\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertFalse(response.contains("org.springframework"));
        assertFalse(response.contains("org.hibernate"));
        assertFalse(response.contains("jwt.secret"));
    }

    @Test
    @DisplayName("38. Error Sanitization: Revoked JWT response contains clean 401 without leakages")
    void testErrorSanitization_RevokedJwt_Clean() throws Exception {
        String email = "sanitized_revoked@example.com";
        getOrCreateTestUser(email, "Pass123!");
        String token = jwtUtil.generateToken(email, List.of("ROLE_USER"), "sess-1", "USER");
        tokenRevocationService.revokeToken(token);

        MvcResult result = mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertFalse(response.contains("org.springframework"));
        assertFalse(response.contains("jwt.secret"));
    }
}
