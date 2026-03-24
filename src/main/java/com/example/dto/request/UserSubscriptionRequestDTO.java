package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSubscriptionRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Plan ID is required")
    private Long planId;
}