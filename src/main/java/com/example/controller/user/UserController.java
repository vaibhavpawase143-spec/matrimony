package com.example.controller.user;

import com.example.dto.request.ChangePasswordRequestDTO;
import com.example.dto.request.LoginRequest;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.AuthResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.User;
import com.example.service.EmailService;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;
    private final EmailService emailService;
    private final com.example.security.ratelimit.RateLimitService rateLimitService;
    private final com.example.security.ratelimit.ClientIpResolver clientIpResolver;
    private final com.example.config.RateLimitProperties rateLimitProperties;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @RequestBody UserRegisterRequestDTO dto) {

        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        var policy = rateLimitProperties.getRegistration();

        var ipCheck = rateLimitService.tryAcquire("reg:ip:" + clientIp, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many registration requests from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "REGISTRATION"
            );
        }

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
    public ResponseEntity<AuthResponse> login(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @RequestBody LoginRequest request) {

        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        String ipKey = "user_login:ip:" + clientIp;
        String accountKey = "user_login:account:" + normalizedEmail;

        var policy = rateLimitProperties.getUserLogin();

        var ipCheck = rateLimitService.checkFailedAttempts(ipKey, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many failed login attempts from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "USER_LOGIN"
            );
        }

        var accountCheck = rateLimitService.checkFailedAttempts(accountKey, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!accountCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many failed login attempts for this account. Please try again in " + accountCheck.getRetryAfterSeconds() + " seconds.",
                    accountCheck.getRetryAfterSeconds(),
                    "USER_LOGIN"
            );
        }

        String token;
        try {
            token = service.loginAndGenerateToken(
                    request.getEmail(),
                    request.getPassword()
            );
            rateLimitService.reset(ipKey);
            rateLimitService.reset(accountKey);
        } catch (RuntimeException e) {
            rateLimitService.recordFailedAttempt(ipKey, policy.getWindowSeconds());
            rateLimitService.recordFailedAttempt(accountKey, policy.getWindowSeconds());
            throw e;
        }

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

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO request) {

        service.changePassword(request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Password changed successfully"
                )
        );
    }

    // ================= REDIRECT OLD EMAIL LINKS =================
    @GetMapping("/verify")
    public String redirectOldVerifyLink(@RequestParam String token) {
        // Redirect old /api/users/verify links to the correct /api/auth/verify endpoint
        return "redirect:/api/auth/verify?token=" + token;
    }
}