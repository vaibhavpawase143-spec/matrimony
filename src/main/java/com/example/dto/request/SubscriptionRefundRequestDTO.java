package com.example.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionRefundRequestDTO {

    private BigDecimal refundAmount;

    private String refundReason;

}