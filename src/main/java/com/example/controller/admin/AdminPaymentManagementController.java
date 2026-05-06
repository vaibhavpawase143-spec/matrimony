package com.example.controller.admin;

import com.example.dto.response.ApiResponse;
import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminPaymentManagementController {

    private final PaymentRepository paymentRepository;

    // ================= GET PAYMENTS =================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return new ApiResponse<>(true, "All payments retrieved", payments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return new ApiResponse<>(true, "Payment retrieved", payment);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return new ApiResponse<>(true, "User payments retrieved", payments);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return new ApiResponse<>(true, "Payments filtered by status", payments);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return new ApiResponse<>(true, "Payment retrieved by transaction ID", payment);
    }

    // ================= PAYMENT STATISTICS =================

    @GetMapping("/stats/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> getPaymentSummary() {
        List<Payment> allPayments = paymentRepository.findAll();
        List<Payment> successPayments = paymentRepository.findByStatus("SUCCESS");
        List<Payment> failedPayments = paymentRepository.findByStatus("FAILED");
        List<Payment> pendingPayments = paymentRepository.findByStatus("PENDING");

        BigDecimal totalRevenue = successPayments.stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = Map.of(
                "totalTransactions", (long) allPayments.size(),
                "successfulTransactions", (long) successPayments.size(),
                "failedTransactions", (long) failedPayments.size(),
                "pendingTransactions", (long) pendingPayments.size(),
                "totalRevenue", totalRevenue,
                "averageTransactionAmount", allPayments.isEmpty() ? BigDecimal.ZERO : 
                    totalRevenue.divide(BigDecimal.valueOf(successPayments.size()), 2, java.math.RoundingMode.HALF_UP),
                "successRate", allPayments.isEmpty() ? 0.0 : 
                    (double) successPayments.size() / allPayments.size() * 100
        );
        return new ApiResponse<>(true, "Payment summary retrieved", stats);
    }

    @GetMapping("/stats/by-month")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> getPaymentsByMonth() {
        List<Payment> payments = paymentRepository.findAll();
        
        Map<String, Object> monthlyStats = new java.util.LinkedHashMap<>();
        payments.stream()
                .filter(p -> p.getCreatedAt() != null)
                .forEach(p -> {
                    String month = p.getCreatedAt().getYear() + "-" + 
                                  String.format("%02d", p.getCreatedAt().getMonthValue());
                    monthlyStats.computeIfPresent(month, (k, v) -> {
                        Map<String, Object> data = (Map<String, Object>) v;
                        data.put("count", (Long) data.get("count") + 1);
                        data.put("total", ((BigDecimal) data.get("total")).add(p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO));
                        return data;
                    });
                    monthlyStats.putIfAbsent(month, Map.of(
                            "count", 1L,
                            "total", p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO
                    ));
                });

        return new ApiResponse<>(true, "Monthly payment statistics retrieved", monthlyStats);
    }

    @GetMapping("/stats/by-method")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Long>> getPaymentsByMethod() {
        List<Payment> payments = paymentRepository.findAll();
        
        Map<String, Long> methodStats = new java.util.HashMap<>();
        payments.forEach(p -> {
            String method = p.getPaymentMethod() != null ? p.getPaymentMethod() : "UNKNOWN";
            methodStats.put(method, methodStats.getOrDefault(method, 0L) + 1);
        });

        return new ApiResponse<>(true, "Payment method statistics retrieved", methodStats);
    }

    // ================= PAYMENT VERIFICATION =================

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Payment> verifyPayment(@PathVariable Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus("VERIFIED");
            paymentRepository.save(payment);
            return new ApiResponse<>(true, "Payment verified successfully", payment);
        }
        throw new RuntimeException("Payment not found");
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<Payment> refundPayment(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setStatus("REFUNDED");
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            return new ApiResponse<>(true, "Payment refunded successfully", payment);
        }
        throw new RuntimeException("Payment not found");
    }

    // ================= EXPORT & REPORTS =================

    @GetMapping("/export/csv")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> exportPaymentsCSV() {
        // TODO: Implement CSV export
        return ResponseEntity.ok("CSV export coming soon");
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> exportPaymentsExcel() {
        // TODO: Implement Excel export
        return ResponseEntity.ok("Excel export coming soon");
    }
}

