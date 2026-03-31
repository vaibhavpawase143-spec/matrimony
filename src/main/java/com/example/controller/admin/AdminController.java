package com.example.controller.admin;

import com.example.dto.other.AdminLoginDTO;
import com.example.dto.request.AdminRequestDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.service.AdminService;
import com.example.service.RefreshTokenService;
import com.example.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // ================= CREATE ADMIN =================
    @PostMapping
    public AdminResponseDTO create(@Valid @RequestBody AdminRequestDTO dto) {

        Admin admin = new Admin();

        admin.setName(dto.getName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPassword(dto.getPassword());
        admin.setIsActive(true);

        Admin saved = adminService.create(admin);

        return mapToResponse(saved);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody AdminLoginDTO dto) {

        Admin admin = adminService.login(dto.getUsername(), dto.getPassword());

        String accessToken = jwtUtil.generateToken(
                admin.getEmail(),
                List.of("ROLE_ADMIN")
        );

        RefreshToken refreshToken = refreshTokenService.createToken(admin.getEmail());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken(),
                "admin", mapToResponse(admin)
        );
    }

    // ================= GET ALL ADMINS =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminResponseDTO> getAll() {
        return adminService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ADMIN BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminResponseDTO getById(@PathVariable Long id) {
        return mapToResponse(adminService.getById(id));
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public String logout(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        refreshTokenService.deleteByEmail(email);

        return "Logged out successfully";
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> request) {

        String token = request.get("refreshToken");

        RefreshToken refreshToken = refreshTokenService.verifyToken(token);

        String newAccessToken = jwtUtil.generateToken(
                refreshToken.getEmail(),
                List.of("ROLE_ADMIN")
        );

        return Map.of(
                "accessToken", newAccessToken
        );
    }

    // ================= MAPPERS =================

    private AdminResponseDTO mapToResponse(Admin admin) {
        AdminResponseDTO dto = new AdminResponseDTO();

        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setIsActive(admin.getIsActive());
        dto.setLastLogin(admin.getLastLogin());
        dto.setCreatedAt(admin.getCreatedAt());

        return dto;
    }
}