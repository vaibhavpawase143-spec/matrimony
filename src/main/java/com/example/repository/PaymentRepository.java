package com.example.repository;

import com.example.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByTransactionId(String transactionId);

    Optional<Payment> findByTransactionId(String transactionId);

    // ✅ By user
    List<Payment> findByUser_Id(Long userId);

    // ✅ By status
    List<Payment> findByIsActiveIgnoreCase(String isActive);

    // ✅ By user + status
    List<Payment> findByUser_IdAndIsActiveIgnoreCase(Long userId, String isActive);
}