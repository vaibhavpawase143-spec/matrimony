package com.example.serviceimpl;

import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repo;

    // ✅ Create Payment
    @Override
    public Payment create(Payment payment) {

        if (repo.existsByTransactionId(payment.getTransactionId())) {
            throw new RuntimeException("Transaction already exists: " + payment.getTransactionId());
        }

        payment.setCreatedAt(LocalDateTime.now());
        // ✅ Status handled in entity (@PrePersist → PENDING)

        return repo.save(payment);
    }

    // ✅ Get all payments
    @Override
    public List<Payment> getAll() {
        return repo.findAll();
    }

    // ✅ Get by user
    @Override
    public List<Payment> getByUser(Long userId) {
        return repo.findByUser_Id(userId);
    }

    // ✅ Get by transactionId
    @Override
    public Payment getByTransactionId(String transactionId) {
        return repo.findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found: " + transactionId));
    }

    // ✅ Get by status (PENDING / SUCCESS / FAILED)
    @Override
    public List<Payment> getByisActive(String isActive) {
        return repo.findByIsActiveIgnoreCase(isActive);
    }

    // ✅ Get by user + status
    @Override
    public List<Payment> getByUserAndisActive(Long userId, String isActive) {
        return repo.findByUser_IdAndIsActiveIgnoreCase(userId, isActive);
    }
}