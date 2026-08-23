package com.example.controller.auth;

import com.example.dto.request.ResendVerificationRequestDTO;
import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.LoginResponse;
import com.example.service.RecaptchaService;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RecaptchaService recaptchaService;
    private final com.example.security.ratelimit.RateLimitService rateLimitService;
    private final com.example.security.ratelimit.ClientIpResolver clientIpResolver;
    private final com.example.config.RateLimitProperties rateLimitProperties;
    private final com.example.repository.UserRepository userRepository;
    private final com.example.security.JwtUtil jwtUtil;
    private final com.example.security.TokenRevocationService tokenRevocationService;

    @Value("${app.bypass.enabled:false}")
    private boolean bypassEnabled;

    // ================= STEP 1: SEND VERIFICATION =================
    @PostMapping("/send-verification")
    public ApiResponse<String> sendVerification(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @RequestParam String email) {

        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        var policy = rateLimitProperties.getEmailVerification();

        var ipCheck = rateLimitService.tryAcquire("email_verify:ip:" + clientIp, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many verification requests from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "EMAIL_VERIFY_SEND"
            );
        }

        var accountCheck = rateLimitService.tryAcquire("email_verify:account:" + normalizedEmail, 1, policy.getBlockDurationSeconds());
        if (!accountCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Please wait " + accountCheck.getRetryAfterSeconds() + " seconds before requesting another verification email.",
                    accountCheck.getRetryAfterSeconds(),
                    "EMAIL_VERIFY_SEND"
            );
        }

        userService.sendVerification(email);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email sent successfully")
                .data(null)
                .build();
    }

    // ================= STEP 2: VERIFY EMAIL =================
    @GetMapping("/verify")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam String token,
            HttpServletResponse response
    ) {

        try {

            userService.verifyEmail(token);

            response.sendRedirect(
                    "http://localhost:3000/email-verified"
            );

        } catch (Exception e) {

            e.printStackTrace();

        }

        return ResponseEntity.ok().build();
    }
    // ================= DEV: BYPASS EMAIL VERIFICATION =================
    @PostMapping("/bypass-verification")
    public ApiResponse<String> bypassVerification(@RequestParam String email) {

        if (!bypassEnabled) {
            throw new AccessDeniedException("Bypass verification is disabled in production");
        }
        
        userService.bypassEmailVerification(email);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Email verification bypassed for development")
                .data(null)
                .build();
    }

    // ================= STEP 3: REGISTER =================
    @PostMapping("/register")
    public ApiResponse<String> register(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @Valid @RequestBody UserRegisterRequestDTO request) {

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

        recaptchaService.verify(
                request.getRecaptchaToken(),
                "register"
        );

        userService.register(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User registered successfully")
                .data(null)
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @Valid @RequestBody UserLoginRequestDTO request) {

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

        recaptchaService.verify(
                request.getRecaptchaToken(),
                "login"
        );

        LoginResponse loginResponse;
        try {
            loginResponse = userService.loginWithProfile(
                    request.getEmail(),
                    request.getPassword()
            );
            // Reset counters on successful login
            rateLimitService.reset(ipKey);
            rateLimitService.reset(accountKey);
        } catch (RuntimeException e) {
            rateLimitService.recordFailedAttempt(ipKey, policy.getWindowSeconds());
            rateLimitService.recordFailedAttempt(accountKey, policy.getWindowSeconds());
            throw e;
        }

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(loginResponse)
                .build();
    }

    // ================= RESEND VERIFICATION =================
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @Valid @RequestBody ResendVerificationRequestDTO request
    ) {

        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        var policy = rateLimitProperties.getEmailVerification();

        var ipCheck = rateLimitService.tryAcquire("email_verify:ip:" + clientIp, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many verification requests from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "EMAIL_VERIFY_RESEND"
            );
        }

        var accountCheck = rateLimitService.tryAcquire("email_verify:account:" + normalizedEmail, 1, policy.getBlockDurationSeconds());
        if (!accountCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Please wait " + accountCheck.getRetryAfterSeconds() + " seconds before requesting another verification email.",
                    accountCheck.getRetryAfterSeconds(),
                    "EMAIL_VERIFY_RESEND"
            );
        }

        userService.resendVerification(request.getEmail());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email resent successfully")
                .data(null)
                .build();
    }

    // ================= MOBILE OTP =================
    @PostMapping("/send-otp")
    public ApiResponse<String> sendOTP(@RequestParam String phone) {
        
        userService.sendOTPToPhone(phone);

        return ApiResponse.<String>builder()
                .success(true)
                .message("OTP sent successfully")
                .data(null)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOTP(@RequestParam String phone, @RequestParam String otp) {
        
        userService.verifyPhoneOTP(phone, otp);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Phone verified successfully")
                .data(null)
                .build();
    }

    @PostMapping("/bypass-phone-verification")
    public ApiResponse<String> bypassPhoneVerification(@RequestParam String phone) {

        if (!bypassEnabled) {
            throw new AccessDeniedException("Bypass phone verification is disabled in production");
        }
        
        userService.bypassPhoneVerification(phone);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Phone verification bypassed for development")
                .data(null)
                .build();
    }


    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ApiResponse<java.util.Map<String, String>> refresh(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            @RequestBody java.util.Map<String, String> request
    ) {
        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        var policy = rateLimitProperties.getUserLogin();

        var ipCheck = rateLimitService.tryAcquire("refresh:ip:" + clientIp, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new com.example.exception.RateLimitExceededException(
                    "Too many refresh requests from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "USER_REFRESH"
            );
        }

        String token = request != null ? request.get("refreshToken") : null;
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Refresh token is required");
        }

        com.example.model.RefreshToken newRefreshToken = refreshTokenService.rotateToken(token);

        com.example.model.User user = userRepository.findByEmailWithRoles(newRefreshToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())
                || Boolean.TRUE.equals(user.getIsDeleted())
                || Boolean.TRUE.equals(user.getIsBlocked())) {
            throw new RuntimeException("User account is inactive or blocked");
        }

        java.util.List<String> roles = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().stream().map(com.example.model.Role::getName).toList()
                : java.util.List.of("ROLE_USER");

        String newAccessToken = jwtUtil.generateToken(
                user.getEmail(),
                roles,
                java.util.UUID.randomUUID().toString(),
                "USER"
        );

        java.util.Map<String, String> data = java.util.Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken.getToken()
        );

        return ApiResponse.<java.util.Map<String, String>>builder()
                .success(true)
                .message("Token refreshed")
                .data(data)
                .build();
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ApiResponse<String> logout(
            Principal principal,
            jakarta.servlet.http.HttpServletRequest httpRequest
    ) {

        if (principal != null && principal.getName() != null) {
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                tokenRevocationService.revokeToken(authHeader.substring(7));
            }
            userService.logout(principal.getName());
            refreshTokenService.deleteByEmail(principal.getName());
        }

        return ApiResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .build();
    }
}