package com.example.controller.master;

import com.example.dto.request.MotherTongueRequestDTO;
import com.example.dto.response.MotherTongueResponseDTO;
import com.example.model.Admin;
import com.example.model.MotherTongue;
import com.example.service.MotherTongueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mother-tongues")
@RequiredArgsConstructor
public class MotherTongueController {

    private final MotherTongueService motherTongueService;

    // ✅ Create
    @PostMapping
    public MotherTongueResponseDTO create(@Valid @RequestBody MotherTongueRequestDTO dto) {

        MotherTongue entity = mapToEntity(dto);
        MotherTongue saved = motherTongueService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public MotherTongueResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody MotherTongueRequestDTO dto
    ) {
        MotherTongue entity = mapToEntity(dto);
        MotherTongue updated = motherTongueService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        motherTongueService.delete(id);
        return "MotherTongue deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public MotherTongueResponseDTO getById(@PathVariable Long id) {
        MotherTongue data = motherTongueService.getById(id)
                .orElseThrow(() -> new RuntimeException("MotherTongue not found"));

        return mapToResponse(data);
    }

    // 🔍 Get by Admin
    @GetMapping("/admin/{adminId}")
    public List<MotherTongueResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return motherTongueService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<MotherTongueResponseDTO> getActive(@PathVariable Long adminId) {
        return motherTongueService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive by Admin
    @GetMapping("/admin/{adminId}/inactive")
    public List<MotherTongueResponseDTO> getInactive(@PathVariable Long adminId) {
        return motherTongueService.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/admin/{adminId}/search")
    public List<MotherTongueResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword
    ) {
        return motherTongueService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 Mapping Methods
    // ===============================

    private MotherTongue mapToEntity(MotherTongueRequestDTO dto) {

        MotherTongue entity = new MotherTongue();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private MotherTongueResponseDTO mapToResponse(MotherTongue entity) {

        MotherTongueResponseDTO dto = new MotherTongueResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}