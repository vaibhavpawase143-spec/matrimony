package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatsDTO {

    private Long totalPayments;

    private Long successfulPayments;

    private Long failedPayments;

    private Long pendingPayments;

    private Long refundedPayments;

    private BigDecimal totalRevenue;
}