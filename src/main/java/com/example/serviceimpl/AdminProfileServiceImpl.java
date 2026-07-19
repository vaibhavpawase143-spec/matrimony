package com.example.serviceimpl;

import com.example.dto.request.AdminProfileUpdateDTO;
import com.example.dto.request.ChangePasswordDTO;
import com.example.dto.response.AdminProfileResponseDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.repository.AdminRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminProfileService;
import com.example.service.CurrentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl
        implements AdminProfileService {

    private final AdminRepository adminRepository;

    private final CurrentAdminService currentAdminService;

    private final PasswordEncoder passwordEncoder;

    private final AdminAuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public AdminProfileResponseDTO getProfile() {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        Admin admin = adminRepository.findByIdWithRole(currentAdmin.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        return mapToResponse(admin);
    }

    @Override
    @Transactional
    public AdminProfileResponseDTO updateProfile(
            AdminProfileUpdateDTO dto
    ) {

        Admin admin = currentAdminService.getCurrentAdmin();

        String oldValue =
                "Name=" + admin.getName() +
                        ", Phone=" + admin.getPhone() +
                        ", Photo=" + admin.getProfilePhoto();

        admin.setName(dto.getName());
        admin.setPhone(dto.getPhone());
        admin.setProfilePhoto(dto.getProfilePhoto());

        Admin updatedAdmin = adminRepository.save(admin);

        String newValue =
                "Name=" + updatedAdmin.getName() +
                        ", Phone=" + updatedAdmin.getPhone() +
                        ", Photo=" + updatedAdmin.getProfilePhoto();

        auditLogService.log(
                admin.getId(),
                "ADMIN_PROFILE",
                "PROFILE_UPDATED",
                "ADMIN",
                admin.getId(),
                "Admin profile updated",
                oldValue,
                newValue
        );

        return mapToResponse(updatedAdmin);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {

        Admin admin = currentAdminService.getCurrentAdmin();

        // ===============================
        // Verify current password
        // ===============================
        if (!passwordEncoder.matches(
                dto.getCurrentPassword(),
                admin.getPassword())) {

            throw new RuntimeException("Current password is incorrect");
        }

        // ===============================
        // New password == Current password
        // ===============================
        if (passwordEncoder.matches(
                dto.getNewPassword(),
                admin.getPassword())) {

            throw new RuntimeException(
                    "New password must be different from current password");
        }

        // ===============================
        // Confirm Password
        // ===============================
        if (!dto.getNewPassword()
                .equals(dto.getConfirmPassword())) {

            throw new RuntimeException(
                    "New password and confirm password do not match");
        }

        // ===============================
        // Update Password
        // ===============================
        admin.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        adminRepository.save(admin);

        // ===============================
        // Audit Log
        // ===============================
        auditLogService.log(
                admin.getId(),
                "ADMIN_PROFILE",
                "PASSWORD_CHANGED",
                "ADMIN",
                admin.getId(),
                "Admin changed password",
                null,
                "Password Updated Successfully"
        );
    }

    private AdminProfileResponseDTO mapToResponse(Admin admin) {

        return AdminProfileResponseDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(
                        admin.getRole() != null
                                ? admin.getRole().getName()
                                : null
                )
                .profilePhoto(admin.getProfilePhoto())
                .isActive(admin.getIsActive())
                .lastLogin(admin.getLastLogin())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}