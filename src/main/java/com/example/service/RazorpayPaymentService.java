package com.example.service;

import com.example.model.Payment;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.PaymentRepository;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor

public class RazorpayPaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(RazorpayPaymentService.class);
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final RazorpayClient razorpayClient;
    @Value("${razorpay.api.key}")
    private String razorpayKey;

    @Value("${razorpay.api.secret}")
    private String razorpayKeySecret;

    /**
     * Create Razorpay order for subscription purchase
     */
    @Transactional
    public Map<String, Object> createOrder(Long planId) throws RazorpayException {
        if (planId == null) {
            throw new com.example.exception.BadRequestException("Plan ID is required");
        }

        User user = getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository.findByIdAndDeletedAtIsNull(planId)
                .orElseThrow(() -> new com.example.exception.ResourceNotFoundException("Subscription plan not found"));

        if (!Boolean.TRUE.equals(plan.getIsActive())) {
            throw new com.example.exception.BadRequestException("Selected subscription plan is currently inactive");
        }

        // Create order options
        JSONObject options = new JSONObject();
        int amountInPaisa = plan.getPrice().multiply(BigDecimal.valueOf(100)).intValue();
        options.put("amount", amountInPaisa); // Amount in paisa
        options.put("currency", "INR");
        options.put(
                "receipt",
                "order_rcpt_" + user.getId() + "_" + planId
        );
        options.put("payment_capture", 1); // Auto capture

        String orderId;
        try {
            Order order = razorpayClient.orders.create(options);
            orderId = order.get("id");
        } catch (RazorpayException e) {
            if (razorpayKey != null && razorpayKey.contains("xxxx")) {
                orderId = "order_test_" + System.currentTimeMillis() + "_" + user.getId();
                log.info("[RAZORPAY TEST MODE] Created mock test order ID: {}", orderId);
            } else {
                throw e;
            }
        }

        // Save payment record
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(plan.getPrice());
        payment.setPaymentMethod("razorpay");
        payment.setTransactionId(orderId);
        payment.setSubscriptionPlan(plan);
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        // Return order details for frontend
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("amount", amountInPaisa);
        response.put("currency", "INR");
        response.put("key", razorpayKey);
        response.put("planName", plan.getName());
        response.put("duration", plan.getDuration());

        return response;
    }

    /**
     * Verify and handle successful payment
     */
    @Transactional
    public boolean verifyPayment(
            String orderId,
            String paymentId,
            String signature
    ) {

        Payment payment = null;

        try {
            boolean verified;
            if (razorpayKeySecret != null && razorpayKeySecret.contains("xxxx") && orderId != null && orderId.startsWith("order_test_")) {
                verified = signature != null && !signature.isBlank();
                log.info("[RAZORPAY TEST MODE] Verified sandbox signature for order {}", orderId);
            } else {
                JSONObject options = new JSONObject();
                options.put("razorpay_order_id", orderId);
                options.put("razorpay_payment_id", paymentId);
                options.put("razorpay_signature", signature);

                verified = Utils.verifyPaymentSignature(options, razorpayKeySecret);
            }

            payment = paymentRepository
                    .findByTransactionId(orderId)
                    .orElseThrow(() ->
                            new com.example.exception.ResourceNotFoundException("Payment not found"));

            if (!verified) {
                payment.setStatus("FAILED");
                paymentRepository.save(payment);
                return false;
            }

            User currentUser = getCurrentUser();

            if (!payment.getUser().getId().equals(currentUser.getId())) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Unauthorized payment verification"
                );
            }

            // Prevent duplicate activation
            if ("SUCCESS".equals(payment.getStatus())) {
                return true;
            }

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            notificationService.createAdminNotification(
                    "Payment Received",
                    "Payment of ₹" + payment.getAmount() + " received from " + payment.getUser().getFullName() + ".",
                    com.example.model.NotificationType.SUBSCRIPTION
            );

            UserSubscription subscription = subscriptionService.activateSubscription(
                    payment.getUser(),
                    payment.getSubscriptionPlan()
            );

            // Send purchase confirmation email
            try {
                emailService.sendSubscriptionPurchasedEmail(
                        payment.getUser().getEmail(),
                        payment.getUser().getFirstName(),
                        payment.getSubscriptionPlan().getName(),
                        payment.getAmount(),
                        subscription != null ? subscription.getEndDate() : null
                );
            } catch (Exception emailEx) {
                log.error("Failed to send subscription purchase email to {}: {}", payment.getUser().getEmail(), emailEx.getMessage());
            }

            log.info(
                    "Payment {} verified successfully for user {}",
                    paymentId,
                    payment.getUser().getEmail()
            );

            return true;

        }

        catch (Exception e) {

            log.error(
                    "Payment verification failed",
                    e
            );

            if (payment != null &&
                    !"SUCCESS".equals(payment.getStatus())) {

                payment.setStatus("FAILED");

                paymentRepository.save(payment);

                notificationService.createAdminNotification(
                        "Payment Failed",
                        "Payment failed for " + payment.getUser().getFullName() + ".",
                        com.example.model.NotificationType.WARNING
                );

                notificationService.createSubscriptionReminder(
                        payment.getUser().getId(),
                        null,
                        "Payment Failed",
                        "Your payment of ₹" + payment.getAmount() + " could not be completed."
                );
            }

            return false;

        }

    }
    /**
     * Create user subscription after successful payment
     */

    /**
     * Get payment status
     */
    public String getPaymentStatus(String orderId) {
        try {
            Payment payment = paymentRepository.findByTransactionId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            User currentUser = getCurrentUser();
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            if (!isAdmin && (payment.getUser() == null || !currentUser.getId().equals(payment.getUser().getId()))) {
                throw new org.springframework.security.access.AccessDeniedException("Access denied: You cannot view status of another user's order");
            }

            return payment.getStatus();
        } catch (org.springframework.security.access.AccessDeniedException ade) {
            throw ade;
        } catch (Exception e) {
            log.error(
                    "Unable to fetch payment status",
                    e
            );

            return "NOT_FOUND";
        }
    }
    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}
