package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("6. Logout Security Regression Tests")
class LogoutSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

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
    @DisplayName("Admin authenticated logout clears sessionId and invalidates refresh token")
    void testAdminLogout_InvalidatesSessionAndRefreshToken() throws Exception {
        String email = "logout_admin_test@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Logout Admin");
        admin.setUsername("logout_admin_test");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(role);
        admin.setSessionId("active-session-guid-1234");
        adminRepository.save(admin);

        refreshTokenService.createToken(email);
        assertTrue(refreshTokenRepository.findByEmail(email).isPresent(), "Refresh token must exist prior to logout");

        String jwtToken = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_ADMIN", "active-session-guid-1234", "ADMIN");

        mockMvc.perform(post("/api/admins/logout")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logged out successfully"));

        // Verify RefreshToken was deleted
        assertFalse(refreshTokenRepository.findByEmail(email).isPresent(), "Logout must delete admin's refresh token");

        // Verify Admin sessionId was cleared
        Admin updatedAdmin = adminRepository.findByEmailIgnoreCase(email).orElseThrow();
        assertNull(updatedAdmin.getSessionId(), "Admin sessionId must be set to null upon logout");
    }

    @Test
    @DisplayName("Unauthenticated request to admin logout endpoint must return HTTP 401 Unauthorized")
    void testAdminLogout_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/api/admins/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("User authenticated logout marks user account offline")
    void testUserLogout_SetsUserOffline() throws Exception {
        String email = "logout_user_test@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Logout");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsOnline(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String jwtToken = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));

        User updatedUser = userRepository.findByEmail(email).orElseThrow();
        assertFalse(updatedUser.getIsOnline(), "User isOnline must be false after logout");
        assertNotNull(updatedUser.getLastSeen(), "User lastSeen must be set after logout");
    }

    @Test
    @DisplayName("Logout operations rely on server-side SecurityContext identity and do not affect third parties")
    void testLogout_UsesServerSideIdentity() throws Exception {
        String victimEmail = "logout_victim_user@example.com";
        refreshTokenService.createToken(victimEmail);
        assertTrue(refreshTokenRepository.findByEmail(victimEmail).isPresent());

        // Attacker performs unauthenticated logout
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());

        // Victim's refresh token must still remain intact
        assertTrue(refreshTokenRepository.findByEmail(victimEmail).isPresent(), "Third party refresh token must not be affected by unauthenticated logout");
    }
}
