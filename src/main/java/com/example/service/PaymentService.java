package com.example.service;

import com.example.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment create(Payment payment);

    List<Payment> getAll();

    List<Payment> getByUser(Long userId);

    Payment getByTransactionId(String transactionId);

    List<Payment> getByStatus(String status);

    List<Payment> getByUserAndStatus(Long userId, String status);
}