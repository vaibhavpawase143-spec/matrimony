package com.example.security.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitResult {

    private boolean allowed;
    private long remainingAttempts;
    private long retryAfterSeconds;
    private long currentAttempts;
    private long limit;

    public static RateLimitResult allow(long remaining, long limit) {
        return RateLimitResult.builder()
                .allowed(true)
                .remainingAttempts(remaining)
                .retryAfterSeconds(0)
                .limit(limit)
                .build();
    }

    public static RateLimitResult deny(long retryAfterSeconds, long currentAttempts, long limit) {
        return RateLimitResult.builder()
                .allowed(false)
                .remainingAttempts(0)
                .retryAfterSeconds(Math.max(1, retryAfterSeconds))
                .currentAttempts(currentAttempts)
                .limit(limit)
                .build();
    }
}
