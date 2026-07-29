package com.example.controller.auth;

import com.example.dto.request.AdminChangePasswordDTO;
import com.example.dto.request.LoginRequest;
import com.example.dto.response.LoginResponse;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.repository.AdminRepository;
import com.example.security.JwtUtil;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminService;
import com.example.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AdminAuditLogService adminAuditLogService;
    private final AdminService adminService;

    // 🔐 ADMIN LOGIN (JSON SUPPORT)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // 🔍 Find admin by email
        Admin admin = adminRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 🔒 Password validation
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🚫 Check account status
        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new RuntimeException(
                    "Your account has been deactivated. Please contact the Super Admin."
            );
        }

        // ✅ Generate unique session ID
        String sessionId = UUID.randomUUID().toString();

        // ✅ Save session ID
        admin.setSessionId(sessionId);
        adminRepository.save(admin);

        // 🔐 Generate Access Token
        String accessToken = jwtUtil.generateToken(
                admin.getEmail(),
                List.of("ROLE_ADMIN"),
                sessionId,
                "ADMIN"
        );

        // 🔄 Generate Refresh Token
        RefreshToken refreshToken = refreshTokenService.createToken(admin.getEmail());

        // 📝 Audit Log
        adminAuditLogService.log(
                admin.getId(),
                "AUTHENTICATION",
                "ADMIN_LOGIN",
                "ADMIN",
                admin.getId(),
                "Admin logged in successfully: " + admin.getEmail(),
                null,
                null,
                "SYSTEM",
                "SYSTEM"
        );

        // ✅ Return response
        return ResponseEntity.ok(
                new LoginResponse(
                        accessToken,
                        refreshToken.getToken(),
                        "ROLE_ADMIN",
                        null
                )
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody AdminChangePasswordDTO dto
    ) {

        adminService.changeOwnPassword(dto);

        return ResponseEntity.ok("Password changed successfully.");
    }
}