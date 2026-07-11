package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupportReplyRequestDTO {

    @NotBlank(message = "Reply message is required")
    private String reply;

}