package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReligionRequestDTO {

    @NotBlank(message = "Religion name is required")
    @Size(max = 120, message = "Religion name must not exceed 120 characters")
    private String name;

    private Boolean isActive = true;
}