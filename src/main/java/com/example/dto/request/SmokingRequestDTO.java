package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SmokingRequestDTO {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotBlank(message = "Value is required")
    @Size(max = 50, message = "Value must be less than 50 characters")
    private String value;

    private Boolean isActive;
}