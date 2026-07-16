package com.example.controller.user;

import com.example.dto.request.LoginRequest;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.AuthResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.User;
import com.example.service.EmailService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;
    private final EmailService emailService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@RequestBody UserRegisterRequestDTO dto) {

        User savedUser = service.register(dto);

        String token = UUID.randomUUID().toString();
        service.saveVerificationToken(savedUser.getId(), token);

        emailService.sendVerificationEmail(savedUser.getEmail(), token);

        // send OTP
        service.sendOTPToPhone(savedUser.getPhone());

        UserResponseDTO response = service.getById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User registered successfully. Please verify your email and phone number.")
                .data(response)
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        String token = service.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // ================= PHONE VERIFICATION - SEND OTP =================
    // ================= PHONE VERIFICATION - SEND OTP =================
    @PostMapping("/send-otp")
    public ApiResponse<String> sendOTP(@RequestParam String phone) {

        String otp = service.sendOTPToPhone(phone);

        return ApiResponse.<String>builder()
                .success(true)
                .message("OTP generated successfully")
                .data(otp)
                .build();
    }

    // ================= VERIFY OTP =================
    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOTP(
            @RequestParam String phone,
            @RequestParam String otp) {

        service.verifyPhoneOTP(phone, otp);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Phone verified successfully")
                .data(null)
                .build();
    }

    // ================= TEST EMAIL =================
    @GetMapping("/test-email")
    public ApiResponse<String> testEmail(@RequestParam String email) {

        try {
            emailService.sendEmail(email, "Test Email from Gathbandhan",
                "<h2>🧪 Email Test Successful!</h2>" +
                "<p>This is a test email from your Gathbandhan Matrimony application.</p>" +
                "<p>If you received this, your email configuration is working perfectly! 🎉</p>" +
                "<p><strong>Timestamp:</strong> " + LocalDateTime.now() + "</p>");

            return ApiResponse.<String>builder()
                    .success(true)
                    .message("Test email sent successfully to: " + email)
                    .data(null)
                    .build();

        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .success(false)
                    .message("Email test failed: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    // ================= REDIRECT OLD EMAIL LINKS =================
    @GetMapping("/verify")
    public String redirectOldVerifyLink(@RequestParam String token) {
        // Redirect old /api/users/verify links to the correct /api/auth/verify endpoint
        return "redirect:/api/auth/verify?token=" + token;
    }
}