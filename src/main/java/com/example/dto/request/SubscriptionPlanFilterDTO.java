package com.example.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanFilterDTO {

    private String keyword;

    private Boolean isActive;

    private Long adminId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer minDuration;

    private Integer maxDuration;
}