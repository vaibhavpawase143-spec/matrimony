package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReligionRequestDTO {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotBlank(message = "Religion name is required")
    @Size(max = 120, message = "Name must be less than 120 characters")
    private String name;

    private Boolean isActive;
}