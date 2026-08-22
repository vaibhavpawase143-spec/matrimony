package com.example.exception;

import lombok.Getter;

@Getter
public class RateLimitExceededException extends RuntimeException {

    private final long retryAfterSeconds;
    private final String action;

    public RateLimitExceededException(String message, long retryAfterSeconds, String action) {
        super(message);
        this.retryAfterSeconds = Math.max(1, retryAfterSeconds);
        this.action = action;
    }

    public RateLimitExceededException(String message, long retryAfterSeconds) {
        this(message, retryAfterSeconds, "REQUEST");
    }
}
