package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FaqRequestDTO {

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Answer is required")
    private String answer;

    @NotNull(message = "Display order is required")
    private Integer displayOrder;

    @NotNull(message = "Published status is required")
    private Boolean published;

    @NotNull(message = "Active status is required")
    private Boolean isActive;
}