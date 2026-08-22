package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.config.TestSecurityUtils;
import com.example.model.Admin;
import com.example.model.Permission;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("4. Authorization, Roles & Permissions Security Regression Tests")
class AuthorizationSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

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

    private Permission getOrCreatePermission(String name, String code, boolean isActive) {
        return transactionTemplate.execute(status -> {
            List<Permission> existing = entityManager.createQuery(
                    "SELECT p FROM Permission p WHERE p.code = :code", Permission.class)
                    .setParameter("code", code)
                    .getResultList();

            if (!existing.isEmpty()) {
                Permission p = existing.get(0);
                p.setIsActive(isActive);
                return entityManager.merge(p);
            }

            Permission p = Permission.builder()
                    .name(name)
                    .code(code)
                    .isActive(isActive)
                    .build();
            entityManager.persist(p);
            entityManager.flush();
            return p;
        });
    }

    private Role getOrCreateRoleWithPermissions(String roleName, Set<Permission> permissions) {
        return transactionTemplate.execute(status -> {
            Role role = roleRepository.findByName(roleName).orElseGet(() -> {
                Role r = new Role();
                r.setName(roleName);
                r.setIsActive(true);
                return r;
            });
            role.setPermissions(new HashSet<>(permissions));
            return roleRepository.saveAndFlush(role);
        });
    }

    @Test
    @DisplayName("USER role attempting to access ADMIN endpoint must return HTTP 403 Forbidden")
    void testUserRole_CannotAccessAdminEndpoint_Returns403() throws Exception {
        String email = "authz_user_denied@example.com";
        Role userRole = getOrCreateUserRole();

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Regular");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(get("/api/admins/statistics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("USER role attempting to access Actuator endpoints must return HTTP 403 Forbidden")
    void testUserRole_CannotAccessActuator_Returns403() throws Exception {
        String email = "authz_actuator_denied@example.com";
        Role userRole = getOrCreateUserRole();

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Actuator");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        mockMvc.perform(get("/actuator/beans")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN role with ADMIN_VIEW permission is authorized to access admin resource")
    void testAdminRole_CanAccessAdminEndpoints() throws Exception {
        Permission viewPerm = getOrCreatePermission("Admin View", "ADMIN_VIEW", true);
        Role adminRole = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(viewPerm));

        String email = "authz_allowed_admin@example.com";
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Allowed Admin");
        admin.setUsername("authz_allowed_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(adminRole);
        admin.setSessionId("sess-authz-admin");
        adminRepository.save(admin);

        String token = jwtUtil.generateToken(
                email,
                List.of("ROLE_ADMIN", "ADMIN_VIEW"),
                "sess-authz-admin",
                "ADMIN"
        );

        mockMvc.perform(get("/api/admins/statistics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("ADMIN role lacking ADMIN_DELETE permission must return HTTP 403 Forbidden on delete")
    void testAdminRole_LackingRequiredPermission_Returns403() throws Exception {
        Permission viewPerm = getOrCreatePermission("Admin View", "ADMIN_VIEW", true);
        Role readOnlyAdminRole = getOrCreateRoleWithPermissions("ROLE_ADMIN_VIEWONLY", Set.of(viewPerm));

        String email = "authz_readonly_admin@example.com";
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("ReadOnly Admin");
        admin.setUsername("authz_readonly_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(readOnlyAdminRole);
        admin.setSessionId("sess-authz-readonly");
        adminRepository.save(admin);

        // Token only contains ADMIN_VIEW, NOT ADMIN_DELETE
        String token = jwtUtil.generateToken(
                email,
                List.of("ROLE_ADMIN", "ADMIN_VIEW"),
                "sess-authz-readonly",
                "ADMIN"
        );

        mockMvc.perform(delete("/api/admins/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Inactive permission in role must not be granted and should return 403 Forbidden")
    void testInactivePermission_NotGranted() throws Exception {
        Permission inactivePerm = getOrCreatePermission("Admin Inactive Action", "ADMIN_INACTIVE_ACTION", false);
        Role adminRole = getOrCreateRoleWithPermissions("ROLE_ADMIN_INACTIVE_TEST", Set.of(inactivePerm));

        String email = "authz_inactive_perm_admin@example.com";
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Inactive Perm Admin");
        admin.setUsername("authz_inactive_perm_admin");
        admin.setEmail(email);
        admin.setPassword("password123");
        admin.setIsActive(true);
        admin.setRole(adminRole);
        admin.setSessionId("sess-authz-inactive-perm");
        adminRepository.save(admin);

        // Token lacks ADMIN_VIEW, and user only has an inactive permission
        String token = jwtUtil.generateToken(
                email,
                List.of("ROLE_ADMIN"),
                "sess-authz-inactive-perm",
                "ADMIN"
        );

        mockMvc.perform(get("/api/admins/statistics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Server-side identity validation: user cannot access or modify another user's owned resources")
    void testServerSideIdentity_UserCannotImpersonateAnother() {
        String victimEmail = "server_victim@example.com";
        User victim = userRepository.findByEmail(victimEmail).orElseGet(User::new);
        victim.setFirstName("Victim");
        victim.setLastName("User");
        victim.setEmail(victimEmail);
        victim.setPassword("password123");
        victim.setIsActive(true);
        victim.setIsBlocked(false);
        victim.setIsDeleted(false);
        victim = userRepository.save(victim);

        String attackerEmail = "server_attacker@example.com";
        User attacker = userRepository.findByEmail(attackerEmail).orElseGet(User::new);
        attacker.setFirstName("Attacker");
        attacker.setLastName("User");
        attacker.setEmail(attackerEmail);
        attacker.setPassword("password123");
        attacker.setIsActive(true);
        attacker.setIsBlocked(false);
        attacker.setIsDeleted(false);
        attacker = userRepository.save(attacker);

        // Ownership must return false when attacker's email is evaluated against victim's ID
        boolean isOwner = userSecurity.isOwner(victim.getId(), attackerEmail);
        assertFalse(isOwner, "UserSecurity must reject ownership when authenticated email does not match entity owner");

        // Ownership must return true for legitimate owner
        boolean isLegitOwner = userSecurity.isOwner(victim.getId(), victimEmail);
        assertTrue(isLegitOwner, "UserSecurity must confirm ownership when authenticated email matches entity owner");
    }

    @Test
    @DisplayName("Authorization 403 error response must not leak server internals or stack traces")
    void testAuthorizationError_NoInformationLeakage() throws Exception {
        String email = "leak_test_user@example.com";
        Role userRole = getOrCreateUserRole();

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("Leak");
        user.setLastName("User");
        user.setEmail(email);
        user.setPassword("password123");
        user.setIsActive(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        String token = TestSecurityUtils.generateTestToken(jwtUtil, email, "ROLE_USER", "USER");

        String responseBody = mockMvc.perform(get("/api/admins/statistics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseBody.contains("org.springframework"), "Response must not contain framework packages");
        assertFalse(responseBody.contains("Exception"), "Response must not contain exception stack trace");
        assertFalse(responseBody.contains("SELECT "), "Response must not contain SQL queries");
    }
}
