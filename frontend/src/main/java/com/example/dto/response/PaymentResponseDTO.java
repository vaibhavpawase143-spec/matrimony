package com.example.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}