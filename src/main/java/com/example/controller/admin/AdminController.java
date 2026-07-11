package com.example.controller.admin;

import com.example.dto.other.AdminLoginDTO;
import com.example.dto.request.AdminFilterDTO;
import com.example.dto.request.AdminRequestDTO;
import com.example.dto.request.AdminResetPasswordDTO;
import com.example.dto.request.AdminUpdateDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.dto.response.AdminStatsDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.Admin;
import com.example.model.RefreshToken;
import com.example.security.JwtUtil;
import com.example.service.AdminService;
import com.example.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

        Admin admin = adminService.findByEmail(refreshToken.getEmail());

        String newAccessToken = jwtUtil.generateToken(
                admin.getEmail(),
                List.of(admin.getRole().getName())
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
//    @PutMapping("/block/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(adminService.blockUser(userId));
//    }
//
//    @PutMapping("/unblock/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(adminService.unblockUser(userId));
//    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<AdminStatsDTO> getStatistics() {

        return ApiResponse.<AdminStatsDTO>builder()
                .success(true)
                .message("Admin statistics retrieved successfully")
                .data(adminService.getStatistics())
                .build();
    }
    @GetMapping("/manage")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<Page<AdminResponseDTO>> getAllAdmins(
            @ModelAttribute AdminFilterDTO filter
    ) {

        return ApiResponse.<Page<AdminResponseDTO>>builder()
                .success(true)
                .message("Admins retrieved successfully")
                .data(adminService.getAllAdmins(filter))
                .build();
    }
    @PutMapping("/{id}/manage")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<AdminResponseDTO> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateDTO dto
    ) {

        return ApiResponse.<AdminResponseDTO>builder()
                .success(true)
                .message("Admin updated successfully")
                .data(adminService.updateAdmin(id, dto))
                .build();
    }
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> activateAdmin(
            @PathVariable Long id
    ) {

        adminService.activateAdmin(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Admin activated successfully")
                .data("Admin activated successfully")
                .build();
    }
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> deactivateAdmin(
            @PathVariable Long id
    ) {

        adminService.deactivateAdmin(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Admin deactivated successfully")
                .data("Admin deactivated successfully")
                .build();
    }
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody AdminResetPasswordDTO dto
    ) {

        adminService.resetPassword(id, dto);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successfully")
                .data("Password reset successfully")
                .build();
    }


}