package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PartnerPreferenceRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @Positive(message = "Min age must be positive")
    private Integer minAge;

    @Positive(message = "Max age must be positive")
    private Integer maxAge;

    @Positive(message = "Min height must be positive")
    private Double minHeight;

    @Positive(message = "Max height must be positive")
    private Double maxHeight;

    private Long religionId;
    private Long casteId;
    private Long cityId;

    @NotNull(message = "isActive is required")
    private Boolean isActive;
}