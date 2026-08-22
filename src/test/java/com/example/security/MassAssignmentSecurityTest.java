package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 3 — Mass Assignment & Privilege Escalation Prevention Security Tests")
class MassAssignmentSecurityTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Role getOrCreateUserRole() {
        return roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setIsActive(true);
            return roleRepository.save(r);
        });
    }

    @Test
    @DisplayName("Mass Assignment: Passing admin roles in user registration must NOT grant admin privileges")
    void testRegister_RoleInjection_DoesNotGrantAdminRole() throws Exception {
        getOrCreateUserRole();
        String email = "mass_assign_role@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        String payloadWithAdminRole = """
                {
                    "firstName": "Attacker",
                    "lastName": "RoleEscalate",
                    "email": "mass_assign_role@example.com",
                    "phone": "9876543213",
                    "password": "password123",
                    "recaptchaToken": "valid-token",
                    "roles": ["ROLE_ADMIN", "ROLE_SUPER_ADMIN"],
                    "role": "ROLE_SUPER_ADMIN"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadWithAdminRole))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        transactionTemplate.execute(status -> {
            User createdUser = userRepository.findByEmail(email).orElseThrow();
            // User must NOT have ROLE_ADMIN or ROLE_SUPER_ADMIN
            for (Role role : createdUser.getRoles()) {
                assertNotEquals("ROLE_ADMIN", role.getName(), "User must not be assigned ROLE_ADMIN via mass assignment");
                assertNotEquals("ROLE_SUPER_ADMIN", role.getName(), "User must not be assigned ROLE_SUPER_ADMIN via mass assignment");
            }
            return null;
        });
    }

    @Test
    @DisplayName("Mass Assignment: Passing emailVerified=true in registration must NOT bypass email verification")
    void testRegister_EmailVerifiedInjection_IgnoredByServer() throws Exception {
        getOrCreateUserRole();
        String email = "mass_assign_verify@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        String payloadWithVerification = """
                {
                    "firstName": "Attacker",
                    "lastName": "VerifyBypass",
                    "email": "mass_assign_verify@example.com",
                    "phone": "9876543214",
                    "password": "password123",
                    "recaptchaToken": "valid-token",
                    "emailVerified": true,
                    "phoneVerified": true,
                    "isActive": true
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadWithVerification))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        User createdUser = userRepository.findByEmail(email).orElseThrow();

        // Server logic must keep email_verified as false until actual OTP/token verification
        assertFalse(Boolean.TRUE.equals(createdUser.getEmailVerified()), "Client must not be able to set emailVerified=true via mass assignment");
    }

    @Test
    @DisplayName("Mass Assignment: Passing custom ID in registration must NOT hijack existing entity ID")
    void testRegister_CustomIdInjection_IgnoredByServer() throws Exception {
        getOrCreateUserRole();
        String email = "mass_assign_id@example.com";
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        String payloadWithCustomId = """
                {
                    "id": 999999,
                    "firstName": "Attacker",
                    "lastName": "IdHijack",
                    "email": "mass_assign_id@example.com",
                    "phone": "9876543215",
                    "password": "password123",
                    "recaptchaToken": "valid-token"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadWithCustomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        User createdUser = userRepository.findByEmail(email).orElseThrow();
        assertNotEquals(999999L, createdUser.getId(), "Server must generate entity ID rather than trusting client-supplied ID");
    }
}
