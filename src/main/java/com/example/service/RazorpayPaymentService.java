package com.example.service;

import com.example.model.Payment;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.repository.PaymentRepository;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSubscriptionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final RazorpayClient razorpayClient;

    /**
     * Create Razorpay order for subscription purchase
     */
    @Transactional
    public Map<String, Object> createOrder(Long userId, Long planId) throws RazorpayException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Create order options
        JSONObject options = new JSONObject();
        options.put("amount", plan.getPrice().multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paisa
        options.put("currency", "INR");
        options.put("receipt", "order_rcpt_" + userId + "_" + planId);
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
        response.put("key", "your_razorpay_key_id"); // You'll need to pass this from frontend or config

        return response;
    }

    /**
     * Verify and handle successful payment
     */
    @Transactional
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        try {
            // For now, we'll implement basic verification
            // In production, you should verify the signature properly
            // You can use Razorpay's webhook for more secure verification

            // Update payment status
            Payment payment = paymentRepository.findByTransactionId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            // Create user subscription
            createUserSubscription(payment);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Create user subscription after successful payment
     */
    private void createUserSubscription(Payment payment) {
        // Deactivate existing subscription if any
        Optional<UserSubscription> existingSub = userSubscriptionRepository
                .findByUserIdAndIsActiveTrue(payment.getUser().getId());

        if (existingSub.isPresent()) {
            UserSubscription sub = existingSub.get();
            sub.setIsActive(false);
            sub.setStatus("EXPIRED");
            userSubscriptionRepository.save(sub);
        }

        // Create new subscription
        UserSubscription newSub = new UserSubscription();
        newSub.setUser(payment.getUser());
        newSub.setSubscriptionPlan(payment.getSubscriptionPlan());
        newSub.setStartDate(LocalDateTime.now());
        newSub.setEndDate(LocalDateTime.now().plusDays(payment.getSubscriptionPlan().getDuration()));
        newSub.setIsActive(true);
        newSub.setStatus("ACTIVE");
        userSubscriptionRepository.save(newSub);
    }

    /**
     * Get payment status
     */
    public String getPaymentStatus(String orderId) {
        try {
            Payment payment = paymentRepository.findByTransactionId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            return payment.getStatus();
        } catch (Exception e) {
            return "NOT_FOUND";
        }
    }
}
