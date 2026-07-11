package com.example.dto.request;

import com.example.model.SupportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SupportRequestDTO {

    @NotNull(message = "Category is required")
    private SupportCategory category;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message is required")
    private String message;

    // Optional screenshot
    private String attachmentUrl;

}