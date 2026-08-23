package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("2. JWT Filter & Security Filter Chain Regression Tests")
class JwtFilterTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final String testSecret = "TestSecretKeyForAntigravityMatrimonyTestingProfile1234567890!";

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(roleName);
                    r.setIsActive(true);
                    return roleRepository.save(r);
                });
    }

    @Test
    @DisplayName("Request with no Authorization header to protected endpoint must return 401 Unauthorized")
    void testNoAuthorizationHeader_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with malformed Authorization header prefix (e.g. Basic) must return 401 Unauthorized")
    void testMalformedAuthorizationHeader_InvalidPrefix_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQ="))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with empty Bearer token must return 401 Unauthorized")
    void testEmptyBearerToken_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with unparseable garbage Bearer token must return 401 Unauthorized")
    void testGarbageBearerToken_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer invalid.garbage.jwt.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with expired Bearer token must return 401 Unauthorized")
    void testExpiredBearerToken_Returns401() throws Exception {
        String username = "filter_expired@example.com";
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_USER"))
                .claim("sessionId", "sess")
                .claim("accountType", "USER")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with wrong signature Bearer token must return 401 Unauthorized")
    void testWrongSignatureBearerToken_Returns401() throws Exception {
        String wrongSecret = "DifferentSecretKeyForTestingSignatureValidation999!";
        String tamperedToken = Jwts.builder()
                .setSubject("filter_tampered@example.com")
                .claim("roles", List.of("ROLE_USER"))
                .claim("sessionId", "sess")
                .claim("accountType", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(wrongSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + tamperedToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bearer token for nonexistent account must return 401 Unauthorized")
    void testNonexistentAccountToken_Returns401() throws Exception {
        String token = TestSecurityUtils.generateTestToken(jwtUtil, "nonexistent_account_xyz@example.com", "ROLE_USER", "USER");

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bearer token for inactive user account must return 401 Unauthorized")
    void testInactiveUserAccountToken_Returns401() throws Exception {
        String email = "filter_inactive_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Inactive");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(false);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bearer token for blocked user account must return 401 Unauthorized")
    void testBlockedUserAccountToken_Returns401() throws Exception {
        String email = "filter_blocked_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Blocked");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(true);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bearer token for deleted user account must return 401 Unauthorized")
    void testDeletedUserAccountToken_Returns401() throws Exception {
        String email = "filter_deleted_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Deleted");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(true);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(get("/api/users/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bearer token for inactive admin account must return 401 Unauthorized")
    void testInactiveAdminAccountToken_Returns401() throws Exception {
        String email = "filter_inactive_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Inactive Admin");
        admin.setUsername("filter_inactive_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(false);
        admin.setRole(role);
        adminRepository.save(admin);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_ADMIN", "ADMIN");

        mockMvc.perform(get("/api/admins/statistics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
