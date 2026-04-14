package com.example.controller.admin;

import com.example.dto.other.AdminLoginDTO;
import com.example.dto.request.AdminRequestDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.security.JwtUtil;
import com.example.service.AdminService;
import com.example.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // ================= CREATE ADMIN =================
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<AdminResponseDTO> create(@Valid @RequestBody AdminRequestDTO dto) {

        Admin admin = new Admin();

        admin.setName(dto.getName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPassword(dto.getPassword());

        Admin saved = adminService.create(admin);

        return new ApiResponse<>(
                true,
                "Admin created successfully",
                com.example.mapper.AdminMapper.toDTO(saved) // ✅ FIX
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ApiResponse<Object> login(@Valid @RequestBody AdminLoginDTO dto) {

        Admin admin = adminService.login(dto.getEmail(), dto.getPassword());

        String accessToken = jwtUtil.generateToken(
                admin.getEmail(),
                List.of(admin.getRole().getName())
        );

        RefreshToken refreshToken = refreshTokenService.createToken(admin.getEmail());

        return new ApiResponse<>(
                true,
                "Login successful",
                java.util.Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken.getToken(),
                        "admin", com.example.mapper.AdminMapper.toDTO(admin) // ✅ FIX
                )
        );
    }

    // ================= GET ALL ADMINS =================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<AdminResponseDTO>> getAllAdmins() {

        return ApiResponse.<List<AdminResponseDTO>>builder()
                .success(true)
                .message("Admins fetched")
                .data(adminService.getAll()) // ✅ already DTO
                .build();
    }

    // ================= GET ADMIN BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminResponseDTO> getById(@PathVariable Long id) {

        AdminResponseDTO admin = adminService.getById(id); // ✅ FIX

        String loggedInEmail = getLoggedInEmail();

        if (!admin.getEmail().equalsIgnoreCase(loggedInEmail)) {
            throw new AccessDeniedException("You can only access your own data");
        }

        return new ApiResponse<>(
                true,
                "Admin fetched",
                admin
        );
    }

    // ================= UPDATE ADMIN =================
    @PutMapping("/{id}")
    public ApiResponse<AdminResponseDTO> update(@PathVariable Long id,
                                                @RequestBody AdminRequestDTO dto) {

        AdminResponseDTO existing = adminService.getById(id); // ✅ FIX

        String loggedInEmail = getLoggedInEmail();

        if (!existing.getEmail().equalsIgnoreCase(loggedInEmail)) {
            throw new AccessDeniedException("You can only update your own data");
        }

        Admin updated = new Admin();
        updated.setName(dto.getName());
        updated.setEmail(dto.getEmail());
        updated.setUsername(dto.getUsername());
        updated.setPhone(dto.getPhone());
        updated.setPassword(dto.getPassword());

        Admin saved = adminService.update(id, updated);

        return new ApiResponse<>(
                true,
                "Admin updated successfully",
                com.example.mapper.AdminMapper.toDTO(saved) // ✅ FIX
        );
    }

    // ================= DELETE ADMIN =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> delete(@PathVariable Long id) {

        adminService.delete(id);

        return new ApiResponse<>(true, "Admin deleted successfully", null);
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody java.util.Map<String, String> request) {

        String email = request.get("email");
        refreshTokenService.deleteByEmail(email);

        return new ApiResponse<>(true, "Logged out successfully", null);
    }

    // ================= REFRESH TOKEN =================
    @PostMapping("/refresh")
    public ApiResponse<String> refresh(@RequestBody java.util.Map<String, String> request) {

        String token = request.get("refreshToken");

        RefreshToken refreshToken = refreshTokenService.verifyToken(token);

        String newAccessToken = jwtUtil.generateToken(
                refreshToken.getEmail(),
                List.of("ROLE_ADMIN")
        );

        return new ApiResponse<>(true, "Token refreshed", newAccessToken);
    }

    // ================= HELPER =================
    private String getLoggedInEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
    @PutMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.blockUser(userId));
    }

    @PutMapping("/unblock/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.unblockUser(userId));
    }
}