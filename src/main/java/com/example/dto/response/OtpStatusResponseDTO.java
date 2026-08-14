package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpStatusResponseDTO {

    private boolean success;
    private String message;
    private String target;
    private String channel; // "PHONE" or "EMAIL"
    private String purpose;
    private long cooldownSeconds;
    private long expiresInSeconds;
    private Integer remainingAttempts;
    private String devOtp; // Only populated in dev/testing mode
}
