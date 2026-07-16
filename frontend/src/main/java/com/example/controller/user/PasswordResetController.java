package com.example.controller.user;

import com.example.dto.request.ForgotPasswordRequest;
import com.example.dto.request.ResetPasswordRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final UserService userService;

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request.getEmail());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset link sent to email")
                .data(null)
                .build();
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        userService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successfully")
                .data(null)
                .build();
    }
}