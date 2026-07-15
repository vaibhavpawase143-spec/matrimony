package com.example.controller.master;

import com.example.dto.request.FieldOfStudyRequestDTO;
import com.example.dto.response.FieldOfStudyResponseDTO;
import com.example.model.Admin;
import com.example.model.FieldOfStudy;
import com.example.service.FieldOfStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fields-of-study")
@RequiredArgsConstructor
public class FieldOfStudyController {

    private final FieldOfStudyService fieldOfStudyService;

    // ✅ Create
    @PostMapping
    public FieldOfStudyResponseDTO create(@RequestBody FieldOfStudyRequestDTO dto) {

        FieldOfStudy entity = mapToEntity(dto);

        FieldOfStudy saved = fieldOfStudyService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public FieldOfStudyResponseDTO update(
            @PathVariable Long id,
            @RequestBody FieldOfStudyRequestDTO dto
    ) {
        FieldOfStudy entity = mapToEntity(dto);

        FieldOfStudy updated = fieldOfStudyService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        fieldOfStudyService.delete(id);
        return "FieldOfStudy deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public FieldOfStudyResponseDTO getById(@PathVariable Long id) {
        FieldOfStudy field = fieldOfStudyService.getById(id)
                .orElseThrow(() -> new RuntimeException("FieldOfStudy not found"));

        return mapToResponse(field);
    }

    // 🔍 Get all
    @GetMapping
    public List<FieldOfStudyResponseDTO> getAll() {
        return fieldOfStudyService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Get active
    @GetMapping("/active")
    public List<FieldOfStudyResponseDTO> getActive() {
        return fieldOfStudyService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Get by admin
    @GetMapping("/admin/{adminId}")
    public List<FieldOfStudyResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return fieldOfStudyService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<FieldOfStudyResponseDTO> search(@RequestParam String keyword) {
        return fieldOfStudyService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 MAPPING METHODS
    // ===============================

    private FieldOfStudy mapToEntity(FieldOfStudyRequestDTO dto) {

        FieldOfStudy entity = new FieldOfStudy();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private FieldOfStudyResponseDTO mapToResponse(FieldOfStudy entity) {

        FieldOfStudyResponseDTO dto = new FieldOfStudyResponseDTO();

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