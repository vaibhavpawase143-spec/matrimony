package com.example.serviceimpl;

import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import com.example.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    public PaymentServiceImpl(PaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment savePayment(Payment payment) {

        Optional<Payment> existing = repository.findByTransactionId(payment.getTransactionId());

        if (existing.isPresent()) {
            Payment existingPayment = existing.get();

            existingPayment.setStatus(payment.getStatus());
            existingPayment.setAmount(payment.getAmount());
            existingPayment.setPaymentMethod(payment.getPaymentMethod());

            return repository.save(existingPayment);
        }

        return repository.save(payment);
    }

    @Override
    public Optional<Payment> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Payment> getByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    @Override
    public List<Payment> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Payment> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Payment> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Payment> getByUserAndStatus(Long userId, String status) {
        return repository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}