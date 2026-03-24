package com.example.controller.master;
import com.example.dto.request.BloodGroupRequestDTO;
import com.example.dto.response.BloodGroupResponseDTO;

import com.example.model.BloodGroup;
import com.example.service.BloodGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins/{adminId}/blood-groups")
@RequiredArgsConstructor
public class BloodGroupController {

    private final BloodGroupService bloodGroupService;

    // ================= CREATE =================
    @PostMapping
    public BloodGroupResponseDTO create(@PathVariable Long adminId,
                                        @Valid @RequestBody BloodGroupRequestDTO dto) {

        BloodGroup bloodGroup = mapToEntity(dto);

        BloodGroup saved = bloodGroupService.create(bloodGroup, adminId);

        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public BloodGroupResponseDTO getById(@PathVariable Long adminId,
                                         @PathVariable Long id) {

        return mapToResponse(bloodGroupService.getById(id, adminId));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<BloodGroupResponseDTO> getAll(@PathVariable Long adminId) {

        return bloodGroupService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public List<BloodGroupResponseDTO> getActive(@PathVariable Long adminId) {

        return bloodGroupService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public BloodGroupResponseDTO update(@PathVariable Long adminId,
                                        @PathVariable Long id,
                                        @Valid @RequestBody BloodGroupRequestDTO dto) {

        BloodGroup updated = bloodGroupService.update(id, mapToEntity(dto), adminId);

        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long adminId,
                         @PathVariable Long id) {

        bloodGroupService.delete(id, adminId);

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