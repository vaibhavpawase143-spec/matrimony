package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PartnerPreferenceRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String otherExpectations;

    @Positive(message = "Min age must be positive")
    private Integer minAge;

    @Positive(message = "Max age must be positive")
    private Integer maxAge;

    @Positive(message = "Min height must be positive")
    private Long minHeight;

    @Positive(message = "Max height must be positive")
    private Long maxHeight;

    // NEW
    private Long minWeight;

    private Long maxWeight;

    private Long religionId;

    private Long casteId;

    private Long cityId;

    private Long educationLevelId;

    private Long occupationId;

    private Long maritalStatusId;

    private Long smokingId;

    private Long drinkingId;

    private Long dietId;

    @NotNull(message = "isActive is required")
    private Boolean isActive;
}