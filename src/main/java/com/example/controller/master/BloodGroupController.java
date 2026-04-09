package com.example.controller.master;

import com.example.dto.request.BloodGroupRequestDTO;
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
    public BloodGroupResponseDTO create(@Valid @RequestBody BloodGroupRequestDTO dto) {

        Admin admin = getCurrentAdmin();

        BloodGroup bloodGroup = mapToEntity(dto);
        BloodGroup saved = bloodGroupService.create(bloodGroup, admin.getId());

        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public BloodGroupResponseDTO getById(@PathVariable Long id) {

        Admin admin = getCurrentAdmin();

        BloodGroup bg = bloodGroupService.getById(id, admin.getId());
        return mapToResponse(bg);
    }

    // ================= GET ALL =================
    @GetMapping
    public List<BloodGroupResponseDTO> getAll() {

        Admin admin = getCurrentAdmin();

        return bloodGroupService.getAll(admin.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<BloodGroupResponseDTO> getActive() {

        Admin admin = getCurrentAdmin();

        return bloodGroupService.getActive(admin.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public BloodGroupResponseDTO update(@PathVariable Long id,
                                        @Valid @RequestBody BloodGroupRequestDTO dto) {

        Admin admin = getCurrentAdmin();

        BloodGroup updated = bloodGroupService.update(id, mapToEntity(dto), admin.getId());
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        Admin admin = getCurrentAdmin();

        bloodGroupService.delete(id, admin.getId());
        return "Blood group deleted successfully";
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
}