package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpRequestDTO {

    @NotBlank(message = "Target (phone or email) is required")
    private String target;

    private String channel; // "PHONE", "EMAIL", or null (auto-detect)

    private String purpose; // "VERIFICATION", "LOGIN", "PASSWORD_RESET"
}
