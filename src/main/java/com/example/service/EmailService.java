package com.example.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

    void sendVerificationEmail(String email, String token);

    void sendWelcomeEmail(String to, String firstName);
}