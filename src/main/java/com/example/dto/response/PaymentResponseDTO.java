package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;

    // User Details
    private Long userId;
    private String userName;
    private String userEmail;

    // Subscription Plan
    private Long planId;
    private String planName;

    // Payment Details
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;

    // Dates
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}