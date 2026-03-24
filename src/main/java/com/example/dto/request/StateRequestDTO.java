package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StateRequestDTO {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotNull(message = "Country ID is required")
    private Long countryId;

    @NotBlank(message = "State name is required")
    @Size(max = 120, message = "Name must be less than 120 characters")
    private String name;

    private Boolean isActive;
}