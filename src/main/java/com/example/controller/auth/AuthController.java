package com.example.controller.auth;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.request.ResendVerificationRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.RefreshToken;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 🔐 safer than "*"
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    // ================= STEP 1: SEND VERIFICATION =================
    @PostMapping("/send-verification")
    public ApiResponse<String> sendVerification(@RequestParam String email) {

        userService.sendVerification(email);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email sent successfully")
                .data(null)
                .build();
    }

    // ================= STEP 2: VERIFY EMAIL =================
    @GetMapping("/verify")
    public ApiResponse<String> verifyEmail(@RequestParam String token) {

        userService.verifyEmail(token);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Email verified successfully")
                .data(null)
                .build();
    }

    // ================= STEP 3: REGISTER =================
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody UserRegisterRequestDTO request) {

        userService.register(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("User registered successfully")
                .data(null)
                .build();
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody UserLoginRequestDTO request) {

        String accessToken = userService.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        RefreshToken refreshToken = refreshTokenService.createToken(request.getEmail());

        return ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken.getToken()
                ))
                .build();
    }

    // ================= RESEND VERIFICATION =================
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(
            @Valid @RequestBody ResendVerificationRequestDTO request
    ) {

        userService.resendVerification(request.getEmail());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Verification email resent successfully")
                .data(null)
                .build();
    }
}