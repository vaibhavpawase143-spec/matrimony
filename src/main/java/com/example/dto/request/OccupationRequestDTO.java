package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OccupationRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    @NotNull(message = "isActive is required")
    private Boolean isActive;

    @NotNull(message = "Admin ID is required")
    private Long adminId;
}