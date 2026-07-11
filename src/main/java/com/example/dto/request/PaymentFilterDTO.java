package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFilterDTO {

    // Search by user name, email or transaction id
    private String search;

    // SUCCESS, FAILED, PENDING, REFUNDED
    private String status;

    // Razorpay, UPI, Card, NetBanking...
    private String paymentMethod;

    // Subscription Plan
    private Long planId;

    // Amount Range
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // Date Filter
    private LocalDate fromDate;
    private LocalDate toDate;
}