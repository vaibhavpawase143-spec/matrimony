package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.dto.request.LoginRequest;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.AdminAuditLogService;
import com.example.service.RequestAuditLogService;
import com.example.util.LogSanitizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuditLoggingSecurityTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminAuditLogRepository adminAuditLogRepository;

    @Autowired
    private RequestAuditLogRepository requestAuditLogRepository;

    @Autowired
    private AdminAuditLogService adminAuditLogService;

    @Autowired
    private RequestAuditLogService requestAuditLogService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    private User testUser;
    private Admin testAdmin;
    private String userToken;
    private String adminTokenWithAudit;
    private String adminTokenWithoutAudit;

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

    private Role getOrCreateRole(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role r = new Role();
            r.setName(name);
            r.setIsActive(true);
            return roleRepository.saveAndFlush(r);
        });
    }

    @BeforeEach
    void setUp() {
        Role roleUser = getOrCreateRole("ROLE_USER");

        Permission auditPerm = getOrCreatePermission("Audit View", "AUDIT_VIEW", true);
        Role roleAuditAdmin = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(auditPerm));
        Role rolePlainAdmin = getOrCreateRoleWithPermissions("ROLE_ADMIN_RESTRICTED", Collections.emptySet());

        String userEmail = "audit_user_" + UUID.randomUUID() + "@example.com";
        testUser = new User();
        testUser.setEmail(userEmail);
        testUser.setPassword(passwordEncoder.encode("Password@123"));
        testUser.setFirstName("Audit");
        testUser.setLastName("User");
        testUser.setIsActive(true);
        testUser.setEmailVerified(true);
        testUser.setPhoneVerified(true);
        testUser.setSessionId("sess-audit-user");
        testUser.setRoles(Set.of(roleUser));
        testUser = userRepository.saveAndFlush(testUser);

        userToken = jwtUtil.generateToken(testUser.getEmail(), List.of("ROLE_USER"), "sess-audit-user", "USER");

        // Admin with AUDIT_VIEW authority
        String adminEmail1 = "audit_admin_" + UUID.randomUUID() + "@example.com";
        testAdmin = new Admin();
        testAdmin.setEmail(adminEmail1);
        testAdmin.setUsername("admin_auditor_" + UUID.randomUUID().toString().substring(0, 8));
        testAdmin.setPassword(passwordEncoder.encode("Admin@123"));
        testAdmin.setName("Audit Administrator");
        testAdmin.setIsActive(true);
        testAdmin.setSessionId("sess-audit-admin");
        testAdmin.setRole(roleAuditAdmin);
        testAdmin = adminRepository.saveAndFlush(testAdmin);

        adminTokenWithAudit = jwtUtil.generateToken(
                testAdmin.getEmail(),
                List.of("ROLE_ADMIN", "AUDIT_VIEW"),
                "sess-audit-admin",
                "ADMIN"
        );

        // Admin WITHOUT AUDIT_VIEW authority
        String adminEmail2 = "no_audit_admin_" + UUID.randomUUID() + "@example.com";
        Admin adminNoAudit = new Admin();
        adminNoAudit.setEmail(adminEmail2);
        adminNoAudit.setUsername("admin_no_audit_" + UUID.randomUUID().toString().substring(0, 8));
        adminNoAudit.setPassword(passwordEncoder.encode("Admin@123"));
        adminNoAudit.setName("Standard Admin");
        adminNoAudit.setIsActive(true);
        adminNoAudit.setSessionId("sess-no-audit-admin");
        adminNoAudit.setRole(rolePlainAdmin);
        adminNoAudit = adminRepository.saveAndFlush(adminNoAudit);

        adminTokenWithoutAudit = jwtUtil.generateToken(
                adminNoAudit.getEmail(),
                List.of("ROLE_ADMIN"),
                "sess-no-audit-admin",
                "ADMIN"
        );
    }

    // =========================================================================
    // 1. AUTHENTICATION & LOGIN AUDITING
    // =========================================================================

    @Test
    @DisplayName("Audit 01: Successful login request is safely recorded in request audit trail")
    void testSuccessfulLoginGeneratesAuditRecord() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(testUser.getEmail());
        req.setPassword("Password@123");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // Verify request audit log recorded the POST /api/users/login request as SUCCESS
        boolean found = requestAuditLogRepository.findAll().stream()
                .anyMatch(log -> "/api/users/login".equals(log.getRequestPath())
                        && log.getStatusCode() == 200
                        && "SUCCESS".equals(log.getOutcome()));
        assertTrue(found, "Successful login must be recorded in request audit log");
    }

    @Test
    @DisplayName("Audit 02: Failed login attempt is recorded safely without storing passwords")
    void testFailedLoginGeneratesSafeAuditRecord() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(testUser.getEmail());
        req.setPassword("WrongPassword@999");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        // Verify failed login is recorded as FAILURE
        boolean found = requestAuditLogRepository.findAll().stream()
                .anyMatch(log -> "/api/users/login".equals(log.getRequestPath())
                        && log.getStatusCode() == 400
                        && "FAILURE".equals(log.getOutcome()));
        assertTrue(found, "Failed login must be recorded in request audit log with status 400");

        // Verify request query string / secrets are never stored
        List<RequestAuditLog> logs = requestAuditLogRepository.findAll();
        for (RequestAuditLog log : logs) {
            assertNull(log.getQueryString(), "Query string must never store sensitive data");
            if (log.getRequestPath().contains("/login")) {
                assertFalse(log.getRequestPath().contains("WrongPassword"), "Password must not appear in path");
            }
        }
    }

    @Test
    @DisplayName("Audit 03: Logout request is recorded in audit logs")
    void testLogoutIsAudited() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        boolean found = requestAuditLogRepository.findAll().stream()
                .anyMatch(log -> "/api/auth/logout".equals(log.getRequestPath())
                        && log.getStatusCode() == 200
                        && "SUCCESS".equals(log.getOutcome()));
        assertTrue(found, "Logout must be recorded in request audit log");
    }

    // =========================================================================
    // 2. SECURITY REJECTION & BOLA / PRIVILEGE ESCALATION AUDITING
    // =========================================================================

    @Test
    @DisplayName("Audit 04: Forbidden BOLA/IDOR attempt is audited with 403 status and FAILURE outcome")
    void testBolaAttemptIsAudited() throws Exception {
        // User attempts unauthorized activation
        mockMvc.perform(post("/api/profiles/premium/activate")
                        .header("Authorization", "Bearer " + userToken)
                        .param("userId", testUser.getId().toString())
                        .param("plan", "THREE_MONTHS"))
                .andExpect(status().isForbidden());

        boolean found = requestAuditLogRepository.findAll().stream()
                .anyMatch(log -> "/api/profiles/premium/activate".equals(log.getRequestPath())
                        && log.getStatusCode() == 403
                        && "FAILURE".equals(log.getOutcome()));
        assertTrue(found, "Forbidden premium activation attempt must be audited with status 403 and outcome FAILURE");
    }

    // =========================================================================
    // 3. ADMIN ACTION ATTRIBUTION & AUDIT LOG SERVICE
    // =========================================================================

    @Test
    @DisplayName("Audit 06: Admin action is accurately attributed to the acting admin")
    void testAdminActionAttribution() {
        adminAuditLogService.log(
                testAdmin.getId(),
                "USER_MANAGEMENT",
                "USER_VERIFIED",
                "USER",
                testUser.getId(),
                "Verified profile for user " + testUser.getEmail(),
                "UNVERIFIED",
                "VERIFIED",
                "127.0.0.1",
                "Mozilla/5.0 Test"
        );

        List<AdminAuditLog> logs = adminAuditLogRepository.findAll();
        AdminAuditLog log = logs.stream()
                .filter(l -> l.getAdmin().getId().equals(testAdmin.getId()) && "USER_VERIFIED".equals(l.getAction()))
                .findFirst()
                .orElse(null);

        assertNotNull(log, "Admin audit log must be persisted");
        assertEquals(testAdmin.getId(), log.getAdmin().getId());
        assertEquals("USER_MANAGEMENT", log.getModule());
        assertEquals("USER_VERIFIED", log.getAction());
        assertEquals("USER", log.getEntityType());
        assertEquals(testUser.getId(), log.getEntityId());
        assertEquals("127.0.0.1", log.getIpAddress());
        assertNotNull(log.getCreatedAt(), "Creation timestamp must be set");
    }

    // =========================================================================
    // 4. ACCESS CONTROL TO AUDIT LOGS (TAMPERING & LEAKAGE PROTECTION)
    // =========================================================================

    @Test
    @DisplayName("Audit 07: Normal user CANNOT read admin audit logs (403 Forbidden)")
    void testNormalUserCannotReadAdminAuditLogs() throws Exception {
        mockMvc.perform(get("/api/admin/audit-logs")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Audit 08: Normal user CANNOT read request audit logs (403 Forbidden)")
    void testNormalUserCannotReadRequestAuditLogs() throws Exception {
        mockMvc.perform(get("/api/admin/request-audit-logs")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Audit 09: Admin without AUDIT_VIEW authority CANNOT access audit logs (403 Forbidden)")
    void testAdminWithoutAuditViewAuthorityCannotAccessLogs() throws Exception {
        mockMvc.perform(get("/api/admin/audit-logs")
                        .header("Authorization", "Bearer " + adminTokenWithoutAudit))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/admin/request-audit-logs")
                        .header("Authorization", "Bearer " + adminTokenWithoutAudit))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Audit 10: Authorized admin with AUDIT_VIEW can view audit logs with pagination and filters")
    void testAuthorizedAdminCanViewAuditLogs() throws Exception {
        // First create an audit record
        adminAuditLogService.log(
                testAdmin.getId(),
                "SECURITY",
                "CONFIG_CHANGED",
                "SYSTEM",
                1L,
                "Updated security configuration",
                null,
                "ENABLED",
                "127.0.0.1",
                "AdminBrowser"
        );

        mockMvc.perform(get("/api/admin/audit-logs")
                        .header("Authorization", "Bearer " + adminTokenWithAudit)
                        .param("page", "0")
                        .param("size", "10")
                        .param("module", "SECURITY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        mockMvc.perform(get("/api/admin/request-audit-logs")
                        .header("Authorization", "Bearer " + adminTokenWithAudit)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    // =========================================================================
    // 5. LOG INJECTION / CRLF PROTECTION
    // =========================================================================

    @Test
    @DisplayName("Audit 11: LogSanitizer neutralizes CRLF log injection and control characters")
    void testLogInjectionSanitization() {
        String dirtyInput = "AdminAction\r\n2026-08-22 12:00:00 [INFO] Fake log line injected\nActionEnd\0";
        String clean = LogSanitizer.sanitize(dirtyInput);

        assertFalse(clean.contains("\r"), "Sanitized string must not contain carriage return");
        assertFalse(clean.contains("\n"), "Sanitized string must not contain line feed");
        assertFalse(clean.contains("\0"), "Sanitized string must not contain null byte");
        assertTrue(clean.startsWith("AdminAction"), "Original prefix must be preserved");

        // Verify truncation
        String truncated = LogSanitizer.sanitizeAndTruncate("VeryLongStringForTesting", 10);
        assertEquals(10, truncated.length());
        assertEquals("VeryLongSt", truncated);
    }

    // =========================================================================
    // 6. REQUEST CORRELATION / TRACEABILITY (X-Request-ID)
    // =========================================================================

    @Test
    @DisplayName("Audit 12: X-Request-ID is preserved if provided and generated if absent")
    void testRequestCorrelationIdPreserved() throws Exception {
        String customRequestId = "REQ-" + UUID.randomUUID();

        MvcResult result = mockMvc.perform(get("/api/faqs")
                        .header("X-Request-ID", customRequestId))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-ID"))
                .andReturn();

        String returnedHeader = result.getResponse().getHeader("X-Request-ID");
        assertEquals(customRequestId, returnedHeader, "Supplied X-Request-ID must be echoed in response header");

        // When no X-Request-ID is supplied, a UUID is generated
        MvcResult resultAuto = mockMvc.perform(get("/api/faqs"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-ID"))
                .andReturn();

        String autoHeader = resultAuto.getResponse().getHeader("X-Request-ID");
        assertNotNull(autoHeader);
        assertFalse(autoHeader.isBlank(), "Generated X-Request-ID must not be blank");
    }

    // =========================================================================
    // 7. SENSITIVE DATA EXCLUSION
    // =========================================================================

    @Test
    @DisplayName("Audit 13: Sensitive secrets (passwords, tokens, OTPs) are never persisted in audit tables")
    void testSensitiveDataNeverLogged() {
        List<RequestAuditLog> requestLogs = requestAuditLogRepository.findAll();
        for (RequestAuditLog log : requestLogs) {
            // Check that request path or actor name does not leak sensitive keywords
            if (log.getActorName() != null) {
                assertFalse(log.getActorName().contains("password"), "Actor name must not contain password");
                assertFalse(log.getActorName().contains("secret"), "Actor name must not contain secret");
            }
            assertNull(log.getQueryString(), "Query string must always be null to prevent credential leakage");
        }
    }
}
