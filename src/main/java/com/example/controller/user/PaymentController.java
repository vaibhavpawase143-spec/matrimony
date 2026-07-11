package com.example.controller.user;

import com.example.model.Payment;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    // =========================
    // ✅ CREATE PAYMENT
    // =========================
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {

        // 🔥 Ensure user exists (IMPORTANT)
        if (payment.getUser() != null && payment.getUser().getId() != null) {
            User user = userRepository.findById(payment.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            payment.setUser(user);
        }

        return ResponseEntity.ok(paymentService.savePayment(payment));
    }

    // =========================
    // 🔍 GET ALL
    // =========================
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return paymentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // 🔍 GET BY USER
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getByUser(userId));
    }

    // =========================
    // 🔍 GET BY TRANSACTION ID
    // =========================
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getByTransactionId(@PathVariable String transactionId) {
        return paymentService.getByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // 🔍 GET BY STATUS (✅ FIXED)
    // =========================
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(paymentService.getByStatus(status));
    }

    // =========================
    // 🔍 GET BY USER + STATUS
    // =========================
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Payment>> getByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {

        return ResponseEntity.ok(paymentService.getByUserAndStatus(userId, status));
    }

    // =========================
    // ❌ DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}