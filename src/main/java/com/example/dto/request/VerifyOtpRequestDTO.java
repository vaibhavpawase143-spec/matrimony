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
public class VerifyOtpRequestDTO {

    @NotBlank(message = "Target (phone or email) is required")
    private String target;

    @NotBlank(message = "OTP is required")
    private String otp;

    private String purpose; // "VERIFICATION", "LOGIN", "PASSWORD_RESET"
}
