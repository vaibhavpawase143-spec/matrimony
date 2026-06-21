package com.example.service;

import com.example.model.Payment;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
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
        User user = getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Create order options
        JSONObject options = new JSONObject();
        options.put("amount", plan.getPrice().multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paisa
        options.put("currency", "INR");
        options.put(
                "receipt",
                "order_rcpt_" + user.getId() + "_" + planId
        );
        options.put("payment_capture", 1); // Auto capture

        // Create the order
        Order order = razorpayClient.orders.create(options);

        // Save payment record
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(plan.getPrice());
        payment.setPaymentMethod("razorpay");
        payment.setTransactionId(order.get("id"));
        payment.setSubscriptionPlan(plan);
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        // Return order details for frontend
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("key", razorpayKey);  // You'll need to pass this from frontend or config

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

            JSONObject options = new JSONObject();

            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean verified =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpayKeySecret
                    );

            payment = paymentRepository
                    .findByTransactionId(orderId)
                    .orElseThrow(() ->
                            new RuntimeException("Payment not found"));

            if (!verified) {

                payment.setStatus("FAILED");
                paymentRepository.save(payment);

                return false;
            }

            User currentUser = getCurrentUser();

            if (!payment.getUser().getId().equals(currentUser.getId())) {

                throw new RuntimeException(
                        "Unauthorized payment verification"
                );
            }

            // Prevent duplicate activation
            if ("SUCCESS".equals(payment.getStatus())) {

                return true;

            }

            payment.setStatus("SUCCESS");

            paymentRepository.save(payment);

            subscriptionService.activateSubscription(
                    payment.getUser(),
                    payment.getSubscriptionPlan()
            );

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
            return payment.getStatus();
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
