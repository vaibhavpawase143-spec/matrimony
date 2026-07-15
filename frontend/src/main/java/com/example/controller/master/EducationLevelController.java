package com.example.controller.master;

import com.example.dto.request.EducationLevelRequestDto;
import com.example.dto.response.EducationLevelResponseDto;
import com.example.model.Admin;
import com.example.model.EducationLevel;
import com.example.service.EducationLevelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/education-levels")
public class EducationLevelController {

    private final EducationLevelService educationLevelService;

    public EducationLevelController(EducationLevelService educationLevelService) {
        this.educationLevelService = educationLevelService;
    }

    // ✅ Create
    @PostMapping
    public EducationLevelResponseDto create(@Valid @RequestBody EducationLevelRequestDto dto) {
        EducationLevel entity = mapToEntity(dto);
        return mapToResponse(educationLevelService.create(entity));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public EducationLevelResponseDto update(@PathVariable Long id,
                                            @Valid @RequestBody EducationLevelRequestDto dto) {
        EducationLevel entity = mapToEntity(dto);
        return mapToResponse(educationLevelService.update(id, entity));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        educationLevelService.delete(id);
        return "Education level deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public EducationLevelResponseDto getById(@PathVariable Long id) {
        return educationLevelService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Education level not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<EducationLevelResponseDto> getAll() {
        return educationLevelService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<EducationLevelResponseDto> getActive() {
        return educationLevelService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/inactive")
    public List<EducationLevelResponseDto> getInactive() {
        return educationLevelService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<EducationLevelResponseDto> getByAdmin(@PathVariable Long adminId) {
        return educationLevelService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<EducationLevelResponseDto> getActiveByAdmin(@PathVariable Long adminId) {
        return educationLevelService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<EducationLevelResponseDto> search(@RequestParam String keyword) {
        return educationLevelService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search by Admin
    @GetMapping("/admin/{adminId}/search")
    public List<EducationLevelResponseDto> searchByAdmin(@PathVariable Long adminId,
                                                         @RequestParam String keyword) {
        return educationLevelService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    private EducationLevel mapToEntity(EducationLevelRequestDto dto) {
        EducationLevel entity = new EducationLevel();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    private EducationLevelResponseDto mapToResponse(EducationLevel entity) {
        EducationLevelResponseDto dto = new EducationLevelResponseDto();

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