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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 3 — Business Security, Ownership & Conflict Handling Tests")
class BusinessSecurityAndOwnershipTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserSecurity userSecurity;

    private Role getOrCreateUserRole() {
        return roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setIsActive(true);
            return roleRepository.save(r);
        });
    }

    private String getAdminToken() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            r.setIsActive(true);
            return roleRepository.save(r);
        });

        String email = "biz_admin@example.com";
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Biz Admin");
        admin.setUsername("biz_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(adminRole);
        admin.setSessionId("sess-biz-admin");
        adminRepository.save(admin);

        return jwtUtil.generateToken(
                email,
                List.of("ROLE_ADMIN", "ADMIN_CREATE", "ADMIN_VIEW"),
                "sess-biz-admin",
                "ADMIN"
        );
    }

    // =========================================================================
    // G. RESOURCE OWNERSHIP / IDOR VALIDATION
    // =========================================================================

    @Test
    @DisplayName("Ownership: Server-side UserSecurity evaluates ownership against authenticated identity")
    void testOwnership_UserSecurityValidatesPrincipal() {
        Role userRole = getOrCreateUserRole();

        User victim = userRepository.findByEmail("biz_victim@example.com").orElseGet(User::new);
        victim.setFirstName("Victim");
        victim.setLastName("User");
        victim.setEmail("biz_victim@example.com");
        victim.setPassword("password123");
        victim.setIsActive(true);
        victim.setIsBlocked(false);
        victim.setIsDeleted(false);
        victim.setRoles(Set.of(userRole));
        victim = userRepository.save(victim);

        User attacker = userRepository.findByEmail("biz_attacker@example.com").orElseGet(User::new);
        attacker.setFirstName("Attacker");
        attacker.setLastName("User");
        attacker.setEmail("biz_attacker@example.com");
        attacker.setPassword("password123");
        attacker.setIsActive(true);
        attacker.setIsBlocked(false);
        attacker.setIsDeleted(false);
        attacker.setRoles(Set.of(userRole));
        attacker = userRepository.save(attacker);

        // Attacker attempting to claim ownership of victim's resource must fail
        boolean isOwner = userSecurity.isOwner(victim.getId(), "biz_attacker@example.com");
        assertFalse(isOwner, "UserSecurity must return false when authenticated email does not own entity ID");

        // Legit owner must pass
        boolean isLegit = userSecurity.isOwner(victim.getId(), "biz_victim@example.com");
        assertTrue(isLegit, "UserSecurity must return true when authenticated email owns entity ID");
    }

    // =========================================================================
    // H. CONFLICT / DUPLICATE HANDLING
    // =========================================================================

    @Test
    @DisplayName("Conflict: Registering with an already existing email returns error")
    void testRegister_DuplicateEmail_ReturnsError() throws Exception {
        getOrCreateUserRole();
        String existingEmail = "biz_existing_email@example.com";

        User existingUser = userRepository.findByEmail(existingEmail).orElseGet(User::new);
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setEmail(existingEmail);
        existingUser.setPhone("9876543220");
        existingUser.setPassword("password123");
        existingUser.setIsActive(true);
        userRepository.save(existingUser);

        String duplicatePayload = """
                {
                    "firstName": "New",
                    "lastName": "Duplicate",
                    "email": "biz_existing_email@example.com",
                    "phone": "9876543221",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicatePayload))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Conflict: Registering with an already existing phone number returns error")
    void testRegister_DuplicatePhone_ReturnsError() throws Exception {
        getOrCreateUserRole();
        String existingPhone = "9876543230";

        User existingUser = userRepository.findByEmail("biz_phone_user@example.com").orElseGet(User::new);
        existingUser.setFirstName("Phone");
        existingUser.setLastName("User");
        existingUser.setEmail("biz_phone_user@example.com");
        existingUser.setPhone(existingPhone);
        existingUser.setPassword("password123");
        existingUser.setIsActive(true);
        userRepository.save(existingUser);

        String duplicatePhonePayload = """
                {
                    "firstName": "New",
                    "lastName": "PhoneUser",
                    "email": "biz_new_email_diff@example.com",
                    "phone": "9876543230",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicatePhonePayload))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Conflict: Admin creation with duplicate username returns error")
    void testAdminCreate_DuplicateUsername_ReturnsError() throws Exception {
        String token = getAdminToken();

        String payload1 = """
                {
                    "name": "Admin First",
                    "username": "duplicate_admin_user",
                    "email": "first_dup_admin@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/admins")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload1));

        String payload2 = """
                {
                    "name": "Admin Second",
                    "username": "duplicate_admin_user",
                    "email": "second_dup_admin@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/admins")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload2))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false));
    }

    // =========================================================================
    // J. HTTP METHOD RESTRICTIONS (405 METHOD NOT ALLOWED)
    // =========================================================================

    @Test
    @DisplayName("HTTP Method: Unsupported DELETE on /api/auth/register returns HTTP 405")
    void testRegister_UnsupportedDeleteMethod_Returns405() throws Exception {
        mockMvc.perform(delete("/api/auth/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    @DisplayName("HTTP Method: Unsupported PUT on /api/auth/login returns HTTP 405")
    void testLogin_UnsupportedPutMethod_Returns405() throws Exception {
        mockMvc.perform(put("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    @DisplayName("HTTP Method: Unsupported PATCH on /api/admins/login returns HTTP 405")
    void testAdminLogin_UnsupportedPatchMethod_Returns405() throws Exception {
        mockMvc.perform(patch("/api/admins/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }
}
