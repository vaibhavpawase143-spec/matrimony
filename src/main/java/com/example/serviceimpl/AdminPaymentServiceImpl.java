package com.example.serviceimpl;

import com.example.dto.request.PaymentFilterDTO;
import com.example.dto.response.PaymentResponseDTO;
import com.example.dto.response.PaymentStatsDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminPaymentService;
import com.example.service.CurrentAdminService;
import com.example.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl
        implements AdminPaymentService {

    private final PaymentRepository repository;

    private final CurrentAdminService currentAdminService;

    private final AdminAuditLogService auditLogService;

    @Override
    public Page<PaymentResponseDTO> getAllPayments(
            PaymentFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return repository.findAll(
                PaymentSpecification.getPayments(filter),
                pageable
        ).map(this::mapToResponse);
    }
    private PaymentResponseDTO mapToResponse(Payment payment) {

        return PaymentResponseDTO.builder()
                .id(payment.getId())

                // User Details
                .userId(
                        payment.getUser() != null
                                ? payment.getUser().getId()
                                : null
                )
                .userName(
                        payment.getUser() != null
                                ? payment.getUser().getFullName()
                                : null
                )
                .userEmail(
                        payment.getUser() != null
                                ? payment.getUser().getEmail()
                                : null
                )

                // Plan Details
                .planId(
                        payment.getSubscriptionPlan() != null
                                ? payment.getSubscriptionPlan().getId()
                                : null
                )
                .planName(
                        payment.getSubscriptionPlan() != null
                                ? payment.getSubscriptionPlan().getName()
                                : null
                )

                // Payment Details
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())

                // Audit Fields
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())

                .build();
    }
    @Override
    public PaymentResponseDTO getPaymentById(Long id) {

        Payment payment = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        return mapToResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO refundPayment(
            Long id,
            BigDecimal refundAmount,
            String refundReason
    ) {

        Payment payment = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if ("REFUNDED".equalsIgnoreCase(payment.getStatus())) {
            throw new BadRequestException("Payment is already refunded.");
        }

        String oldStatus = payment.getStatus();

        payment.setStatus("REFUNDED");
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = repository.save(payment);

        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "PAYMENT_MANAGEMENT",
                "PAYMENT_REFUNDED",
                "PAYMENT",
                saved.getId(),
                "Refunded payment of user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus,
                "Status=REFUNDED, Refund Amount=" + refundAmount +
                        ", Reason=" + refundReason,
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO markPaymentSuccess(Long id) {

        Payment payment = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
            throw new BadRequestException("Payment is already marked as SUCCESS.");
        }

        String oldStatus = payment.getStatus();

        payment.setStatus("SUCCESS");
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = repository.save(payment);

        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "PAYMENT_MANAGEMENT",
                "PAYMENT_MARKED_SUCCESS",
                "PAYMENT",
                saved.getId(),
                "Marked payment as SUCCESS for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus,
                "Status=SUCCESS",
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public PaymentResponseDTO markPaymentFailed(
            Long id,
            String reason
    ) {

        Payment payment = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if ("FAILED".equalsIgnoreCase(payment.getStatus())) {
            throw new BadRequestException("Payment is already marked as FAILED.");
        }

        String oldStatus = payment.getStatus();

        payment.setStatus("FAILED");
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = repository.save(payment);

        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "PAYMENT_MANAGEMENT",
                "PAYMENT_MARKED_FAILED",
                "PAYMENT",
                saved.getId(),
                "Marked payment as FAILED for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus,
                "Status=FAILED, Reason=" + reason,
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }

    @Override
    public PaymentStatsDTO getStatistics() {

        return PaymentStatsDTO.builder()
                .totalPayments(repository.count())
                .successfulPayments(repository.countByStatus("SUCCESS"))
                .failedPayments(repository.countByStatus("FAILED"))
                .pendingPayments(repository.countByStatus("PENDING"))
                .refundedPayments(repository.countByStatus("REFUNDED"))
                .totalRevenue(repository.getTotalRevenue())
                .build();
    }
}