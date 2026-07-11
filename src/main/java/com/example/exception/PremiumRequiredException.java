package com.example.exception;

public class PremiumRequiredException extends RuntimeException {

    public PremiumRequiredException(String message) {
        super(message);
    }
}