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

    List<Payment> findByUserId(Long userId);

    List<Payment> findByStatus(String status);

    List<Payment> findByUserIdAndStatus(Long userId, String status);
}