package com.example.controller.user;

import com.example.model.Payment;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    private boolean isUserAdmin(User user) {
        return user != null && user.getRoles() != null && user.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));
    }

    private void validateUserOwnership(Long targetUserId, String actionDescription) {
        User currentUser = getAuthenticatedUser();
        if (!isUserAdmin(currentUser) && (currentUser.getId() == null || !currentUser.getId().equals(targetUserId))) {
            throw new AccessDeniedException("Access denied: " + actionDescription);
        }
    }

    // =========================
    // ✅ CREATE PAYMENT (ADMIN ONLY FOR DIRECT CREATION)
    // =========================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        if (payment.getUser() != null && payment.getUser().getId() != null) {
            User user = userRepository.findById(payment.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            payment.setUser(user);
        }

        return ResponseEntity.ok(paymentService.savePayment(payment));
    }

    // =========================
    // 🔍 GET PAYMENTS (SCOPED TO CURRENT USER FOR USERS, ALL FOR ADMINS)
    // =========================
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        User currentUser = getAuthenticatedUser();
        if (isUserAdmin(currentUser)) {
            return ResponseEntity.ok(paymentService.getAll());
        }
        return ResponseEntity.ok(paymentService.getByUser(currentUser.getId()));
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        Payment payment = paymentService.getById(id)
                .orElse(null);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = getAuthenticatedUser();
        if (!isUserAdmin(currentUser) && (payment.getUser() == null || !currentUser.getId().equals(payment.getUser().getId()))) {
            throw new AccessDeniedException("Access denied: You cannot view another user's payment");
        }

        return ResponseEntity.ok(payment);
    }

    // =========================
    // 🔍 GET BY USER
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getByUser(@PathVariable Long userId) {
        validateUserOwnership(userId, "You cannot view another user's payments");
        return ResponseEntity.ok(paymentService.getByUser(userId));
    }

    // =========================
    // 🔍 GET BY TRANSACTION ID
    // =========================
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentService.getByTransactionId(transactionId)
                .orElse(null);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = getAuthenticatedUser();
        if (!isUserAdmin(currentUser) && (payment.getUser() == null || !currentUser.getId().equals(payment.getUser().getId()))) {
            throw new AccessDeniedException("Access denied: You cannot view another user's payment");
        }

        return ResponseEntity.ok(payment);
    }

    // =========================
    // 🔍 GET BY STATUS (ADMIN ONLY)
    // =========================
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
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
        validateUserOwnership(userId, "You cannot view another user's payments");
        return ResponseEntity.ok(paymentService.getByUserAndStatus(userId, status));
    }

    // =========================
    // ❌ DELETE (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}