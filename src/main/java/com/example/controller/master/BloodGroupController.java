package com.example.controller.master;

import com.example.dto.request.BloodGroupRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.BloodGroupResponseDTO;
import com.example.model.Admin;
import com.example.model.BloodGroup;
import com.example.repository.AdminRepository;
import com.example.service.BloodGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-groups")
@RequiredArgsConstructor
public class BloodGroupController {

    private final BloodGroupService bloodGroupService;
    private final AdminRepository adminRepository;

    // 🔐 Get logged-in admin
    private Admin getCurrentAdmin() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<BloodGroupResponseDTO> create(@Valid @RequestBody BloodGroupRequestDTO dto) {

        Admin admin = getCurrentAdmin();

        BloodGroup bloodGroup = mapToEntity(dto);
        BloodGroup saved = bloodGroupService.create(bloodGroup, admin.getId());

        return ApiResponse.<BloodGroupResponseDTO>builder()
                .success(true)
                .message("Blood group created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<BloodGroupResponseDTO> getById(@PathVariable Long id) {

        Admin admin = getCurrentAdmin();

        BloodGroup bg = bloodGroupService.getById(id, admin.getId());
        return ApiResponse.<BloodGroupResponseDTO>builder()
                .success(true)
                .message("Blood group retrieved successfully.")
                .data(mapToResponse(bg))
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<BloodGroupResponseDTO>> getAll() {

        List<BloodGroupResponseDTO> bloodGroups = bloodGroupService.getAll(null)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BloodGroupResponseDTO>>builder()
                .success(true)
                .message("Blood groups retrieved successfully.")
                .data(bloodGroups)
                .build();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<BloodGroupResponseDTO>> getActive() {

        List<BloodGroupResponseDTO> activeBloodGroups = bloodGroupService.getActive(null)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BloodGroupResponseDTO>>builder()
                .success(true)
                .message("Active blood groups retrieved successfully.")
                .data(activeBloodGroups)
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<BloodGroupResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody BloodGroupRequestDTO dto) {

        Admin admin = getCurrentAdmin();

        BloodGroup updated = bloodGroupService.update(
                id,
                mapToEntity(dto),
                admin.getId()
        );

        return ApiResponse.<BloodGroupResponseDTO>builder()
                .success(true)
                .message("Blood group updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }
    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {

        Admin admin = getCurrentAdmin();

        bloodGroupService.delete(id, admin.getId());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Blood group deleted successfully.")
                .build();
    }

    // ================= MAPPERS =================

    private BloodGroup mapToEntity(BloodGroupRequestDTO dto) {

        BloodGroup bg = new BloodGroup();
        bg.setType(dto.getType());
        bg.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return bg;
    }

    private BloodGroupResponseDTO mapToResponse(BloodGroup bg) {

        BloodGroupResponseDTO dto = new BloodGroupResponseDTO();

        dto.setId(bg.getId());
        dto.setType(bg.getType());
        dto.setIsActive(bg.getIsActive());
        dto.setCreatedAt(bg.getCreatedAt());

        if (bg.getAdmin() != null) {
            dto.setAdminId(bg.getAdmin().getId());
        }

        return dto;
    }
    @GetMapping("/deleted")
    public ApiResponse<List<BloodGroupResponseDTO>> getDeleted() {

        Admin admin = getCurrentAdmin();

        List<BloodGroupResponseDTO> deleted = bloodGroupService
                .getDeleted(admin.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BloodGroupResponseDTO>>builder()
                .success(true)
                .message("Deleted blood groups retrieved successfully.")
                .data(deleted)
                .build();
    }
    @PutMapping("/restore/{id}")
    public ApiResponse<BloodGroupResponseDTO> restore(@PathVariable Long id) {

        BloodGroup restored = bloodGroupService.restore(id);

        return ApiResponse.<BloodGroupResponseDTO>builder()
                .success(true)
                .message("Blood group restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(@PathVariable Long id) {

        bloodGroupService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Blood group permanently deleted successfully.")
                .build();
    }
}