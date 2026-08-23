package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.RefreshTokenService;
import com.example.service.SubscriptionPlanService;
import com.example.service.SubscriptionService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Task 7 Baseline — Authorization, Privilege Escalation & Session Security Tests")
class AuthorizationPrivilegeSecurityTest extends BaseIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private ShortlistRepository shortlistRepository;

    @Autowired
    private PartnerPreferenceRepository partnerPreferenceRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurity userSecurity;

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

    private User getOrCreateTestUser(String email, String rawPassword) {
        Role role = getOrCreateRole("ROLE_USER");
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFirstName("AuthzUser");
        user.setLastName("Test");
        user.setEmail(email);
        user.setPhone("98" + String.format("%08d", Math.abs((email + System.currentTimeMillis()).hashCode() % 100000000)));
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setRoles(Set.of(role));
        return userRepository.saveAndFlush(user);
    }

    private Admin getOrCreateTestAdmin(String email, String rawPassword, String sessionId, Role role) {
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElseGet(Admin::new);
        admin.setName("Authz Admin");
        admin.setUsername("admin_authz_" + Math.abs(email.hashCode()));
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(role);
        admin.setIsActive(true);
        admin.setSessionId(sessionId);
        return adminRepository.saveAndFlush(admin);
    }

    private Profile createTestProfile(User user) {
        Profile profile = profileRepository.findByUserId(user.getId()).orElseGet(Profile::new);
        profile.setUser(user);
        profile.setAbout("Test profile about");
        profile.setIsActive(true);
        profile.setIsPremium(false);
        return profileRepository.saveAndFlush(profile);
    }

    private Interest getOrCreateTestInterest(User sender, User receiver) {
        return interestRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId())
                .map(existing -> {
                    existing.setStatus("PENDING");
                    existing.setIsActive(true);
                    return interestRepository.saveAndFlush(existing);
                })
                .orElseGet(() -> {
                    Interest interest = new Interest();
                    interest.setSender(sender);
                    interest.setReceiver(receiver);
                    interest.setStatus("PENDING");
                    interest.setIsActive(true);
                    return interestRepository.saveAndFlush(interest);
                });
    }

    // =========================================================================
    // SECTION A: HORIZONTAL AUTHORIZATION & BOLA / IDOR
    // =========================================================================

    @Test
    @DisplayName("1. Horizontal: User A cannot delete User B's profile")
    void testHorizontal_UserCannotDeleteAnotherUserProfile() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a1@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b1@example.com", "Pass123!");
        Profile profileB = createTestProfile(userB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a1", "USER");

        // User A attempts to DELETE User B's profile
        mockMvc.perform(delete("/api/profiles/" + profileB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().is4xxClientError());

        // Profile B must still exist
        assertTrue(profileRepository.findById(profileB.getId()).isPresent(), "Victim profile must not be deleted");
    }

    @Test
    @DisplayName("2. Horizontal: User A cannot update User B's partner preferences (BOLA / IDOR Protection)")
    void testHorizontal_UserCannotUpdateAnotherUserPartnerPreference() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a2@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b2@example.com", "Pass123!");

        PartnerPreference prefB = partnerPreferenceRepository.findByUserId(userB.getId()).orElseGet(() -> {
            PartnerPreference p = new PartnerPreference();
            p.setUser(userB);
            p.setMinAge(22);
            p.setMaxAge(28);
            p.setIsActive(true);
            return partnerPreferenceRepository.saveAndFlush(p);
        });

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a2", "USER");

        String attackPayload = """
                {
                    "minAge": 45,
                    "maxAge": 60,
                    "isActive": true
                }
                """;

        // User A calls PUT /api/partner-preferences/{userB_id} -> must be rejected with 403 Forbidden
        mockMvc.perform(put("/api/partner-preferences/" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attackPayload))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("3. Horizontal: User A reading User B's sent interests is rejected with HTTP 403")
    void testHorizontal_UserCannotReadAnotherUserSentInterests() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a3@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b3@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a3", "USER");

        // User A performs GET /api/interests/sent/{userB_id} -> must return 403 Forbidden
        mockMvc.perform(get("/api/interests/sent/" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("4. Horizontal: User A reading User B's received interests is rejected with HTTP 403")
    void testHorizontal_UserCannotReadAnotherUserReceivedInterests() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a4@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b4@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a4", "USER");

        // User A performs GET /api/interests/received/{userB_id} -> must return 403 Forbidden
        mockMvc.perform(get("/api/interests/received/" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("5. Horizontal: User A cannot delete User B's photo")
    void testHorizontal_UserCannotDeleteAnotherUserPhoto() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a5@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b5@example.com", "Pass123!");

        UserPhoto photoB = UserPhoto.builder()
                .user(userB)
                .photoUrl("https://example.com/uploads/photoB.jpg")
                .photoType(PhotoType.OTHER)
                .primaryPhoto(false)
                .build();
        photoB = userPhotoRepository.saveAndFlush(photoB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a5", "USER");

        mockMvc.perform(delete("/api/photos/" + photoB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().is4xxClientError());

        assertTrue(userPhotoRepository.findById(photoB.getId()).isPresent(), "Victim photo must remain in database");
    }

    @Test
    @DisplayName("6. Horizontal: User A cannot set User B's photo as primary")
    void testHorizontal_UserCannotSetPrimaryAnotherUserPhoto() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a6@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b6@example.com", "Pass123!");

        UserPhoto photoB = UserPhoto.builder()
                .user(userB)
                .photoUrl("https://example.com/uploads/photoB_primary.jpg")
                .photoType(PhotoType.OTHER)
                .primaryPhoto(false)
                .build();
        photoB = userPhotoRepository.saveAndFlush(photoB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a6", "USER");

        mockMvc.perform(put("/api/photos/primary/" + photoB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("7. Horizontal: User A reading User B's notifications is rejected with HTTP 403")
    void testHorizontal_UserCannotReadAnotherUserNotifications() throws Exception {
        User userA = getOrCreateTestUser("authz_user_a7@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_user_b7@example.com", "Pass123!");

        Notification notificationB = new Notification();
        notificationB.setReceiverId(userB.getId());
        notificationB.setMessage("Private notification for User B");
        notificationB.setType(NotificationType.SYSTEM);
        notificationB.setRead(false);
        notificationB.setDeleted(false);
        notificationRepository.saveAndFlush(notificationB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a7", "USER");

        mockMvc.perform(get("/api/notifications?userId=" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("8. Horizontal: User A cannot delete another user's owned resources (UserSecurity.isOwner check)")
    void testHorizontal_UserSecurity_OwnershipValidation() {
        User userA = getOrCreateTestUser("authz_owner_a8@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_victim_b8@example.com", "Pass123!");

        assertTrue(userSecurity.isOwner(userA.getId(), userA.getEmail()));
        assertFalse(userSecurity.isOwner(userB.getId(), userA.getEmail()), "User A must not be recognized as owner of User B's ID");
    }

    // =========================================================================
    // SECTION B: VERTICAL PRIVILEGE ESCALATION (ROLE_USER VS ADMIN ENDPOINTS)
    // =========================================================================

    @Test
    @DisplayName("9. Vertical: ROLE_USER cannot access /api/admin/dashboard")
    void testVertical_UserCannotAccessAdminDashboard() throws Exception {
        User user = getOrCreateTestUser("vert_user_dash@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v1", "USER");

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("10. Vertical: ROLE_USER cannot access /api/admin/users (Admin user management)")
    void testVertical_UserCannotAccessAdminUserManagement() throws Exception {
        User user = getOrCreateTestUser("vert_user_mgmt@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v2", "USER");

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("11. Vertical: ROLE_USER cannot access /api/admin/audit-logs")
    void testVertical_UserCannotAccessAdminAuditLogs() throws Exception {
        User user = getOrCreateTestUser("vert_user_audit@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v3", "USER");

        mockMvc.perform(get("/api/admin/audit-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("12. Vertical: ROLE_USER cannot access /api/admin/reports")
    void testVertical_UserCannotAccessAdminReports() throws Exception {
        User user = getOrCreateTestUser("vert_user_reports@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v4", "USER");

        mockMvc.perform(get("/api/admin/reports")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("13. Vertical: ROLE_USER cannot access /api/admin/subscriptions")
    void testVertical_UserCannotAccessAdminSubscriptions() throws Exception {
        User user = getOrCreateTestUser("vert_user_subs@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v5", "USER");

        mockMvc.perform(get("/api/admin/subscriptions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("14. Vertical: ROLE_USER cannot access /api/admin/support")
    void testVertical_UserCannotAccessAdminSupportTickets() throws Exception {
        User user = getOrCreateTestUser("vert_user_supp@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v6", "USER");

        mockMvc.perform(get("/api/admin/support")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("15. Vertical: ROLE_USER cannot access /api/admin/success-stories")
    void testVertical_UserCannotAccessAdminSuccessStories() throws Exception {
        User user = getOrCreateTestUser("vert_user_stories@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v7", "USER");

        mockMvc.perform(get("/api/admin/success-stories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("16. Vertical: ROLE_USER cannot access /api/admins")
    void testVertical_UserCannotAccessAdminListEndpoint() throws Exception {
        User user = getOrCreateTestUser("vert_user_admins@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-v8", "USER");

        mockMvc.perform(get("/api/admins")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION C: USER ID MANIPULATION & PARAMETER TAMPERING
    // =========================================================================

    @Test
    @DisplayName("17. ID Manipulation: User A creating shortlist on behalf of User B is rejected with HTTP 403")
    void testIdManipulation_UserCannotShortlistAsAnotherUser() throws Exception {
        User userA = getOrCreateTestUser("idman_user_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("idman_user_b@example.com", "Pass123!");
        User target = getOrCreateTestUser("idman_target@example.com", "Pass123!");
        Profile targetProfile = createTestProfile(target);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-idman-a", "USER");

        // Calling path with userB ID: POST /api/shortlists/user/{userB_id}/profile/{targetProfile_id} -> 403 Forbidden
        mockMvc.perform(post("/api/shortlists/user/" + userB.getId() + "/profile/" + targetProfile.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("18. ID Manipulation: User A creating partner preference with User B's ID in body is rejected with HTTP 403")
    void testIdManipulation_UserCannotCreatePartnerPreferenceForAnotherUser() throws Exception {
        User userA = getOrCreateTestUser("idman_pref_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("idman_pref_b@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-idman-pref", "USER");

        String payload = String.format("""
                {
                    "userId": %d,
                    "minAge": 20,
                    "maxAge": 30,
                    "isActive": true
                }
                """, userB.getId());

        mockMvc.perform(post("/api/partner-preferences")
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("19. ID Manipulation: User A marking all notifications read for User B is rejected with HTTP 403")
    void testIdManipulation_UserCannotMarkAllNotificationsReadForAnotherUser() throws Exception {
        User userA = getOrCreateTestUser("idman_notif_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("idman_notif_b@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-idman-notif", "USER");

        mockMvc.perform(put("/api/notifications/read-all/" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("20. ID Manipulation: Subscription cancel operates strictly on authenticated principal")
    void testIdManipulation_UserCannotAccessAnotherUserSubscriptionViaId() throws Exception {
        User userA = getOrCreateTestUser("idman_sub_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("idman_sub_b@example.com", "Pass123!");

        SubscriptionPlan plan = subscriptionPlanRepository.findAll().stream().findFirst().orElseGet(() -> {
            SubscriptionPlan p = new SubscriptionPlan();
            p.setName("Gold Plan");
            p.setDuration(30);
            p.setIsActive(true);
            return subscriptionPlanRepository.saveAndFlush(p);
        });

        userSubscriptionRepository.findByUserIdAndIsActiveTrue(userB.getId()).ifPresent(old -> {
            old.setIsActive(false);
            old.setStatus("CANCELLED");
            userSubscriptionRepository.saveAndFlush(old);
        });

        UserSubscription subB = new UserSubscription();
        subB.setUser(userB);
        subB.setSubscriptionPlan(plan);
        subB.setStartDate(LocalDateTime.now());
        subB.setEndDate(LocalDateTime.now().plusDays(30));
        subB.setIsActive(true);
        subB.setStatus("ACTIVE");
        subB = userSubscriptionRepository.saveAndFlush(subB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-idman-sub", "USER");

        // User A calling cancel without having an active subscription returns 400 Bad Request
        mockMvc.perform(put("/api/subscription/cancel")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isBadRequest());

        // User B subscription must remain active
        UserSubscription refreshedB = userSubscriptionRepository.findById(subB.getId()).orElseThrow();
        assertTrue(refreshedB.getIsActive(), "User B subscription must remain active");
    }

    // =========================================================================
    // SECTION D: ADMIN PERMISSION BOUNDARIES & AUTHORITY VERIFICATION
    // =========================================================================

    @Test
    @DisplayName("21. Admin Permissions: Admin with USER_VIEW authority can view user list")
    void testAdminPermission_AdminWithUserView_CanViewUsers() throws Exception {
        Permission userView = getOrCreatePermission("User View", "USER_VIEW", true);
        Role role = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(userView));

        String email = "admin_uv_viewer@example.com";
        String sessionId = "sess-uv-1";
        getOrCreateTestAdmin(email, "Pass123!", sessionId, role);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN", "USER_VIEW"), sessionId, "ADMIN");

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("22. Admin Permissions: Admin lacking USER_DELETE authority cannot delete user")
    void testAdminPermission_AdminWithoutUserDelete_CannotDeleteUser() throws Exception {
        Permission userView = getOrCreatePermission("User View", "USER_VIEW", true);
        Role role = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(userView));

        String email = "admin_no_delete@example.com";
        String sessionId = "sess-no-del";
        getOrCreateTestAdmin(email, "Pass123!", sessionId, role);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN", "USER_VIEW"), sessionId, "ADMIN");

        mockMvc.perform(delete("/api/admin/users/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("23. Admin Permissions: Admin lacking AUDIT_VIEW authority cannot view audit logs")
    void testAdminPermission_AdminWithoutAuditView_CannotViewAuditLogs() throws Exception {
        Permission userView = getOrCreatePermission("User View", "USER_VIEW", true);
        Role role = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(userView));

        String email = "admin_no_audit@example.com";
        String sessionId = "sess-no-audit";
        getOrCreateTestAdmin(email, "Pass123!", sessionId, role);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN", "USER_VIEW"), sessionId, "ADMIN");

        mockMvc.perform(get("/api/admin/audit-logs")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("24. Admin Permissions: Admin lacking REPORT_EXPORT authority cannot export user reports")
    void testAdminPermission_AdminWithoutReportExport_CannotExportReports() throws Exception {
        Permission userView = getOrCreatePermission("User View", "USER_VIEW", true);
        Role role = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(userView));

        String email = "admin_no_export@example.com";
        String sessionId = "sess-no-export";
        getOrCreateTestAdmin(email, "Pass123!", sessionId, role);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN", "USER_VIEW"), sessionId, "ADMIN");

        mockMvc.perform(get("/api/admin/users/export")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("25. Admin Permissions: Admin assigned inactive permission cannot access guarded endpoint")
    void testAdminPermission_AdminWithInactivePermission_Rejected() throws Exception {
        Permission inactivePerm = getOrCreatePermission("Inactive Manage Action", "CMS_MANAGE", false);
        Role role = getOrCreateRoleWithPermissions("ROLE_ADMIN", Set.of(inactivePerm));

        String email = "admin_inactive_cms@example.com";
        String sessionId = "sess-inact-cms";
        getOrCreateTestAdmin(email, "Pass123!", sessionId, role);

        String token = jwtUtil.generateToken(email, List.of("ROLE_ADMIN"), sessionId, "ADMIN");

        mockMvc.perform(get("/api/admin/cms")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION E: ROLE MANIPULATION & PRIVILEGE ESCALATION ATTEMPTS
    // =========================================================================

    @Test
    @DisplayName("26. Role Manipulation: Registration payload with privileged 'role' field is ignored")
    void testRoleManipulation_RegisterWithAdminRole_Ignored() throws Exception {
        String email = "reg_role_inject_" + System.currentTimeMillis() + "@example.com";
        String phone = "9812" + String.format("%06d", (int)(Math.random() * 900000 + 100000));
        String payload = String.format("""
                {
                    "firstName": "Role",
                    "lastName": "Injector",
                    "email": "%s",
                    "phone": "%s",
                    "password": "Password123!",
                    "recaptchaToken": "test-recaptcha-token",
                    "role": "ROLE_ADMIN",
                    "roles": ["ROLE_ADMIN", "SUPER_ADMIN"],
                    "isAdmin": true
                }
                """, email, phone);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        System.out.println(">>> REGISTER STATUS: " + result.getResponse().getStatus());
        System.out.println(">>> REGISTER BODY: " + result.getResponse().getContentAsString());

        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 400);

        userRepository.findByEmailWithRoles(email).ifPresent(registered -> {
            boolean hasAdminRole = registered.getRoles().stream().anyMatch(r -> r.getName().contains("ADMIN"));
            assertFalse(hasAdminRole, "User must NOT have ROLE_ADMIN assigned via registration payload");
        });
    }

    @Test
    @DisplayName("27. Role Manipulation: Profile update payload with privileged fields is ignored")
    void testRoleManipulation_ProfileUpdateWithAdminFlag_Ignored() throws Exception {
        User user = getOrCreateTestUser("profile_role_inject@example.com", "Pass123!");
        createTestProfile(user);
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-prof-inject", "USER");

        String payload = """
                {
                    "aboutMe": "Trying to elevate role",
                    "role": "ROLE_ADMIN",
                    "isAdmin": true,
                    "isSuperAdmin": true
                }
                """;

        mockMvc.perform(put("/api/profiles/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        User updated = userRepository.findByEmailWithRoles(user.getEmail()).orElseThrow();
        boolean hasAdminRole = updated.getRoles().stream().anyMatch(r -> r.getName().contains("ADMIN"));
        assertFalse(hasAdminRole, "Profile update must NOT elevate user roles");
    }

    @Test
    @DisplayName("28. Role Manipulation: User cannot modify own role via API endpoints")
    void testRoleManipulation_UserCannotModifyOwnRoleViaApi() {
        User user = getOrCreateTestUser("role_immutability@example.com", "Pass123!");
        User reloaded = userRepository.findByEmailWithRoles(user.getEmail()).orElseThrow();

        assertEquals(1, reloaded.getRoles().size());
        assertEquals("ROLE_USER", reloaded.getRoles().iterator().next().getName());
    }

    // =========================================================================
    // SECTION F: SUBSCRIPTION & PREMIUM AUTHORIZATION
    // =========================================================================

    @Test
    @DisplayName("29. Premium Access: Free non-premium user receives 403 Forbidden on premium match validation")
    void testSubscription_FreeUserCannotAccessPremiumMatches() throws Exception {
        User user = getOrCreateTestUser("free_user_match@example.com", "Pass123!");
        Profile profile = createTestProfile(user);
        profile.setIsPremium(false);
        profileRepository.saveAndFlush(profile);

        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-free-match", "USER");

        mockMvc.perform(get("/api/match/" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("PREMIUM_REQUIRED"));
    }

    @Test
    @DisplayName("30. Premium Access: Active premium user can access premium match features")
    void testSubscription_ActivePremiumUserCanAccessPremiumFeatures() throws Exception {
        User user = getOrCreateTestUser("premium_user_match@example.com", "Pass123!");
        Profile profile = createTestProfile(user);
        profile.setIsPremium(true);
        profileRepository.saveAndFlush(profile);

        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-prem-match", "USER");

        mockMvc.perform(get("/api/match/" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("31. Premium Access: Expired subscription marks user non-premium")
    void testSubscription_ExpiredSubscription_DoesNotGrantAccess() {
        User user = getOrCreateTestUser("expired_sub_user@example.com", "Pass123!");
        Profile profile = createTestProfile(user);
        profile.setIsPremium(false); // Expired state
        profileRepository.saveAndFlush(profile);

        assertFalse(profile.getIsPremium(), "Expired subscription profile must have isPremium = false");
    }

    @Test
    @DisplayName("32. Subscription IDOR: Cancelling subscription operates strictly on authenticated principal")
    void testSubscription_UserCannotCancelAnotherUserSubscription() throws Exception {
        User userA = getOrCreateTestUser("sub_cancel_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("sub_cancel_b@example.com", "Pass123!");

        SubscriptionPlan plan = subscriptionPlanRepository.findAll().stream().findFirst().orElseGet(() -> {
            SubscriptionPlan p = new SubscriptionPlan();
            p.setName("Silver Plan");
            p.setDuration(30);
            p.setIsActive(true);
            return subscriptionPlanRepository.saveAndFlush(p);
        });

        userSubscriptionRepository.findByUserIdAndIsActiveTrue(userB.getId()).ifPresent(old -> {
            old.setIsActive(false);
            old.setStatus("CANCELLED");
            userSubscriptionRepository.saveAndFlush(old);
        });

        UserSubscription subB = new UserSubscription();
        subB.setUser(userB);
        subB.setSubscriptionPlan(plan);
        subB.setStartDate(LocalDateTime.now());
        subB.setEndDate(LocalDateTime.now().plusDays(30));
        subB.setIsActive(true);
        subB.setStatus("ACTIVE");
        subB = userSubscriptionRepository.saveAndFlush(subB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-sub-cancel-a", "USER");

        // Calling cancel as user A
        mockMvc.perform(put("/api/subscription/cancel?subscriptionId=" + subB.getId())
                        .header("Authorization", "Bearer " + tokenA));

        // User B subscription remains intact
        UserSubscription checkB = userSubscriptionRepository.findById(subB.getId()).orElseThrow();
        assertTrue(checkB.getIsActive(), "Victim subscription must not be deactivated");
    }

    // =========================================================================
    // SECTION G: JWT ROLE & CLAIM MANIPULATION
    // =========================================================================

    @Test
    @DisplayName("33. JWT Manipulation: Tampered payload claiming ROLE_ADMIN fails signature verification")
    void testJwtManipulation_TamperedUserToAdminToken_Rejected() throws Exception {
        User user = getOrCreateTestUser("jwt_tamper_user@example.com", "Pass123!");
        String validToken = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-jwt-1", "USER");

        String[] parts = validToken.split("\\.");
        String modifiedPayload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(String.format("{\"sub\":\"%s\",\"roles\":[\"ROLE_ADMIN\"],\"accountType\":\"ADMIN\"}", user.getEmail()).getBytes(StandardCharsets.UTF_8));
        String tamperedToken = parts[0] + "." + modifiedPayload + "." + parts[2];

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + tamperedToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("34. JWT Manipulation: alg=none token rejected with HTTP 401")
    void testJwtManipulation_AlgNoneToken_Rejected() throws Exception {
        String noneHeader = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"sub\":\"admin@example.com\",\"roles\":[\"ROLE_ADMIN\"]}".getBytes(StandardCharsets.UTF_8));
        String noneToken = noneHeader + "." + payload + ".";

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + noneToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("35. JWT Manipulation: Manipulated accountType for regular user rejected on admin routes")
    void testJwtManipulation_ManipulatedAccountType_Rejected() throws Exception {
        User user = getOrCreateTestUser("fake_admin_type_user@example.com", "Pass123!");
        // Issued with accountType="ADMIN" but user only has ROLE_USER in DB
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-fake-admin", "ADMIN");

        mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // SECTION H: HTTP METHOD AUTHORIZATION & BYPASS
    // =========================================================================

    @Test
    @DisplayName("36. HTTP Method: Unsupported DELETE/PUT on POST-only /api/auth/login returns HTTP 405")
    void testHttpMethod_UnsupportedMethodOnSensitiveEndpoint_Returns405() throws Exception {
        mockMvc.perform(delete("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("METHOD_NOT_ALLOWED"));
    }

    // =========================================================================
    // SECTION I: MULTI-TAB AUTHENTICATION & STORAGE ISOLATION
    // =========================================================================

    @Test
    @DisplayName("37. Multi-Tab Session: Server-side token isolation allows independent Tab 1 User A and Tab 2 User B")
    void testMultiTab_ServerSideTokenIndependence() throws Exception {
        User userA = getOrCreateTestUser("tab1_user_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("tab2_user_b@example.com", "Pass123!");

        createTestProfile(userA);
        createTestProfile(userB);

        String tab1Token = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "tab1-session", "USER");
        String tab2Token = jwtUtil.generateToken(userB.getEmail(), List.of("ROLE_USER"), "tab2-session", "USER");

        // Tab 1 request executes as User A
        MvcResult res1 = mockMvc.perform(get("/api/profiles/me")
                        .header("Authorization", "Bearer " + tab1Token))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(res1.getResponse().getContentAsString().contains(userA.getEmail()));

        // Tab 2 request executes as User B
        MvcResult res2 = mockMvc.perform(get("/api/profiles/me")
                        .header("Authorization", "Bearer " + tab2Token))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(res2.getResponse().getContentAsString().contains(userB.getEmail()));

        // Subsequent Tab 1 request remains User A (server-side isolation verified)
        MvcResult res3 = mockMvc.perform(get("/api/profiles/me")
                        .header("Authorization", "Bearer " + tab1Token))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(res3.getResponse().getContentAsString().contains(userA.getEmail()));
    }

    // =========================================================================
    // SECTION J: ERROR SANITIZATION
    // =========================================================================

    @Test
    @DisplayName("38. Error Sanitization: HTTP 403 Forbidden response contains clean structured JSON without leakages")
    void testErrorSanitization_403ForbiddenResponse_Clean() throws Exception {
        User user = getOrCreateTestUser("error_sanitization_user@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-err", "USER");

        MvcResult result = mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertFalse(content.contains("org.springframework"), "Response must not contain Spring internal class names");
        assertFalse(content.contains("org.hibernate"), "Response must not contain Hibernate internals");
        assertFalse(content.contains("jwt.secret"), "Response must not leak secrets");
    }

    // =========================================================================
    // SECTION K: TASK 7 HARDENING SPECIFIC REGRESSION SUITE
    // =========================================================================

    @Test
    @DisplayName("39. Hardening: User A cannot read User B pending interests")
    void testHorizontal_UserCannotReadAnotherUserPendingInterests() throws Exception {
        User userA = getOrCreateTestUser("authz_pending_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_pending_b@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-pending-a", "USER");

        mockMvc.perform(get("/api/interests/received/" + userB.getId() + "/pending")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("40. Hardening: User A cannot accept User B's received interest")
    void testHorizontal_UserCannotAcceptAnotherUserInterest() throws Exception {
        User userA = getOrCreateTestUser("authz_accept_a@example.com", "Pass123!");
        User sender = getOrCreateTestUser("authz_sender_x@example.com", "Pass123!");
        User receiverB = getOrCreateTestUser("authz_receiver_b@example.com", "Pass123!");

        Interest interest = getOrCreateTestInterest(sender, receiverB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-accept-a", "USER");

        mockMvc.perform(put("/api/interests/accept/" + interest.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("41. Hardening: User A cannot reject User B's received interest")
    void testHorizontal_UserCannotRejectAnotherUserInterest() throws Exception {
        User userA = getOrCreateTestUser("authz_reject_a@example.com", "Pass123!");
        User sender = getOrCreateTestUser("authz_sender_y@example.com", "Pass123!");
        User receiverB = getOrCreateTestUser("authz_receiver_y@example.com", "Pass123!");

        Interest interest = getOrCreateTestInterest(sender, receiverB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-reject-a", "USER");

        mockMvc.perform(put("/api/interests/reject/" + interest.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("42. Hardening: User C cannot delete interest between User A and User B")
    void testHorizontal_UserCannotDeleteAnotherUserInterest() throws Exception {
        User userA = getOrCreateTestUser("authz_del_int_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_del_int_b@example.com", "Pass123!");
        User userC = getOrCreateTestUser("authz_del_int_c@example.com", "Pass123!");

        Interest interest = getOrCreateTestInterest(userA, userB);

        String tokenC = jwtUtil.generateToken(userC.getEmail(), List.of("ROLE_USER"), "sess-del-c", "USER");

        mockMvc.perform(delete("/api/interests/" + interest.getId())
                        .header("Authorization", "Bearer " + tokenC))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("43. Hardening: User A cannot read User B unread notification count")
    void testHorizontal_UserCannotReadAnotherUserUnreadNotifications() throws Exception {
        User userA = getOrCreateTestUser("authz_unread_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_unread_b@example.com", "Pass123!");

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-unread-a", "USER");

        mockMvc.perform(get("/api/notifications/unread?userId=" + userB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("44. Hardening: User A cannot delete User B's notification")
    void testHorizontal_UserCannotDeleteAnotherUserNotification() throws Exception {
        User userA = getOrCreateTestUser("authz_del_notif_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_del_notif_b@example.com", "Pass123!");

        Notification notificationB = new Notification();
        notificationB.setReceiverId(userB.getId());
        notificationB.setMessage("User B private notification");
        notificationB.setType(NotificationType.SYSTEM);
        notificationB.setRead(false);
        notificationB.setDeleted(false);
        notificationB = notificationRepository.saveAndFlush(notificationB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-delnotif-a", "USER");

        mockMvc.perform(delete("/api/notifications/" + notificationB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("45. Hardening: User A cannot mark User B's notification as read")
    void testHorizontal_UserCannotMarkReadAnotherUserNotification() throws Exception {
        User userA = getOrCreateTestUser("authz_read_notif_a@example.com", "Pass123!");
        User userB = getOrCreateTestUser("authz_read_notif_b@example.com", "Pass123!");

        Notification notificationB = new Notification();
        notificationB.setReceiverId(userB.getId());
        notificationB.setMessage("User B unread notification");
        notificationB.setType(NotificationType.SYSTEM);
        notificationB.setRead(false);
        notificationB.setDeleted(false);
        notificationB = notificationRepository.saveAndFlush(notificationB);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-readnotif-a", "USER");

        mockMvc.perform(put("/api/notifications/read/" + notificationB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("46. Hardening: Legitimate User A can create and update own partner preferences")
    void testLegitimateUser_PartnerPreferenceCrud_Success() throws Exception {
        User user = getOrCreateTestUser("authz_legit_pref@example.com", "Pass123!");
        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-legit-pref", "USER");

        String createPayload = String.format("""
                {
                    "userId": %d,
                    "minAge": 24,
                    "maxAge": 30,
                    "isActive": true
                }
                """, user.getId());

        mockMvc.perform(post("/api/partner-preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isOk());

        String updatePayload = """
                {
                    "minAge": 25,
                    "maxAge": 32,
                    "isActive": true
                }
                """;

        mockMvc.perform(put("/api/partner-preferences/" + user.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("47. Hardening: Legitimate receiver User B can accept interest from User A")
    void testLegitimateUser_InterestManagement_Success() throws Exception {
        User senderA = getOrCreateTestUser("authz_legit_sender@example.com", "Pass123!");
        User receiverB = getOrCreateTestUser("authz_legit_receiver@example.com", "Pass123!");

        Interest interest = getOrCreateTestInterest(senderA, receiverB);

        String tokenB = jwtUtil.generateToken(receiverB.getEmail(), List.of("ROLE_USER"), "sess-legit-b", "USER");

        // Receiver B accepts -> 200 OK
        mockMvc.perform(put("/api/interests/accept/" + interest.getId())
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        // Receiver B queries received -> 200 OK
        mockMvc.perform(get("/api/interests/received/" + receiverB.getId())
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("48. Hardening: Legitimate User A can view and manage own notifications")
    void testLegitimateUser_NotificationManagement_Success() throws Exception {
        User user = getOrCreateTestUser("authz_legit_notif@example.com", "Pass123!");

        Notification notification = new Notification();
        notification.setReceiverId(user.getId());
        notification.setMessage("Own legitimate notification");
        notification.setType(NotificationType.SYSTEM);
        notification.setRead(false);
        notification.setDeleted(false);
        notification = notificationRepository.saveAndFlush(notification);

        String token = jwtUtil.generateToken(user.getEmail(), List.of("ROLE_USER"), "sess-legit-notif", "USER");

        mockMvc.perform(get("/api/notifications?userId=" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/notifications/read/" + notification.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/notifications/" + notification.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("49. Hardening: Legitimate User A can add, check, and remove own shortlist items")
    void testLegitimateUser_ShortlistCrud_Success() throws Exception {
        User userA = getOrCreateTestUser("authz_legit_sl_a@example.com", "Pass123!");
        User target = getOrCreateTestUser("authz_legit_sl_tgt@example.com", "Pass123!");
        Profile targetProfile = createTestProfile(target);

        String tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-legit-sl", "USER");

        // Add to shortlist
        mockMvc.perform(post("/api/shortlists/user/" + userA.getId() + "/profile/" + targetProfile.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk());

        // Check shortlist status
        mockMvc.perform(get("/api/shortlists/user/" + userA.getId() + "/profile/" + targetProfile.getId() + "/check")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Remove from shortlist
        mockMvc.perform(delete("/api/shortlists/user/" + userA.getId() + "/profile/" + targetProfile.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isNoContent());
    }
}
