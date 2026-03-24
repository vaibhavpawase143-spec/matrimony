package com.example.dto.other;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterestStatusUpdateDTO {

    @NotBlank(message = "Status is required")
    private String status;
}