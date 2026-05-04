package com.example.controller.user;

import com.example.service.RazorpayPaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
@RequiredArgsConstructor
public class RazorpayPaymentController {

    private final RazorpayPaymentService razorpayPaymentService;

    /**
     * Create Razorpay order for subscription purchase
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestParam Long userId,
            @RequestParam Long planId) {

        try {
            Map<String, Object> orderData = razorpayPaymentService.createOrder(userId, planId);
            return ResponseEntity.ok(orderData);
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Verify payment after completion
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String signature) {

        try {
            boolean isValid = razorpayPaymentService.verifyPayment(orderId, paymentId, signature);
            if (isValid) {
                return ResponseEntity.ok(Map.of("message", "Payment verified and subscription activated"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Payment verification failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Payment verification failed"));
        }
    }

    /**
     * Get payment status
     */
    @GetMapping("/payment-status/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String orderId) {
        try {
            String status = razorpayPaymentService.getPaymentStatus(orderId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get payment status"));
        }
    }
}
