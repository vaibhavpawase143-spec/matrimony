package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HeightRequestDTO {

    @NotBlank(message = "Height is required")
    @Size(max = 50, message = "Height must not exceed 50 characters")
    private String height;

    @NotNull(message = "isActive is required")
    private Boolean isActive;

    @NotNull(message = "Admin ID is required")
    private Long adminId;
}