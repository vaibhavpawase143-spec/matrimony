package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private List<String> trustedProxies = new ArrayList<>();

    // User Login Rate Limiting
    private Policy userLogin = new Policy(5, 900, 900); // 5 attempts per 15 minutes

    // Admin Login Rate Limiting (Stricter)
    private Policy adminLogin = new Policy(5, 900, 900); // 5 attempts per 15 minutes

    // Forgot Password Rate Limiting
    private Policy forgotPassword = new Policy(3, 900, 60); // 3 requests per 15 minutes, 60s cooldown

    // Email Verification Resend Rate Limiting
    private Policy emailVerification = new Policy(3, 900, 60); // 3 requests per 15 minutes, 60s cooldown

    // Password Reset Token Validation Rate Limiting
    private Policy resetPassword = new Policy(5, 900, 900); // 5 invalid attempts per 15 minutes

    // Registration Rate Limiting
    private Policy registration = new Policy(10, 3600, 3600); // 10 registrations per IP per hour

    @Getter
    @Setter
    public static class Policy {
        private int maxAttempts;
        private int windowSeconds;
        private int blockDurationSeconds;

        public Policy() {
            this.maxAttempts = 5;
            this.windowSeconds = 900;
            this.blockDurationSeconds = 900;
        }

        public Policy(int maxAttempts, int windowSeconds, int blockDurationSeconds) {
            this.maxAttempts = maxAttempts;
            this.windowSeconds = windowSeconds;
            this.blockDurationSeconds = blockDurationSeconds;
        }
    }
}
