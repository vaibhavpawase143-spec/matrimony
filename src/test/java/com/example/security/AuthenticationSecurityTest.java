package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("3. Authentication Endpoints Security Regression Tests")
class AuthenticationSecurityTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @DisplayName("Valid user login credentials must return HTTP 200 with access and refresh tokens")
    void testUserLogin_Success() throws Exception {
        String email = "auth_valid_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Auth");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("ValidPass123!"));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String payload = """
                {
                    "email": "auth_valid_user@example.com",
                    "password": "ValidPass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("User login with invalid password must be rejected")
    void testUserLogin_InvalidPassword_Fails() throws Exception {
        String email = "auth_wrongpass_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Auth");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("CorrectPass123!"));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String payload = """
                {
                    "email": "auth_wrongpass_user@example.com",
                    "password": "WrongPassword999!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("User login with non-existent email must be rejected")
    void testUserLogin_NonExistentEmail_Fails() throws Exception {
        String payload = """
                {
                    "email": "completely_unknown_user_99999@example.com",
                    "password": "SomePassword123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("User login with inactive account must be rejected")
    void testUserLogin_InactiveAccount_Fails() throws Exception {
        String email = "auth_inactive_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Inactive");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Pass123!"));
        user.setIsActive(false);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String payload = """
                {
                    "email": "auth_inactive_user@example.com",
                    "password": "Pass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("User login with deleted account must be rejected")
    void testUserLogin_DeletedAccount_Fails() throws Exception {
        String email = "auth_deleted_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Deleted");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Pass123!"));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(true);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String payload = """
                {
                    "email": "auth_deleted_user@example.com",
                    "password": "Pass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("User login with unverified email must be rejected")
    void testUserLogin_UnverifiedEmail_Fails() throws Exception {
        String email = "auth_unverified_email_user@example.com";
        Role role = getOrCreateRole("ROLE_USER");

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Unverified");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Pass123!"));
        user.setIsActive(true);
        user.setEmailVerified(false);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        userRepository.save(user);

        String payload = """
                {
                    "email": "auth_unverified_email_user@example.com",
                    "password": "Pass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Admin login with valid credentials must return HTTP 200 with tokens")
    void testAdminLogin_Success() throws Exception {
        String email = "auth_valid_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Auth Admin");
        admin.setUsername("auth_valid_admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode("AdminPass123!"));
        admin.setIsActive(true);
        admin.setRole(role);
        adminRepository.save(admin);

        String payload = """
                {
                    "email": "auth_valid_admin@example.com",
                    "password": "AdminPass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("Admin login with invalid password must be rejected")
    void testAdminLogin_InvalidPassword_Fails() throws Exception {
        String email = "auth_wrongpass_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Auth Admin");
        admin.setUsername("auth_wrongpass_admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode("CorrectAdminPass123!"));
        admin.setIsActive(true);
        admin.setRole(role);
        adminRepository.save(admin);

        String payload = """
                {
                    "email": "auth_wrongpass_admin@example.com",
                    "password": "WrongPassword999!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Admin login with inactive account must be rejected")
    void testAdminLogin_InactiveAccount_Fails() throws Exception {
        String email = "auth_inactive_admin@example.com";
        Role role = getOrCreateRole("ROLE_ADMIN");

        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Inactive Admin");
        admin.setUsername("auth_inactive_admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode("AdminPass123!"));
        admin.setIsActive(false);
        admin.setRole(role);
        adminRepository.save(admin);

        String payload = """
                {
                    "email": "auth_inactive_admin@example.com",
                    "password": "AdminPass123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        mockMvc.perform(post("/api/admins/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Authentication failure error response must not leak stack traces, SQL, or internal secrets")
    void testAuthenticationError_NoInformationLeakage() throws Exception {
        String payload = """
                {
                    "email": "nonexistent_leak_test@example.com",
                    "password": "WrongPassword123!",
                    "recaptchaToken": "test_bypass"
                }
                """;

        String responseBody = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("Exception"), "Response must not contain Exception names");
        assertFalse(responseBody.contains("org.hibernate"), "Response must not contain Hibernate packages");
        assertFalse(responseBody.contains("org.postgresql"), "Response must not contain PostgreSQL driver classes");
        assertFalse(responseBody.contains("SELECT "), "Response must not contain SQL queries");
        assertFalse(responseBody.contains("password_hash"), "Response must not contain password hash keywords");
        assertFalse(responseBody.contains("jwt.secret"), "Response must not contain JWT secret configuration names");
    }
}
