package com.example.service;

/**
 * SMS Service Interface
 *
 * Used for sending OTP and general SMS messages.
 *
 * Implementation can be:
 * - Fast2SMS
 * - Twilio
 * - AWS SNS
 * - Mock (for testing)
 */
public interface SMSService {

    /**
     * Send OTP to a phone number
     *
     * @param phone  Phone number (10-digit or with country code)
     * @param otp    Generated OTP
     */
    void sendOTP(String phone, String otp);

    /**
     * Send a custom SMS message
     *
     * @param phone   Phone number
     * @param message Message content
     */
    void sendSMS(String phone, String message);
}