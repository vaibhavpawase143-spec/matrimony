package com.example.controller.auth;

import com.example.dto.request.SendOtpRequestDTO;
import com.example.dto.request.VerifyOtpRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.OtpStatusResponseDTO;
import com.example.service.RealtimeOTPService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/otp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OTPController {

    private final RealtimeOTPService realtimeOTPService;

    // ================= 1. SEND OTP =================
    @PostMapping("/send")
    public ApiResponse<OtpStatusResponseDTO> sendOTP(@Valid @RequestBody SendOtpRequestDTO request) {
        OtpStatusResponseDTO response = realtimeOTPService.sendOTP(request);
        return ApiResponse.<OtpStatusResponseDTO>builder()
                .success(true)
                .message(response.getMessage())
                .data(response)
                .build();
    }

    // ================= 2. RESEND OTP =================
    @PostMapping("/resend")
    public ApiResponse<OtpStatusResponseDTO> resendOTP(
            @RequestParam String target,
            @RequestParam(required = false, defaultValue = "VERIFICATION") String purpose
    ) {
        OtpStatusResponseDTO response = realtimeOTPService.resendOTP(target, purpose);
        return ApiResponse.<OtpStatusResponseDTO>builder()
                .success(true)
                .message("OTP resent successfully")
                .data(response)
                .build();
    }

    // ================= 3. VERIFY OTP =================
    @PostMapping("/verify")
    public ApiResponse<String> verifyOTP(@Valid @RequestBody VerifyOtpRequestDTO request) {
        boolean verified = realtimeOTPService.verifyOTP(request);
        return ApiResponse.<String>builder()
                .success(verified)
                .message("OTP verified successfully")
                .data("SUCCESS")
                .build();
    }

    // ================= 4. LOGIN SEND OTP =================
    @PostMapping("/login-send")
    public ApiResponse<OtpStatusResponseDTO> sendLoginOTP(@RequestParam String target) {
        SendOtpRequestDTO request = SendOtpRequestDTO.builder()
                .target(target)
                .purpose("LOGIN")
                .build();
        OtpStatusResponseDTO response = realtimeOTPService.sendOTP(request);
        return ApiResponse.<OtpStatusResponseDTO>builder()
                .success(true)
                .message("Login OTP sent successfully")
                .data(response)
                .build();
    }

    // ================= 5. LOGIN VERIFY OTP =================
    @PostMapping("/login-verify")
    public ApiResponse<LoginResponse> loginWithOTP(@Valid @RequestBody VerifyOtpRequestDTO request) {
        request.setPurpose("LOGIN");
        LoginResponse loginResponse = realtimeOTPService.loginWithOTP(request);
        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("OTP Login successful")
                .data(loginResponse)
                .build();
    }
}
