package com.example.controller.master;

import com.example.dto.request.FamilyStatusRequestDto;
import com.example.dto.response.FamilyStatusResponseDto;
import com.example.model.Admin;
import com.example.model.FamilyStatus;
import com.example.service.FamilyStatusService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/family-status")
public class FamilyStatusController {

    private final FamilyStatusService service;

    public FamilyStatusController(FamilyStatusService service) {
        this.service = service;
    }

    // ✅ Create
    @PostMapping
    public FamilyStatusResponseDto create(@Valid @RequestBody FamilyStatusRequestDto dto) {
        return mapToResponse(service.create(mapToEntity(dto)));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public FamilyStatusResponseDto update(@PathVariable Long id,
                                          @Valid @RequestBody FamilyStatusRequestDto dto) {
        return mapToResponse(service.update(id, mapToEntity(dto)));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "FamilyStatus deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public FamilyStatusResponseDto getById(@PathVariable Long id) {
        return service.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("FamilyStatus not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<FamilyStatusResponseDto> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<FamilyStatusResponseDto> getActive() {
        return service.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<FamilyStatusResponseDto> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<FamilyStatusResponseDto> search(@RequestParam String keyword) {
        return service.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING (SAFE VERSION)
    // =========================

    private FamilyStatus mapToEntity(FamilyStatusRequestDto dto) {

        FamilyStatus entity = new FamilyStatus();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    private FamilyStatusResponseDto mapToResponse(FamilyStatus entity) {

        return FamilyStatusResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .adminId(entity.getAdminId()) // 🔥 optimized
                .build();
    }
}