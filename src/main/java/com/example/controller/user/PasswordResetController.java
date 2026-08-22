package com.example.controller.user;

import com.example.config.RateLimitProperties;
import com.example.dto.request.ForgotPasswordRequest;
import com.example.dto.request.ResetPasswordRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.exception.RateLimitExceededException;
import com.example.security.ratelimit.ClientIpResolver;
import com.example.security.ratelimit.RateLimitService;
import com.example.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final UserService userService;
    private final RateLimitService rateLimitService;
    private final ClientIpResolver clientIpResolver;
    private final RateLimitProperties rateLimitProperties;

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(
            HttpServletRequest httpRequest,
            @Valid @RequestBody ForgotPasswordRequest request) {

        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        RateLimitProperties.Policy policy = rateLimitProperties.getForgotPassword();

        var ipCheck = rateLimitService.tryAcquire("forgot_pwd:ip:" + clientIp, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new RateLimitExceededException(
                    "Too many password reset requests from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "FORGOT_PASSWORD"
            );
        }

        var accountCheck = rateLimitService.tryAcquire("forgot_pwd:account:" + normalizedEmail, 1, policy.getBlockDurationSeconds());
        if (!accountCheck.isAllowed()) {
            throw new RateLimitExceededException(
                    "Please wait " + accountCheck.getRetryAfterSeconds() + " seconds before requesting another password reset.",
                    accountCheck.getRetryAfterSeconds(),
                    "FORGOT_PASSWORD"
            );
        }

        try {
            userService.forgotPassword(request.getEmail());
        } catch (RuntimeException e) {
            // Uniform response for non-existent users to prevent account enumeration
            if (e.getMessage() == null || !e.getMessage().contains("User not found")) {
                throw e;
            }
        }

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset link sent to email")
                .data(null)
                .build();
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            HttpServletRequest httpRequest,
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        String clientIp = clientIpResolver.resolveClientIp(httpRequest);
        String token = request.getToken() != null ? request.getToken().trim() : "";
        String tokenHash = DigestUtils.md5DigestAsHex(token.getBytes(StandardCharsets.UTF_8));
        RateLimitProperties.Policy policy = rateLimitProperties.getResetPassword();

        String ipKey = "reset_pwd:ip:" + clientIp;
        String tokenKey = "reset_pwd:token:" + tokenHash;

        var ipCheck = rateLimitService.checkFailedAttempts(ipKey, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!ipCheck.isAllowed()) {
            throw new RateLimitExceededException(
                    "Too many invalid reset token attempts from this IP. Please try again in " + ipCheck.getRetryAfterSeconds() + " seconds.",
                    ipCheck.getRetryAfterSeconds(),
                    "RESET_PASSWORD"
            );
        }

        var tokenCheck = rateLimitService.checkFailedAttempts(tokenKey, policy.getMaxAttempts(), policy.getWindowSeconds());
        if (!tokenCheck.isAllowed()) {
            throw new RateLimitExceededException(
                    "This password reset link has exceeded maximum verification attempts. Please request a new link.",
                    tokenCheck.getRetryAfterSeconds(),
                    "RESET_PASSWORD"
            );
        }

        try {
            userService.resetPassword(
                    request.getToken(),
                    request.getNewPassword()
            );
            rateLimitService.reset(ipKey);
            rateLimitService.reset(tokenKey);
        } catch (RuntimeException e) {
            rateLimitService.recordFailedAttempt(ipKey, policy.getWindowSeconds());
            rateLimitService.recordFailedAttempt(tokenKey, policy.getWindowSeconds());
            throw e;
        }

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successfully")
                .data(null)
                .build();
    }
}