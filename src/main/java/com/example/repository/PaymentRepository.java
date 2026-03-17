package com.example.repository;

import com.example.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 🔍 Get payments by user
    List<Payment> findByUserId(Long userId);

    // 🔍 Get by transaction id (VERY IMPORTANT)
    Payment findByTransactionId(String transactionId);

    // 🔍 Check duplicate transaction
    boolean existsByTransactionId(String transactionId);

    // 🔍 Filter by payment status
    List<Payment> findByPaymentStatus(String status);

    // 🔍 User + status
    List<Payment> findByUserIdAndPaymentStatus(Long userId, String status);
}