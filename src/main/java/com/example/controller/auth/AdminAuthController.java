package com.example.controller.auth;

import com.example.dto.response.LoginResponse;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.repository.AdminRepository;
import com.example.security.JwtUtil;

import com.example.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // 🔐 ADMIN LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestParam String email,
            @RequestParam String password
    ) {

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 🔐 Access Token
        String accessToken = jwtUtil.generateToken(
                admin.getEmail(),
                List.of("ROLE_ADMIN")
        );

        // 🔄 Refresh Token
        RefreshToken refreshToken = refreshTokenService.createToken(admin.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken())
        );
    }
}