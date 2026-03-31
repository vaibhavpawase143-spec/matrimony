package com.example.controller.auth;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.*;
import com.example.model.RefreshToken;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;
import com.example.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    // ================= REGISTER =================
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
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginRequestDTO request) {

        String accessToken = userService.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        RefreshToken refreshToken = refreshTokenService.createToken(request.getEmail());

        LoginResponse response = new LoginResponse(accessToken, refreshToken.getToken());

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }
}