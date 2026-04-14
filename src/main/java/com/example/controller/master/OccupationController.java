package com.example.controller.master;

import com.example.dto.request.OccupationRequestDTO;
import com.example.dto.response.OccupationResponseDTO;
import com.example.model.Admin;
import com.example.model.Occupation;
import com.example.service.OccupationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/occupations")
@RequiredArgsConstructor
public class OccupationController {

    private final OccupationService occupationService;
    // ADD THIS METHOD ONLY (rest is already correct)

    @GetMapping
    public List<OccupationResponseDTO> getAll() {
        return occupationService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    // ✅ Create
    @PostMapping
    public OccupationResponseDTO create(@Valid @RequestBody OccupationRequestDTO dto) {

        Occupation entity = mapToEntity(dto);
        Occupation saved = occupationService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public OccupationResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody OccupationRequestDTO dto
    ) {
        Occupation entity = mapToEntity(dto);
        Occupation updated = occupationService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        occupationService.delete(id);
        return "Occupation deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public OccupationResponseDTO getById(@PathVariable Long id) {
        Occupation data = occupationService.getById(id)
                .orElseThrow(() -> new RuntimeException("Occupation not found"));

        return mapToResponse(data);
    }

    // 🔍 Get by Admin
    @GetMapping("/admin/{adminId}")
    public List<OccupationResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return occupationService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/admin/{adminId}/active")
    public List<OccupationResponseDTO> getActive(@PathVariable Long adminId) {
        return occupationService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/admin/{adminId}/inactive")
    public List<OccupationResponseDTO> getInactive(@PathVariable Long adminId) {
        return occupationService.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/admin/{adminId}/search")
    public List<OccupationResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword
    ) {
        return occupationService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 Mapping Methods
    // ===============================

    private Occupation mapToEntity(OccupationRequestDTO dto) {

        Occupation entity = new Occupation();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private OccupationResponseDTO mapToResponse(Occupation entity) {

        OccupationResponseDTO dto = new OccupationResponseDTO();

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