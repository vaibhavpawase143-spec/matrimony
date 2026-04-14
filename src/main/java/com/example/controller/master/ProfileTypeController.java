package com.example.controller.master;

import com.example.dto.request.ProfileTypeRequestDTO;
import com.example.dto.response.ProfileTypeResponseDTO;
import com.example.model.Admin;
import com.example.model.ProfileType;
import com.example.service.ProfileTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile-types")
@RequiredArgsConstructor
public class ProfileTypeController {

    private final ProfileTypeService service;

    // ===== CREATE / UPDATE =====
    @PostMapping
    public ProfileTypeResponseDTO save(@Valid @RequestBody ProfileTypeRequestDTO dto) {

        ProfileType entity = mapToEntity(dto);
        ProfileType saved = service.save(entity);

        return mapToResponse(saved);
    }

    // ===== GET BY ID =====
    @GetMapping("/{id}")
    public ProfileTypeResponseDTO getById(@PathVariable Long id) {
        ProfileType pt = service.getById(id)
                .orElseThrow(() -> new RuntimeException("ProfileType not found"));

        return mapToResponse(pt);
    }

    // ===== GET ALL =====
    @GetMapping
    public List<ProfileTypeResponseDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===== GET BY ADMIN =====
    @GetMapping("/admin/{adminId}")
    public List<ProfileTypeResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===== ACTIVE =====
    @GetMapping("/admin/{adminId}/active")
    public List<ProfileTypeResponseDTO> getActive(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===== INACTIVE =====
    @GetMapping("/admin/{adminId}/inactive")
    public List<ProfileTypeResponseDTO> getInactive(@PathVariable Long adminId) {
        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===== SEARCH =====
    @GetMapping("/admin/{adminId}/search")
    public List<ProfileTypeResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "ProfileType deleted successfully";
    }

    // =========================
    // 🔁 MAPPING METHODS (FIXED)
    // =========================

    private ProfileType mapToEntity(ProfileTypeRequestDTO dto) {

        ProfileType pt = new ProfileType();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        pt.setAdmin(admin);

        pt.setName(dto.getName());

        if (dto.getIsActive() != null) {
            pt.setIsActive(dto.getIsActive());
        }

        return pt;
    }

    private ProfileTypeResponseDTO mapToResponse(ProfileType pt) {

        ProfileTypeResponseDTO dto = new ProfileTypeResponseDTO();

        dto.setId(pt.getId());
        dto.setAdminId(pt.getAdmin() != null ? pt.getAdmin().getId() : null);

        // ❌ REMOVED: pt.getAdmin().getName() → caused Lazy error
        dto.setAdminName(null);

        dto.setName(pt.getName());
        dto.setIsActive(pt.getIsActive());
        dto.setCreatedAt(pt.getCreatedAt());
        dto.setUpdatedAt(pt.getUpdatedAt());

        return dto;
    }
}