package com.example.controller.auth;

import com.example.dto.request.UserLoginRequestDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.MessageResponse;
import com.example.model.RefreshToken;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;
import com.example.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody UserRegisterRequestDTO request
    ) {
        userService.register(request);
        return ResponseEntity.ok(
                new MessageResponse("User registered successfully")
        );
    }

    // 🔐 USER LOGIN (UPDATED)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginRequestDTO request
    ) {

        // 🔹 Generate access token
        String accessToken = userService.loginAndGenerateToken(
                request.getEmail(),
                request.getPassword()
        );

        // 🔹 Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createToken(request.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken())
        );
    }

    // 🔄 REFRESH TOKEN API
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestParam String refreshToken
    ) {

        // 🔹 Validate refresh token
        RefreshToken token = refreshTokenService.verifyToken(refreshToken);

        // 🔹 Generate new access token
        String newAccessToken = jwtUtil.generateToken(
                token.getEmail(),
                List.of("ROLE_USER") // 🔥 if admin needed, we can enhance later
        );

        return ResponseEntity.ok(
                new LoginResponse(newAccessToken, refreshToken)
        );
    }
}