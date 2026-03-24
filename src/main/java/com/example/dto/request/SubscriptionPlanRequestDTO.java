package com.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanRequestDTO {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotBlank(message = "Plan name is required")
    @Size(max = 100)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer duration;

    @Size(max = 500)
    private String description;

    private Boolean isActive;
}