package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionPlanResponseDTO {

    private Long id;
    private Long adminId;
    private String adminName;

    private String name;
    private BigDecimal price;
    private Integer duration;
    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}