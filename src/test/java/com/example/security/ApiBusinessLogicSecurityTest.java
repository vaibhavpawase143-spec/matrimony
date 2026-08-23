package com.example.security;

import com.example.config.BaseIntegrationTest;
import com.example.dto.request.InterestRequestDTO;
import com.example.dto.request.SendMessageRequestDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;
import com.example.service.InterestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Task 10 — API & Business Logic Security Hardening Test Suite")
public class ApiBusinessLogicSecurityTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserBlockRepository userBlockRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private InterestService interestService;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ShortlistRepository shortlistRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userA;
    private User userB;
    private User userC;
    private Admin admin;

    private String tokenA;
    private String tokenB;
    private String tokenC;
    private String adminToken;

    private SubscriptionPlan getOrCreateSubscriptionPlan() {
        return subscriptionPlanRepository.findAll().stream().findFirst().orElseGet(() -> {
            SubscriptionPlan plan = new SubscriptionPlan();
            plan.setName("Gold Plan Test " + UUID.randomUUID());
            plan.setAdmin(admin);
            plan.setPrice(new BigDecimal("999.00"));
            plan.setDuration(90);
            plan.setIsActive(true);
            return subscriptionPlanRepository.saveAndFlush(plan);
        });
    }

    private Role getOrCreateRole(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role r = new Role();
            r.setName(name);
            r.setIsActive(true);
            return roleRepository.save(r);
        });
    }

    private User createUniqueUser(String prefix, String sessionId) {
        String email = prefix + "_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String phone = "77" + (System.currentTimeMillis() % 100000000L);
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode("Password@123"));
        user.setFirstName("First" + prefix);
        user.setLastName("Last" + prefix);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setSessionId(sessionId);
        user.setRoles(java.util.Set.of(getOrCreateRole("ROLE_USER")));
        user = userRepository.saveAndFlush(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setIsActive(true);
        profile.setIsDeleted(false);
        profile.setIsPremium(false);
        profileRepository.saveAndFlush(profile);

        return user;
    }

    private Admin createUniqueAdmin(String sessionId) {
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        Admin a = new Admin();
        a.setEmail("admin_task10_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        a.setUsername("adm_" + UUID.randomUUID().toString().substring(0, 8));
        a.setPassword(passwordEncoder.encode("Admin@123"));
        a.setName("Task10 Admin");
        a.setRole(adminRole);
        a.setSessionId(sessionId);
        a.setIsActive(true);
        return adminRepository.saveAndFlush(a);
    }

    @BeforeEach
    void setUp() {
        userA = createUniqueUser("userA", "sess-a");
        userB = createUniqueUser("userB", "sess-b");
        userC = createUniqueUser("userC", "sess-c");
        admin = createUniqueAdmin("sess-adm");

        tokenA = jwtUtil.generateToken(userA.getEmail(), List.of("ROLE_USER"), "sess-a", "USER");
        tokenB = jwtUtil.generateToken(userB.getEmail(), List.of("ROLE_USER"), "sess-b", "USER");
        tokenC = jwtUtil.generateToken(userC.getEmail(), List.of("ROLE_USER"), "sess-c", "USER");
        adminToken = jwtUtil.generateToken(admin.getEmail(), List.of("ROLE_ADMIN"), "sess-adm", "ADMIN");
    }

    // =========================================================================
    // 1. FREE PREMIUM ACTIVATION BYPASS (VULN-AUTHZ-01)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-01: Normal user cannot directly activate premium on profile")
    void testNormalUserCannotDirectlyActivatePremiumOnProfile() throws Exception {
        mockMvc.perform(post("/api/profiles/premium/activate")
                        .header("Authorization", "Bearer " + tokenA)
                        .param("userId", userA.getId().toString())
                        .param("plan", "THREE_MONTHS"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("VULN-AUTHZ-01: Admin is authorized to activate premium on profile")
    void testAdminCanActivatePremiumOnProfile() throws Exception {
        mockMvc.perform(post("/api/profiles/premium/activate")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("userId", userA.getId().toString())
                        .param("plan", "THREE_MONTHS"))
                .andExpect(status().isOk());

        Profile updated = profileRepository.findByUserId(userA.getId()).orElseThrow();
        assertTrue(updated.getIsPremium(), "Admin premium activation should set isPremium=true");
    }

    // =========================================================================
    // 2. PAYMENT ADMINISTRATION & IDOR PROTECTION (VULN-AUTHZ-02)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-02: Normal user GET /api/payments returns ONLY their own payments, not all platform payments")
    void testNormalUserCannotViewAllPayments() throws Exception {
        // Create payment for User A
        Payment payA = new Payment();
        payA.setUser(userA);
        payA.setAmount(new BigDecimal("499.00"));
        payA.setPaymentMethod("RAZORPAY");
        payA.setTransactionId("TXN_A_" + UUID.randomUUID());
        payA.setStatus("SUCCESS");
        paymentRepository.saveAndFlush(payA);

        // Create payment for User B
        Payment payB = new Payment();
        payB.setUser(userB);
        payB.setAmount(new BigDecimal("999.00"));
        payB.setPaymentMethod("RAZORPAY");
        payB.setTransactionId("TXN_B_" + UUID.randomUUID());
        payB.setStatus("SUCCESS");
        paymentRepository.saveAndFlush(payB);

        // User A querying /api/payments must receive only their own payment
        mockMvc.perform(get("/api/payments")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + payB.getId() + ")]").doesNotExist())
                .andExpect(jsonPath("$[?(@.id == " + payA.getId() + ")]").exists());
    }

    @Test
    @DisplayName("VULN-AUTHZ-02: Normal user cannot view another user's payment by ID (IDOR)")
    void testNormalUserCannotViewAnotherUserPaymentById() throws Exception {
        Payment payB = new Payment();
        payB.setUser(userB);
        payB.setAmount(new BigDecimal("999.00"));
        payB.setPaymentMethod("RAZORPAY");
        payB.setTransactionId("TXN_B_" + UUID.randomUUID());
        payB.setStatus("SUCCESS");
        payB = paymentRepository.saveAndFlush(payB);

        mockMvc.perform(get("/api/payments/" + payB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("VULN-AUTHZ-02: Normal user cannot delete payments or create unverified payments directly")
    void testNormalUserCannotDeleteOrDirectlyCreatePayment() throws Exception {
        Payment payA = new Payment();
        payA.setUser(userA);
        payA.setAmount(new BigDecimal("499.00"));
        payA.setPaymentMethod("RAZORPAY");
        payA.setTransactionId("TXN_DEL_" + UUID.randomUUID());
        payA.setStatus("SUCCESS");
        payA = paymentRepository.saveAndFlush(payA);

        // Normal user delete attempt
        mockMvc.perform(delete("/api/payments/" + payA.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());

        // Normal user direct create attempt
        mockMvc.perform(post("/api/payments")
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 999.00, \"status\": \"SUCCESS\"}"))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // 3. USER REPORT ADMIN PROTECTION (VULN-AUTHZ-03)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-03: Normal user cannot access administrative report endpoints")
    void testNormalUserCannotAccessAdminReportEndpoints() throws Exception {
        mockMvc.perform(get("/api/report/all")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/report/pending")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/report/review/1")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/report/unblock/1")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // 4. USER BLOCK BOLA / IDOR PROTECTION (VULN-AUTHZ-04)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-04: User cannot block or unblock on behalf of another user")
    void testUserCannotBlockOnBehalfOfAnotherUser() throws Exception {
        // User A attempts to block User C using User B's ID as blockerId
        mockMvc.perform(post("/api/block")
                        .header("Authorization", "Bearer " + tokenA)
                        .param("blockerId", userB.getId().toString())
                        .param("blockedId", userC.getId().toString()))
                .andExpect(status().isForbidden());

        // User A attempts to view User B's blocked users
        mockMvc.perform(get("/api/block/my-blocked-users")
                        .header("Authorization", "Bearer " + tokenA)
                        .param("blockerId", userB.getId().toString()))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // 5. SHORTLIST GLOBAL DUMP & ENUMERATION PROTECTION (VULN-AUTHZ-05)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-05: Normal user cannot dump all platform shortlists")
    void testNormalUserCannotDumpAllShortlists() throws Exception {
        mockMvc.perform(get("/api/shortlists")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("VULN-AUTHZ-05: Normal user cannot view who shortlisted another user's profile")
    void testNormalUserCannotViewWhoShortlistedAnotherUserProfile() throws Exception {
        Profile profileB = profileRepository.findByUserId(userB.getId()).orElseThrow();

        mockMvc.perform(get("/api/shortlists/profile/" + profileB.getId())
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // 6. CHAT MESSAGE MANIPULATION & DELETION IDOR PROTECTION (VULN-AUTHZ-06)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-06: Non-participant user cannot delete or tamper with messages in another conversation")
    void testNonParticipantCannotDeleteOrTamperWithMessage() throws Exception {
        // Create active subscription for User A so chat service allows sending
        UserSubscription sub = new UserSubscription();
        sub.setUser(userA);
        sub.setSubscriptionPlan(getOrCreateSubscriptionPlan());
        sub.setIsActive(true);
        sub.setStatus("ACTIVE");
        sub.setStartDate(LocalDateTime.now().minusDays(1));
        sub.setEndDate(LocalDateTime.now().plusMonths(3));
        userSubscriptionRepository.saveAndFlush(sub);

        // Create match between User A and User B so chat is permitted
        Match match = new Match();
        match.setUsers(userA, userB);
        matchRepository.saveAndFlush(match);

        // Send message from User A to User B
        Message msg = chatService.sendMessageByEmail(userA.getEmail(), userB.getId(), "Secret conversation", null);

        // Non-participant User C tries to delete message
        mockMvc.perform(delete("/api/chat/message/" + msg.getId())
                        .header("Authorization", "Bearer " + tokenC))
                .andExpect(status().isForbidden());

        // Non-participant User C tries to pin message
        mockMvc.perform(put("/api/chat/messages/" + msg.getId() + "/pin")
                        .header("Authorization", "Bearer " + tokenC))
                .andExpect(status().isForbidden());

        // Non-participant User C tries to react to message
        mockMvc.perform(put("/api/chat/reaction")
                        .header("Authorization", "Bearer " + tokenC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"messageId\": " + msg.getId() + ", \"reaction\": \"❤️\"}"))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // 7. INTEREST STATE MACHINE TRANSITION INTEGRITY (VULN-AUTHZ-07)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-07: Cannot reject or re-accept an already ACCEPTED interest")
    void testCannotAlterAlreadyFinalizedInterest() throws Exception {
        // Send interest from User A to User B
        String createJson = "{\"senderId\": " + userA.getId() + ", \"receiverId\": " + userB.getId() + "}";
        String resStr = mockMvc.perform(post("/api/interests/send")
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        com.fasterxml.jackson.databind.JsonNode rootNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(resStr);
        Long interestId = rootNode.get("id").asLong();

        // Receiver User B accepts interest -> transitions to ACCEPTED
        mockMvc.perform(put("/api/interests/accept/" + interestId)
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk());

        // Attempting to reject an already ACCEPTED interest must return 400 Bad Request
        mockMvc.perform(put("/api/interests/reject/" + interestId)
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isBadRequest());
    }

    // =========================================================================
    // 8. NOTIFICATION TEST ENDPOINT PROTECTION (VULN-AUTHZ-09)
    // =========================================================================

    @Test
    @DisplayName("VULN-AUTHZ-09: Normal user cannot trigger test notification creation")
    void testNormalUserCannotTriggerTestNotification() throws Exception {
        mockMvc.perform(post("/api/notifications/test")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isForbidden());
    }
}
