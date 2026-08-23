package com.example.service;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Payment;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.PaymentRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSubscriptionRepository;
import com.example.serviceimpl.ProfilePremiumSyncServiceImpl;
import com.example.serviceimpl.SubscriptionServiceImpl;
import com.example.serviceimpl.UserDetailsServiceImpl;
import com.razorpay.RazorpayClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubscriptionSecurityAndDataIntegrityTest {

    @Mock
    private UserSubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionPlanRepository planRepository;

    @Mock
    private ProfilePremiumSyncService profilePremiumSyncService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private RazorpayClient razorpayClient;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @InjectMocks
    private ProfilePremiumSyncServiceImpl profilePremiumSyncServiceImpl;

    private User testUser;
    private SubscriptionPlan paidPlan;
    private SubscriptionPlan freePlan;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(101L);
        testUser.setEmail("rahul@example.com");
        testUser.setFirstName("Rahul");
        testUser.setLastName("Sharma");

        paidPlan = SubscriptionPlan.builder()
                .id(1L)
                .name("Premium 3 Months")
                .price(BigDecimal.valueOf(1199.00))
                .duration(90)
                .isActive(true)
                .build();

        freePlan = SubscriptionPlan.builder()
                .id(2L)
                .name("Free")
                .price(BigDecimal.ZERO)
                .duration(0)
                .isActive(true)
                .build();
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("ISSUE-1: Direct activation of paid plans must throw BadRequestException")
    void testPaidPlanDirectActivationRejected() {
        mockSecurityContext("rahul@example.com");
        when(userRepository.findByEmailIgnoreCase("rahul@example.com")).thenReturn(Optional.of(testUser));
        when(planRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(paidPlan));

        UserSubscriptionRequestDTO request = new UserSubscriptionRequestDTO();
        request.setPlanId(1L);

        assertThrows(BadRequestException.class, () -> {
            subscriptionService.subscribeUser(request);
        });

        verify(subscriptionRepository, never()).save(any(UserSubscription.class));
    }

    @Test
    @DisplayName("ISSUE-3: ProfilePremiumSyncService handles missing profile gracefully without exception")
    void testProfileSyncHandlesMissingProfileGracefully() {
        when(profileRepository.findByUserId(101L)).thenReturn(Optional.empty());

        UserSubscription sub = UserSubscription.builder()
                .id(50L)
                .user(testUser)
                .subscriptionPlan(paidPlan)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(90))
                .isActive(true)
                .status("ACTIVE")
                .build();

        // Must execute cleanly without throwing ResourceNotFoundException
        assertDoesNotThrow(() -> {
            profilePremiumSyncServiceImpl.sync(testUser, sub);
        });

        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("ISSUE-4 & ISSUE-7: Inactive plan activation is rejected")
    void testInactivePlanRejected() {
        mockSecurityContext("rahul@example.com");
        when(userRepository.findByEmailIgnoreCase("rahul@example.com")).thenReturn(Optional.of(testUser));

        SubscriptionPlan inactivePlan = SubscriptionPlan.builder()
                .id(3L)
                .name("Old Inactive Plan")
                .price(BigDecimal.ZERO)
                .duration(30)
                .isActive(false)
                .build();

        when(planRepository.findByIdAndDeletedAtIsNull(3L)).thenReturn(Optional.of(inactivePlan));

        UserSubscriptionRequestDTO request = new UserSubscriptionRequestDTO();
        request.setPlanId(3L);

        assertThrows(BadRequestException.class, () -> {
            subscriptionService.subscribeUser(request);
        });
    }

    @Test
    @DisplayName("ISSUE-8: Safe duration mapping on active subscription")
    void testSafeDurationMapping() {
        com.example.model.Profile profile = new com.example.model.Profile();
        profile.setId(201L);
        profile.setUser(testUser);

        when(profileRepository.findByUserId(101L)).thenReturn(Optional.of(profile));

        UserSubscription sub = UserSubscription.builder()
                .id(51L)
                .user(testUser)
                .subscriptionPlan(paidPlan) // duration = 90
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(90))
                .isActive(true)
                .status("ACTIVE")
                .build();

        profilePremiumSyncServiceImpl.sync(testUser, sub);

        assertTrue(profile.getIsPremium());
        assertEquals(com.example.model.PremiumPlan.THREE_MONTHS, profile.getPremiumPlan());
        verify(profileRepository, times(1)).save(profile);
    }

    @Mock
    private org.springframework.core.env.Environment environment;

    @Test
    @DisplayName("ISSUE-4: RazorpayPaymentService rejects order creation for inactive or deleted plans")
    void testRazorpayRejectsInactivePlan() {
        mockSecurityContext("rahul@example.com");
        when(userRepository.findByEmailIgnoreCase("rahul@example.com")).thenReturn(Optional.of(testUser));

        SubscriptionPlan inactivePlan = SubscriptionPlan.builder()
                .id(10L)
                .name("Inactive Plan")
                .price(BigDecimal.valueOf(999))
                .duration(30)
                .isActive(false)
                .build();

        when(planRepository.findByIdAndDeletedAtIsNull(10L)).thenReturn(Optional.of(inactivePlan));

        RazorpayPaymentService razorpayService = new RazorpayPaymentService(
                userRepository,
                paymentRepository,
                planRepository,
                subscriptionService,
                notificationService,
                emailService,
                razorpayClient,
                environment
        );

        assertThrows(BadRequestException.class, () -> {
            razorpayService.createOrder(10L);
        });
    }

    @Test
    @DisplayName("AUDIT-1: Mock/Sandbox is strictly prohibited in production environments")
    void testSandboxProhibitedInProductionEnvironment() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        RazorpayPaymentService razorpayService = new RazorpayPaymentService(
                userRepository,
                paymentRepository,
                planRepository,
                subscriptionService,
                notificationService,
                emailService,
                razorpayClient,
                environment
        );

        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setAmount(BigDecimal.valueOf(999));
        payment.setTransactionId("order_test_12345");
        payment.setStatus("PENDING");

        when(paymentRepository.findByTransactionId("order_test_12345")).thenReturn(Optional.of(payment));

        // In production profile, signature verification will execute real verification and fail for dummy signature
        assertFalse(razorpayService.verifyPayment("order_test_12345", "pay_test_999", "dummy_sig"));
    }

    @Test
    @DisplayName("AUDIT-2: Payment verification is strictly idempotent on duplicate callbacks")
    void testVerifyPaymentIdempotency() {
        mockSecurityContext("rahul@example.com");
        when(userRepository.findByEmailIgnoreCase("rahul@example.com")).thenReturn(Optional.of(testUser));

        Payment payment = new Payment();
        payment.setUser(testUser);
        payment.setAmount(BigDecimal.valueOf(999));
        payment.setTransactionId("order_LVn395nxjsa8");
        payment.setStatus("SUCCESS"); // Already marked success previously

        when(paymentRepository.findByTransactionId("order_LVn395nxjsa8")).thenReturn(Optional.of(payment));

        SubscriptionService mockSubService = mock(SubscriptionService.class);

        RazorpayPaymentService razorpayService = new RazorpayPaymentService(
                userRepository,
                paymentRepository,
                planRepository,
                mockSubService,
                notificationService,
                emailService,
                razorpayClient,
                environment
        );

        // Verification returns true idempotently without reactivating subscription or sending duplicate notifications
        boolean result = razorpayService.verifyPayment("order_LVn395nxjsa8", "pay_8888", "valid_sig");

        assertTrue(result);
        verify(mockSubService, never()).activateSubscription(any(), any());
        verify(notificationService, never()).createAdminNotification(any(), any(), any());
    }
}
