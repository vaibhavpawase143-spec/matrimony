package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupportStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;

}