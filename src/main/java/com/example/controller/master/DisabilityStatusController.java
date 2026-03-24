package com.example.controller.master;

import com.example.dto.request.DisabilityStatusRequestDto;
import com.example.dto.response.DisabilityStatusResponseDto;
import com.example.model.Admin;
import com.example.model.DisabilityStatus;
import com.example.service.DisabilityStatusService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/disability-statuses")
public class DisabilityStatusController {

    private final DisabilityStatusService disabilityStatusService;

    public DisabilityStatusController(DisabilityStatusService disabilityStatusService) {
        this.disabilityStatusService = disabilityStatusService;
    }

    // ✅ Create
    @PostMapping
    public DisabilityStatusResponseDto create(@Valid @RequestBody DisabilityStatusRequestDto dto) {
        DisabilityStatus entity = mapToEntity(dto);
        return mapToResponse(disabilityStatusService.create(entity));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public DisabilityStatusResponseDto update(@PathVariable Long id,
                                              @Valid @RequestBody DisabilityStatusRequestDto dto) {
        DisabilityStatus entity = mapToEntity(dto);
        return mapToResponse(disabilityStatusService.update(id, entity));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        disabilityStatusService.delete(id);
        return "Disability status deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public DisabilityStatusResponseDto getById(@PathVariable Long id) {
        return disabilityStatusService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Disability status not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<DisabilityStatusResponseDto> getAll() {
        return disabilityStatusService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<DisabilityStatusResponseDto> getActive() {
        return disabilityStatusService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/inactive")
    public List<DisabilityStatusResponseDto> getInactive() {
        return disabilityStatusService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<DisabilityStatusResponseDto> getByAdmin(@PathVariable Long adminId) {
        return disabilityStatusService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<DisabilityStatusResponseDto> getActiveByAdmin(@PathVariable Long adminId) {
        return disabilityStatusService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<DisabilityStatusResponseDto> search(@RequestParam String keyword) {
        return disabilityStatusService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search by Admin
    @GetMapping("/admin/{adminId}/search")
    public List<DisabilityStatusResponseDto> searchByAdmin(@PathVariable Long adminId,
                                                           @RequestParam String keyword) {
        return disabilityStatusService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    private DisabilityStatus mapToEntity(DisabilityStatusRequestDto dto) {
        DisabilityStatus entity = new DisabilityStatus();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setValue(dto.getValue());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    private DisabilityStatusResponseDto mapToResponse(DisabilityStatus entity) {
        DisabilityStatusResponseDto dto = new DisabilityStatusResponseDto();

        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}