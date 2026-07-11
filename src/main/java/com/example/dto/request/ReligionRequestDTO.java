package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Religion Request DTO
 * Used for creating and updating religion master data.
 * Admin ID is obtained from authenticated context, not from request.
 */
@Data
public class ReligionRequestDTO {


    @NotBlank(message = "Religion name is required")
    @Size(max = 120, message = "Name must be less than 120 characters")
    private String name;

    private Boolean isActive;
}