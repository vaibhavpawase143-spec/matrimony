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
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Long planId) {

        Long finalPlanId = planId;
        if (finalPlanId == null && body != null && body.get("planId") != null) {
            try {
                finalPlanId = Long.valueOf(body.get("planId").toString());
            } catch (NumberFormatException ignored) {}
        }

        if (finalPlanId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "planId is required"));
        }

        try {
            Map<String, Object> orderData =
                    razorpayPaymentService.createOrder(finalPlanId);

            return ResponseEntity.ok(orderData);

        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (com.example.exception.ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(
                    Map.of("error", e.getMessage())
            );
        } catch (com.example.exception.BadRequestException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * Verify payment after completion
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @RequestBody(required = false) Map<String, String> body,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String paymentId,
            @RequestParam(required = false) String signature) {

        String finalOrderId = orderId != null ? orderId : (body != null ? body.get("orderId") : null);
        String finalPaymentId = paymentId != null ? paymentId : (body != null ? body.get("paymentId") : null);
        String finalSignature = signature != null ? signature : (body != null ? body.get("signature") : null);

        if (finalOrderId == null || finalPaymentId == null || finalSignature == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Missing required parameters: orderId, paymentId, and signature are required")
            );
        }

        try {
            boolean isValid = razorpayPaymentService.verifyPayment(finalOrderId, finalPaymentId, finalSignature);
            if (isValid) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Payment verified and subscription activated"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Payment verification failed"));
            }
        } catch (org.springframework.security.access.AccessDeniedException ade) {
            return ResponseEntity.status(403).body(Map.of("error", ade.getMessage()));
        } catch (com.example.exception.ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Payment verification failed: " + e.getMessage()));
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
        } catch (org.springframework.security.access.AccessDeniedException ade) {
            return ResponseEntity.status(403).body(Map.of("error", ade.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get payment status"));
        }
    }
}
