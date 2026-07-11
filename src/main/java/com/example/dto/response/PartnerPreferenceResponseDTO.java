package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartnerPreferenceResponseDTO {

    private Long id;

    private Long userId;

    private Integer minAge;

    private Integer maxAge;

    private Long minHeight;

    private Long maxHeight;

    // NEW
    private Long minWeight;

    private Long maxWeight;

    private Long religionId;

    private Long casteId;

    private Long cityId;

    private Long educationLevelId;

    private String otherExpectations;

    private Long occupationId;

    private Long maritalStatusId;

    private Long smokingId;

    private Long drinkingId;

    private Long dietId;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}