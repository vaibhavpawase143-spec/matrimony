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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("5. Refresh Token Security & Rotation Regression Tests")
class RefreshTokenSecurityTest extends BaseIntegrationTest {

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
    @DisplayName("Valid user creation generates valid refresh token with future expiration")
    void testCreateToken_Success() {
        String email = "rt_valid_user@example.com";
        RefreshToken token = refreshTokenService.createToken(email);

        assertNotNull(token, "RefreshToken must not be null");
        assertNotNull(token.getToken(), "Token string must not be null");
        assertEquals(email, token.getEmail(), "Token email must match requested email");
        assertTrue(token.getExpiryDate().isAfter(Instant.now()), "Expiry date must be in the future");
    }

    @Test
    @DisplayName("Verification of valid refresh token succeeds")
    void testVerifyToken_ValidToken() {
        String email = "rt_verify_user@example.com";
        RefreshToken token = refreshTokenService.createToken(email);

        RefreshToken verifiedToken = refreshTokenService.verifyToken(token.getToken());
        assertNotNull(verifiedToken);
        assertEquals(token.getToken(), verifiedToken.getToken());
    }

    @Test
    @DisplayName("Verification of non-existent refresh token must throw Exception")
    void testVerifyToken_NonExistentToken() {
        String randomToken = UUID.randomUUID().toString();
        Exception ex = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyToken(randomToken)
        );
        assertTrue(ex.getMessage().contains("Invalid refresh token"));
    }

    @Test
    @DisplayName("Expired refresh token must be deleted from repository and rejected")
    void testVerifyToken_ExpiredToken() {
        String email = "rt_expired_user@example.com";
        RefreshToken expiredToken = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();
        expiredToken = refreshTokenRepository.saveAndFlush(expiredToken);

        String tokenString = expiredToken.getToken();

        Exception ex = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyToken(tokenString)
        );
        assertTrue(ex.getMessage().contains("expired"));
        assertFalse(refreshTokenRepository.findByToken(tokenString).isPresent(), "Expired refresh token must be deleted from repository");
    }

    @Test
    @DisplayName("Refresh token for inactive user account must be deleted and rejected")
    void testVerifyToken_InactiveUserAccount() {
        String email = "rt_inactive_user@example.com";

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Inactive");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(false);
        user.setIsDeleted(false);
        userRepository.save(user);

        RefreshToken token = refreshTokenService.createToken(email);
        String tokenStr = token.getToken();

        Exception ex = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyToken(tokenStr)
        );
        assertTrue(ex.getMessage().contains("deactivated or deleted"));
        assertFalse(refreshTokenRepository.findByToken(tokenStr).isPresent(), "Token must be deleted for inactive account");
    }

    @Test
    @DisplayName("Refresh token for deleted user account must be deleted and rejected")
    void testVerifyToken_DeletedUserAccount() {
        String email = "rt_deleted_user@example.com";

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Deleted");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsDeleted(true);
        userRepository.save(user);

        RefreshToken token = refreshTokenService.createToken(email);
        String tokenStr = token.getToken();

        Exception ex = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyToken(tokenStr)
        );
        assertTrue(ex.getMessage().contains("deactivated or deleted"));
        assertFalse(refreshTokenRepository.findByToken(tokenStr).isPresent(), "Token must be deleted for deleted account");
    }

    @Test
    @DisplayName("Refresh token for inactive admin account must be deleted and rejected")
    void testVerifyToken_InactiveAdminAccount() {
        String email = "rt_inactive_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Inactive Admin");
        admin.setUsername("rt_inactive_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(false);
        admin.setRole(role);
        adminRepository.save(admin);

        RefreshToken token = refreshTokenService.createToken(email);
        String tokenStr = token.getToken();

        Exception ex = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyToken(tokenStr)
        );
        assertTrue(ex.getMessage().contains("deactivated"));
        assertFalse(refreshTokenRepository.findByToken(tokenStr).isPresent(), "Token must be deleted for inactive admin");
    }

    @Test
    @DisplayName("deleteByEmail must delete existing refresh token from repository")
    void testDeleteByEmail() {
        String email = "rt_delete_me@example.com";
        refreshTokenService.createToken(email);

        assertTrue(refreshTokenRepository.findByEmail(email).isPresent());

        refreshTokenService.deleteByEmail(email);

        assertFalse(refreshTokenRepository.findByEmail(email).isPresent());
    }

    @Test
    @DisplayName("Admin refresh token rotation: old refresh token is revoked upon generating new access token")
    void testAdminRefreshToken_Rotation() throws Exception {
        String email = "rt_rotation_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Rotation Admin");
        admin.setUsername("rt_rotation_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(role);
        admin.setSessionId("sess-rot-1");
        adminRepository.save(admin);

        RefreshToken initialToken = refreshTokenService.createToken(email);
        String oldTokenString = initialToken.getToken();

        String payload = """
                {
                    "refreshToken": "%s"
                }
                """.formatted(oldTokenString);

        // 1st call: successfully refreshes token
        mockMvc.perform(post("/api/admins/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isNotEmpty());

        // 2nd call: old refresh token reuse must fail because it was rotated and deleted
        mockMvc.perform(post("/api/admins/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
