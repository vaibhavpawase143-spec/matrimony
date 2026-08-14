package com.example.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

    void sendVerificationEmail(String email, String token);

    void sendOTPEmail(String to, String otp, String purpose);

    void sendWelcomeEmail(String to, String firstName);
    void sendForgotPasswordEmail(String to, String token);
    void sendPasswordChangedEmail(String to, String firstName);
    void sendAnnouncementEmail(
            String to,
            String firstName,
            String title,
            String message
    );
    void sendPremiumReminderEmail(
            String to,
            String firstName,
            int daysRemaining
    );

    void sendPremiumExpiredEmail(
            String to,
            String firstName
    );
}