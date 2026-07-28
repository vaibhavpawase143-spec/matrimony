package com.example.service;

public interface RecaptchaService {

    /**
     * Verify Google reCAPTCHA v3 token.
     *
     * @param token  Token received from frontend
     * @param action Expected action (login, register, etc.)
     */
    void verify(String token, String action);

}