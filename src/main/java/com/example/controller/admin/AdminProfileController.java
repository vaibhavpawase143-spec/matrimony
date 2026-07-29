package com.example.controller.admin;

import com.example.dto.request.AdminProfileUpdateDTO;
import com.example.dto.request.ChangePasswordDTO;
import com.example.dto.response.AdminProfileResponseDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.Admin;
import com.example.service.AdminProfileService;
import com.example.service.CurrentAdminService;
import com.example.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/profile")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class AdminProfileController {

    private final AdminProfileService adminProfileService;
    private final FileStorageService fileStorageService;
    private final CurrentAdminService currentAdminService;
    // ==========================================
    // GET PROFILE
    // ==========================================

    @GetMapping
    public ApiResponse<AdminProfileResponseDTO> getProfile() {

        return ApiResponse.<AdminProfileResponseDTO>builder()
                .success(true)
                .message("Profile retrieved successfully")
                .data(adminProfileService.getProfile())
                .build();
    }

    // ==========================================
    // UPDATE PROFILE
    // ==========================================

    @PutMapping
    public ApiResponse<AdminProfileResponseDTO> updateProfile(
            @Valid @RequestBody AdminProfileUpdateDTO dto) {

        return ApiResponse.<AdminProfileResponseDTO>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(adminProfileService.updateProfile(dto))
                .build();
    }

    // ==========================================
    // CHANGE PASSWORD
    // ==========================================

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO dto) {

        adminProfileService.changePassword(dto);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password changed successfully")
                .data("Password updated successfully")
                .build();
    }
    @PostMapping("/upload-photo")
    public ApiResponse<String> uploadPhoto(
            @RequestParam("file") MultipartFile file
    ) {

        Admin admin = currentAdminService.getCurrentAdmin();

        String fileName = fileStorageService.storeFile(file);

        admin.setProfilePhoto("/uploads/" + fileName);

        currentAdminService.save(admin);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Photo uploaded successfully")
                .data(admin.getProfilePhoto())
                .build();
    }
}