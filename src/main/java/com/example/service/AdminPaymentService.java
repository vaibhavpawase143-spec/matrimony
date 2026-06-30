package com.example.service;

import com.example.dto.request.PaymentFilterDTO;
import com.example.dto.response.PaymentResponseDTO;
import com.example.dto.response.PaymentStatsDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface AdminPaymentService {

    Page<PaymentResponseDTO> getAllPayments(
            PaymentFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );

    PaymentResponseDTO getPaymentById(Long id);

    PaymentResponseDTO refundPayment(
            Long id,
            BigDecimal refundAmount,
            String refundReason
    );

    PaymentResponseDTO markPaymentSuccess(Long id);

    PaymentResponseDTO markPaymentFailed(
            Long id,
            String reason
    );

    PaymentStatsDTO getStatistics();
}