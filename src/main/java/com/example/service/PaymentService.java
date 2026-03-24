package com.example.service;

import com.example.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Payment savePayment(Payment payment);

    Optional<Payment> getById(Long id);

    Optional<Payment> getByTransactionId(String transactionId);

    List<Payment> getAll();

    List<Payment> getByUser(Long userId);

    List<Payment> getByStatus(String status);

    List<Payment> getByUserAndStatus(Long userId, String status);

    void delete(Long id);
}