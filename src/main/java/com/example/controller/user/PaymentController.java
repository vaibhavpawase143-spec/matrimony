package com.example.controller.user; // user folder

import com.example.model.Payment;
import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create new payment
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.create(payment));
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    // Get payments by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getByUser(userId));
    }

    // Get payment by transaction ID
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentService.getByTransactionId(transactionId);
        if (payment != null) return ResponseEntity.ok(payment);
        return ResponseEntity.notFound().build();
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(paymentService.getByStatus(status));
    }

    // Get payments by user and status
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Payment>> getByUserAndStatus(@PathVariable Long userId, @PathVariable String status) {
        return ResponseEntity.ok(paymentService.getByUserAndStatus(userId, status));
    }
}