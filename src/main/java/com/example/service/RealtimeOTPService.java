package com.example.service;

import com.example.dto.request.SendOtpRequestDTO;
import com.example.dto.request.VerifyOtpRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.OtpStatusResponseDTO;

public interface RealtimeOTPService {

    /**
     * Send OTP to target (phone or email) with rate limiting & expiration setup
     */
    OtpStatusResponseDTO sendOTP(SendOtpRequestDTO request);

    /**
     * Resend OTP with cooldown check
     */
    OtpStatusResponseDTO resendOTP(String target, String purpose);

    /**
     * Verify OTP for verification or password reset
     */
    boolean verifyOTP(VerifyOtpRequestDTO request);

    /**
     * Verify OTP and perform passwordless user login
     */
    LoginResponse loginWithOTP(VerifyOtpRequestDTO request);
}
