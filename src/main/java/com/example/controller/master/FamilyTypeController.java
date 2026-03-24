package com.example.controller.master;

import com.example.dto.request.FamilyTypeRequestDto;
import com.example.dto.response.FamilyTypeResponseDto;
import com.example.model.Admin;
import com.example.model.FamilyType;
import com.example.service.FamilyTypeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/family-types")
public class FamilyTypeController {

    private final FamilyTypeService service;

    public FamilyTypeController(FamilyTypeService service) {
        this.service = service;
    }

    // ✅ Create
    @PostMapping
    public FamilyTypeResponseDto create(@Valid @RequestBody FamilyTypeRequestDto dto) {
        return mapToResponse(service.create(mapToEntity(dto)));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public FamilyTypeResponseDto update(@PathVariable Long id,
                                        @Valid @RequestBody FamilyTypeRequestDto dto) {
        return mapToResponse(service.update(id, mapToEntity(dto)));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "FamilyType deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public FamilyTypeResponseDto getById(@PathVariable Long id) {
        return service.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("FamilyType not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<FamilyTypeResponseDto> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<FamilyTypeResponseDto> getActive() {
        return service.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<FamilyTypeResponseDto> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<FamilyTypeResponseDto> search(@RequestParam String keyword) {
        return service.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING (SAFE)
    // =========================

    private FamilyType mapToEntity(FamilyTypeRequestDto dto) {

        FamilyType entity = new FamilyType();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    private FamilyTypeResponseDto mapToResponse(FamilyType entity) {

        return FamilyTypeResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getId() : null)
                .build();
    }
}