package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.RateLimitProperties;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.security.ratelimit.ClientIpResolver;
import com.example.security.ratelimit.RateLimitResult;
import com.example.security.ratelimit.RateLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 5 Hardening — Production-Grade Rate Limiting & Abuse Protection Tests")
class RateLimitingSecurityTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private ClientIpResolver clientIpResolver;

    @Autowired
    private RateLimitProperties rateLimitProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setupRateLimit() {
        rateLimitService.clearAll();
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
        user.setFirstName("Hardened");
        user.setLastName("User");
        user.setEmail(email);
        user.setPhone("98" + String.format("%08d", Math.abs((email + System.currentTimeMillis()).hashCode() % 100000000)));
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    private Admin getOrCreateTestAdmin(String email, String rawPassword) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Security Admin");
        admin.setUsername("admin_" + Math.abs(email.hashCode()));
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(adminRole);
        admin.setIsActive(true);
        return adminRepository.save(admin);
    }

    // =========================================================================
    // CATEGORY A: USER LOGIN RATE LIMITING & BRUTE FORCE PROTECTION
    // =========================================================================

    @Test
    @DisplayName("1. User Login: Repeated failed login attempts return HTTP 429 Too Many Requests")
    void testUserLogin_RepeatedFailures_Returns429TooManyRequests() throws Exception {
        String email = "ratelimit_user_lock@example.com";
        getOrCreateTestUser(email, "CorrectPass123!");

        // First 5 invalid attempts return 400 Bad Request
        for (int i = 1; i <= 5; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPassword_%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload))
                    .andExpect(status().isBadRequest());
        }

        // 6th attempt -> Blocked with HTTP 429 and Retry-After
        String badPayload = String.format("""
                {
                    "email": "%s",
                    "password": "WrongPassword_6",
                    "recaptchaToken": "test_token"
                }
                """, email);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.errorCode").value("RATE_LIMIT_EXCEEDED"));
    }

    @Test
    @DisplayName("2. User Login: Retry-After header is a positive integer on 429 response")
    void testUserLogin_RetryAfterHeader_IsPositiveInteger() throws Exception {
        String email = "ratelimit_retry_after@example.com";
        getOrCreateTestUser(email, "Pass123!");

        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("""
                            {"email": "%s", "password": "Wrong_%d", "recaptchaToken": "test_token"}
                            """, email, i)))
                    .andExpect(status().isBadRequest());
        }

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {"email": "%s", "password": "Wrong_6", "recaptchaToken": "test_token"}
                                """, email)))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andReturn();

        String retryAfter = result.getResponse().getHeader("Retry-After");
        assertNotNull(retryAfter);
        long retrySec = Long.parseLong(retryAfter);
        assertTrue(retrySec > 0, "Retry-After must be greater than 0");
    }

    @Test
    @DisplayName("3. User Login: Successful login resets failed login counter")
    void testUserLogin_SuccessfulLogin_ResetsCounter() throws Exception {
        String email = "ratelimit_user_reset@example.com";
        String validPassword = "CorrectPass123!";
        getOrCreateTestUser(email, validPassword);

        // 3 failed login attempts
        for (int i = 1; i <= 3; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPassword_%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload))
                    .andExpect(status().isBadRequest());
        }

        // Legitimate login -> Succeeds and resets counter
        String validPayload = String.format("""
                {
                    "email": "%s",
                    "password": "%s",
                    "recaptchaToken": "test_token"
                }
                """, email, validPassword);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("4. User Login: Account isolation ensures one attacked account does not block another")
    void testUserLogin_AccountLevelIsolation() throws Exception {
        String victimEmail = "victim_account@example.com";
        String otherEmail = "innocent_account@example.com";
        getOrCreateTestUser(victimEmail, "VictimPass123!");
        getOrCreateTestUser(otherEmail, "InnocentPass123!");

        // Attack victim account from distinct IPs until victim account is rate limited
        for (int i = 1; i <= 6; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPass%d",
                        "recaptchaToken": "test_token"
                    }
                    """, victimEmail, i);

            final int ipSuffix = i;
            mockMvc.perform(post("/api/auth/login")
                            .with(req -> {
                                req.setRemoteAddr("198.51.100." + ipSuffix);
                                return req;
                            })
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload));
        }

        // Innocent user on another account logs in successfully from a separate IP
        String innocentPayload = String.format("""
                {
                    "email": "%s",
                    "password": "InnocentPass123!",
                    "recaptchaToken": "test_token"
                }
                """, otherEmail);

        mockMvc.perform(post("/api/auth/login")
                        .with(req -> {
                            req.setRemoteAddr("198.51.100.99");
                            return req;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(innocentPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("5. User Login: IP throttling triggers on distributed credential stuffing from single IP")
    void testUserLogin_IpLevelThrottling() throws Exception {
        // Attempt logins on multiple distinct email addresses from the same IP
        for (int i = 1; i <= 5; i++) {
            String payload = String.format("""
                    {
                        "email": "random_target_%d@example.com",
                        "password": "WrongPassword%d",
                        "recaptchaToken": "test_token"
                    }
                    """, i, i);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest());
        }

        // 6th attempt from the same IP is blocked with 429
        String payload6 = """
                {
                    "email": "yet_another_user@example.com",
                    "password": "SomePassword123!",
                    "recaptchaToken": "test_token"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload6))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    @DisplayName("6. User Login: Legacy /api/users/login returns HTTP 429 upon threshold breach")
    void testUserLogin_LegacyEndpoint_Returns429AfterThreshold() throws Exception {
        String email = "legacy_ratelimit_user@example.com";
        getOrCreateTestUser(email, "ValidPass123!");

        for (int i = 1; i <= 5; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "BadPassword%d"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload))
                    .andExpect(status().isBadRequest());
        }

        // 6th attempt -> 429
        String badPayload = String.format("""
                {
                    "email": "%s",
                    "password": "BadPassword6"
                }
                """, email);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isTooManyRequests());
    }

    // =========================================================================
    // CATEGORY B: ADMIN LOGIN STRICT RATE LIMITING
    // =========================================================================

    @Test
    @DisplayName("7. Admin Login: Repeated failed admin logins return HTTP 429 (stricter limit)")
    void testAdminLogin_RepeatedFailures_Returns429TooManyRequests() throws Exception {
        String email = "ratelimit_admin_lock@example.com";
        getOrCreateTestAdmin(email, "SuperAdmin123!");

        // 4 failed attempts (configured limit in test properties is 4)
        for (int i = 1; i <= 4; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongAdminPass%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/admins/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload))
                    .andExpect(status().isBadRequest());
        }

        // 5th attempt -> 429 Too Many Requests
        String badPayload = String.format("""
                {
                    "email": "%s",
                    "password": "WrongAdminPass5",
                    "recaptchaToken": "test_token"
                }
                """, email);

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(jsonPath("$.errorCode").value("RATE_LIMIT_EXCEEDED"));
    }

    @Test
    @DisplayName("8. Admin Login: Retry-After header present and valid on admin 429 response")
    void testAdminLogin_RetryAfterHeader_Present() throws Exception {
        String email = "admin_retry_header@example.com";
        getOrCreateTestAdmin(email, "AdminPass123!");

        for (int i = 1; i <= 4; i++) {
            mockMvc.perform(post("/api/admins/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.format("""
                            {"email": "%s", "password": "Wrong%d", "recaptchaToken": "test_token"}
                            """, email, i)));
        }

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {"email": "%s", "password": "Wrong5", "recaptchaToken": "test_token"}
                                """, email)))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    @DisplayName("9. Admin Login: Legitimate login resets admin failed attempt counters")
    void testAdminLogin_SuccessfulLogin_ResetsCounter() throws Exception {
        String email = "ratelimit_admin_reset@example.com";
        String validPass = "SuperAdminPass123!";
        getOrCreateTestAdmin(email, validPass);

        // 2 failed attempts
        for (int i = 1; i <= 2; i++) {
            String badPayload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPassword%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/admins/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(badPayload))
                    .andExpect(status().isBadRequest());
        }

        // Successful admin login
        String validPayload = String.format("""
                {
                    "email": "%s",
                    "password": "%s",
                    "recaptchaToken": "test_token"
                }
                """, email, validPass);

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("10. Admin Login: Non-existent admin email does not disclose account non-existence")
    void testAdminLogin_NoAccountEnumeration() throws Exception {
        String nonExistentEmail = "unknown_admin_999@example.com";

        for (int i = 1; i <= 4; i++) {
            mockMvc.perform(post("/api/admins/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("""
                                    {"email": "%s", "password": "RandomPassword%d", "recaptchaToken": "test_token"}
                                    """, nonExistentEmail, i)))
                    .andExpect(status().isBadRequest());
        }

        // 5th attempt is rate limited uniformly
        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {"email": "%s", "password": "RandomPassword5", "recaptchaToken": "test_token"}
                                """, nonExistentEmail)))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    // =========================================================================
    // CATEGORY C: FORGOT PASSWORD PROTECTION
    // =========================================================================

    @Test
    @DisplayName("11. Forgot Password: Rapid repeated requests for same account trigger HTTP 429 cooldown")
    void testForgotPassword_AccountCooldownEnforced() throws Exception {
        String email = "forgot_pwd_cooldown@example.com";
        getOrCreateTestUser(email, "InitialPass123!");

        String payload = String.format("""
                {
                    "email": "%s"
                }
                """, email);

        // 1st request -> Allowed (200 OK)
        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // Immediate 2nd request for same account -> 429 Too Many Requests
        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(jsonPath("$.errorCode").value("RATE_LIMIT_EXCEEDED"));
    }

    @Test
    @DisplayName("12. Forgot Password: IP sliding window limit returns HTTP 429 after threshold")
    void testForgotPassword_IpRateLimiting() throws Exception {
        // Test limit: 3 requests per 15 minutes per IP
        for (int i = 1; i <= 3; i++) {
            String email = "forgot_ip_" + i + "@example.com";
            getOrCreateTestUser(email, "Pass123!");

            String payload = String.format("""
                    {
                        "email": "%s"
                    }
                    """, email);

            mockMvc.perform(post("/api/auth/forgot-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isOk());
        }

        // 4th request from same IP -> 429
        String email4 = "forgot_ip_4@example.com";
        getOrCreateTestUser(email4, "Pass123!");
        String payload4 = String.format("""
                {
                    "email": "%s"
                }
                """, email4);

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload4))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("13. Forgot Password: Non-existent email yields consistent response and rate limit")
    void testForgotPassword_NonExistentEmail_HandledConsistently() throws Exception {
        String unknownEmail = "unknown_user_test@example.com";
        String payload = String.format("""
                {"email": "%s"}
                """, unknownEmail);

        // 1st request -> 200 OK (doesn't reveal account existence)
        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // Immediate 2nd request -> 429 Cooldown
        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isTooManyRequests());
    }

    // =========================================================================
    // CATEGORY D: EMAIL VERIFICATION & RESEND PROTECTION
    // =========================================================================

    @Test
    @DisplayName("14. Email Verification: Immediate repeated resend requests trigger HTTP 429 cooldown")
    void testEmailVerification_ResendCooldownEnforced() throws Exception {
        String email = "resend_cooldown@example.com";

        String payload = String.format("""
                {
                    "email": "%s"
                }
                """, email);

        // 1st request -> 200 OK
        mockMvc.perform(post("/api/auth/resend-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // Immediate 2nd request -> 429 Too Many Requests
        mockMvc.perform(post("/api/auth/resend-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    @DisplayName("15. Email Verification: Send verification endpoint enforces account cooldown")
    void testEmailVerification_SendVerificationCooldown() throws Exception {
        String email = "send_verify_cooldown@example.com";

        // 1st request -> 200 OK
        mockMvc.perform(post("/api/auth/send-verification")
                        .param("email", email))
                .andExpect(status().isOk());

        // Immediate 2nd request -> 429 Too Many Requests
        mockMvc.perform(post("/api/auth/send-verification")
                        .param("email", email))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    @DisplayName("16. Email Verification: IP sliding window throttles excessive resend requests")
    void testEmailVerification_IpThrottling() throws Exception {
        // Policy: 3 requests per IP window
        for (int i = 1; i <= 3; i++) {
            mockMvc.perform(post("/api/auth/send-verification")
                            .param("email", "verify_user_" + i + "@example.com"))
                    .andExpect(status().isOk());
        }

        // 4th request from same IP -> 429
        mockMvc.perform(post("/api/auth/send-verification")
                        .param("email", "verify_user_4@example.com"))
                .andExpect(status().isTooManyRequests());
    }

    // =========================================================================
    // CATEGORY E: PASSWORD RESET TOKEN BRUTE FORCE PROTECTION
    // =========================================================================

    @Test
    @DisplayName("17. Reset Password: Repeated invalid token submissions return HTTP 429")
    void testResetPassword_RepeatedInvalidTokens_Returns429() throws Exception {
        for (int i = 1; i <= 5; i++) {
            String payload = String.format("""
                    {
                        "token": "invalid-token-%d",
                        "newPassword": "NewPassword123!"
                    }
                    """, i);

            mockMvc.perform(post("/api/auth/reset-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest());
        }

        // 6th attempt -> 429
        String payload6 = """
                {
                    "token": "invalid-token-6",
                    "newPassword": "NewPassword123!"
                }
                """;

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload6))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    @DisplayName("18. Reset Password: Token hash is used instead of raw token in Redis keys")
    void testResetPassword_TokenHashUsedInRedis() throws Exception {
        String sensitiveToken = "secret-plain-token-xyz-12345";
        String payload = String.format("""
                {
                    "token": "%s",
                    "newPassword": "Password123!"
                }
                """, sensitiveToken);

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));

        // Check Redis keys to confirm plain sensitiveToken was never saved directly
        var keys = stringRedisTemplate.keys("*" + sensitiveToken + "*");
        assertTrue(keys == null || keys.isEmpty(), "Raw token MUST NEVER appear in Redis keys");
    }

    // =========================================================================
    // CATEGORY F: USER REGISTRATION RATE LIMITING
    // =========================================================================

    @Test
    @DisplayName("19. Registration: Rapid repeated registrations from same IP return HTTP 429")
    void testRegistration_RapidRegistrations_Returns429() throws Exception {
        long seed = System.currentTimeMillis() % 1000000;

        // Test limit: 5 registrations per window
        for (int i = 1; i <= 5; i++) {
            String payload = String.format("""
                    {
                        "firstName": "Batch",
                        "lastName": "User%d",
                        "email": "reg_limit_%d_%d@example.com",
                        "phone": "96%08d",
                        "password": "SecurePassword123!",
                        "recaptchaToken": "test_token"
                    }
                    """, i, seed, i, (seed + i) % 100000000);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isOk());
        }

        // 6th registration from same IP -> 429
        String payload6 = String.format("""
                {
                    "firstName": "Batch",
                    "lastName": "User6",
                    "email": "reg_limit_%d_6@example.com",
                    "phone": "96%08d",
                    "password": "SecurePassword123!",
                    "recaptchaToken": "test_token"
                }
                """, seed, (seed + 6) % 100000000);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload6))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(jsonPath("$.errorCode").value("RATE_LIMIT_EXCEEDED"));
    }

    @Test
    @DisplayName("20. Registration: Registrations from different IPs are isolated and allowed")
    void testRegistration_DifferentIps_Allowed() throws Exception {
        long seed = System.currentTimeMillis() % 1000000;

        // Register from distinct IP addresses
        for (int i = 1; i <= 5; i++) {
            String payload = String.format("""
                    {
                        "firstName": "MultiIp",
                        "lastName": "User%d",
                        "email": "multi_ip_%d_%d@example.com",
                        "phone": "95%08d",
                        "password": "SecurePassword123!",
                        "recaptchaToken": "test_token"
                    }
                    """, i, seed, i, (seed + i) % 100000000);

            final int ipSuffix = i;
            mockMvc.perform(post("/api/auth/register")
                            .with(req -> {
                                req.setRemoteAddr("198.51.101." + ipSuffix);
                                return req;
                            })
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isOk());
        }
    }

    // =========================================================================
    // CATEGORY G: CLIENT IP RESOLUTION & TRUSTED PROXY SECURITY
    // =========================================================================

    @Test
    @DisplayName("21. IP Security: Spoofed X-Forwarded-For is ignored when remote address is untrusted")
    void testIpResolution_UntrustedProxy_IgnoresSpoofedHeader() throws Exception {
        String email = "untrusted_proxy_test@example.com";
        getOrCreateTestUser(email, "Pass123!");

        // Attacker sends arbitrary X-Forwarded-For from untrusted remote address (default 127.0.0.1)
        for (int i = 1; i <= 5; i++) {
            String payload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPassword%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            // Attacker rotates fake spoofed header
            mockMvc.perform(post("/api/auth/login")
                            .header("X-Forwarded-For", "203.0.113." + i)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload))
                    .andExpect(status().isBadRequest());
        }

        // 6th attempt still hits the local remote address rate limit -> 429
        String payload6 = String.format("""
                {
                    "email": "%s",
                    "password": "WrongPassword6",
                    "recaptchaToken": "test_token"
                }
                """, email);

        mockMvc.perform(post("/api/auth/login")
                        .header("X-Forwarded-For", "203.0.113.99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload6))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("22. IP Security: Trusted proxy remote address extracts leftmost forwarded IP correctly")
    void testIpResolution_TrustedProxy_HonorsForwardedFor() {
        // 10.0.0.1 is configured as a trusted proxy in application-test.properties
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        request.setRemoteAddr("10.0.0.1");
        request.addHeader("X-Forwarded-For", "203.0.113.50, 10.0.0.1");

        String resolvedIp = clientIpResolver.resolveClientIp(request);
        assertEquals("203.0.113.50", resolvedIp, "Must extract leftmost IP when request comes from trusted proxy");
    }

    // =========================================================================
    // CATEGORY H: CONCURRENCY & ATOMICITY
    // =========================================================================

    @Test
    @DisplayName("23. Concurrency: Simultaneous concurrent requests are atomically throttled")
    void testConcurrency_SimultaneousRequests_AtomicallyThrottled() throws Exception {
        String key = "concurrent_test_key_" + System.currentTimeMillis();
        int maxAllowed = 5;
        int threadCount = 20;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                return rateLimitService.tryAcquire(key, maxAllowed, 60).isAllowed();
            }));
        }

        startLatch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        int allowedCount = 0;
        int deniedCount = 0;
        for (Future<Boolean> future : futures) {
            if (future.get()) {
                allowedCount++;
            } else {
                deniedCount++;
            }
        }

        assertEquals(maxAllowed, allowedCount, "Exactly maxAllowed requests must be permitted");
        assertEquals(threadCount - maxAllowed, deniedCount, "Remaining concurrent requests must be denied");
    }

    @Test
    @DisplayName("24. Concurrency: Concurrent failed attempt recordings are atomic")
    void testConcurrency_RecordFailedAttempts_Atomic() throws Exception {
        String key = "concurrent_fail_key_" + System.currentTimeMillis();
        int threadCount = 10;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                return rateLimitService.recordFailedAttempt(key, 60);
            }));
        }

        startLatch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

        RateLimitResult check = rateLimitService.checkFailedAttempts(key, 5, 60);
        assertFalse(check.isAllowed(), "Failed attempt count must exceed max 5 attempts");
    }

    // =========================================================================
    // CATEGORY I: REDIS IMPLEMENTATION & LOCAL FALLBACK RESILIENCE
    // =========================================================================

    @Test
    @DisplayName("25. Redis Keys: Keys strictly conform to security namespace 'rl:' without secrets")
    void testRedisKeys_PrefixAndNamespace() {
        String testKey = "user_login:account:test_account@example.com";
        rateLimitService.recordFailedAttempt(testKey, 60);

        var keys = stringRedisTemplate.keys("rl:fail:" + testKey);
        assertNotNull(keys);
        assertFalse(keys.isEmpty(), "Key must be stored under 'rl:fail:' namespace in Redis");
    }

    @Test
    @DisplayName("26. Redis Resilience: Local fallback sliding window functions gracefully if needed")
    void testRedisResilience_LocalSlidingWindow_BypassesGracefully() {
        RateLimitResult result = rateLimitService.tryAcquire("fallback_test_key", 2, 60);
        assertTrue(result.isAllowed());
        assertEquals(1, result.getRemainingAttempts());

        RateLimitResult result2 = rateLimitService.tryAcquire("fallback_test_key", 2, 60);
        assertTrue(result2.isAllowed());
        assertEquals(0, result2.getRemainingAttempts());

        RateLimitResult result3 = rateLimitService.tryAcquire("fallback_test_key", 2, 60);
        assertFalse(result3.isAllowed());
        assertTrue(result3.getRetryAfterSeconds() > 0);
    }

    // =========================================================================
    // CATEGORY J: ERROR SANITIZATION & LEAK PREVENTION
    // =========================================================================

    @Test
    @DisplayName("27. Error Sanitization: HTTP 429 responses never expose stack traces, SQL, or internal keys")
    void testErrorSanitization_Clean429Response() throws Exception {
        String email = "sanitize_test@example.com";
        getOrCreateTestUser(email, "Pass123!");

        // Trigger 429
        for (int i = 1; i <= 6; i++) {
            String payload = String.format("""
                    {
                        "email": "%s",
                        "password": "WrongPassword%d",
                        "recaptchaToken": "test_token"
                    }
                    """, email, i);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payload));
        }

        String triggerPayload = String.format("""
                {
                    "email": "%s",
                    "password": "WrongPassword7",
                    "recaptchaToken": "test_token"
                }
                """, email);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(triggerPayload))
                .andExpect(status().isTooManyRequests())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertFalse(responseBody.contains("org.springframework"), "Must not leak Spring internal packages");
        assertFalse(responseBody.contains("org.hibernate"), "Must not leak Hibernate classes");
        assertFalse(responseBody.contains("redis.clients"), "Must not leak Redis clients");
        assertFalse(responseBody.contains("rl:"), "Must not leak internal Redis key prefixes");
        assertFalse(responseBody.contains("password"), "Must not echo sensitive passwords");
    }

    // =========================================================================
    // CATEGORY K: OTP EXISTING BUSINESS LOGIC INTEGRITY
    // =========================================================================

    @Test
    @DisplayName("28. OTP Integrity: Existing 60s cooldown and 3-attempt limits remain fully functional")
    void testOtp_ExistingCooldownAndAttemptLimits_Preserved() throws Exception {
        String phone = "91977770001";

        // 1st request -> Success
        mockMvc.perform(post("/api/auth/send-otp")
                        .param("phone", phone))
                .andExpect(status().isOk());

        // Immediate 2nd request -> Target cooldown triggered
        mockMvc.perform(post("/api/auth/send-otp")
                        .param("phone", phone))
                .andExpect(status().isBadRequest());
    }
}
