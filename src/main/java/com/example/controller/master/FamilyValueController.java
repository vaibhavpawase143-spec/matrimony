package com.example.controller.master;

import com.example.dto.request.FamilyValueRequestDto;
import com.example.dto.response.FamilyValueResponseDto;
import com.example.model.FamilyValue;
import com.example.service.FamilyValueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/family-values")
public class FamilyValueController {

    private final FamilyValueService service;

    public FamilyValueController(FamilyValueService service) {
        this.service = service;
    }

    // ✅ Create
    @PostMapping
    public FamilyValueResponseDto create(@Valid @RequestBody FamilyValueRequestDto dto) {
        return mapToResponse(service.create(mapToEntity(dto)));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public FamilyValueResponseDto update(@PathVariable Long id,
                                         @Valid @RequestBody FamilyValueRequestDto dto) {
        return mapToResponse(service.update(id, mapToEntity(dto)));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "FamilyValue deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public FamilyValueResponseDto getById(@PathVariable Long id) {
        return service.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("FamilyValue not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<FamilyValueResponseDto> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<FamilyValueResponseDto> getActive() {
        return service.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<FamilyValueResponseDto> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<FamilyValueResponseDto> search(@RequestParam String keyword) {
        return service.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING
    // =========================

    private FamilyValue mapToEntity(FamilyValueRequestDto dto) {

        FamilyValue entity = new FamilyValue();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());
        entity.setAdminId(dto.getAdminId());

        return entity;
    }

    private FamilyValueResponseDto mapToResponse(FamilyValue entity) {

        return FamilyValueResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .adminId(entity.getAdminId())
                .build();
    }
}