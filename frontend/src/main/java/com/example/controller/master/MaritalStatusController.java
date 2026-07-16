package com.example.controller.master;

import com.example.dto.request.MaritalStatusRequestDTO;
import com.example.dto.response.MaritalStatusResponseDTO;
import com.example.model.Admin;
import com.example.model.MaritalStatus;
import com.example.service.MaritalStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/marital-status")
@RequiredArgsConstructor
public class MaritalStatusController {

    private final MaritalStatusService maritalStatusService;

    // ✅ Create
    @PostMapping
    public MaritalStatusResponseDTO create(@Valid @RequestBody MaritalStatusRequestDTO dto) {

        MaritalStatus entity = mapToEntity(dto);
        MaritalStatus saved = maritalStatusService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public MaritalStatusResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody MaritalStatusRequestDTO dto
    ) {
        MaritalStatus entity = mapToEntity(dto);
        MaritalStatus updated = maritalStatusService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        maritalStatusService.delete(id);
        return "MaritalStatus deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public MaritalStatusResponseDTO getById(@PathVariable Long id) {
        MaritalStatus data = maritalStatusService.getById(id)
                .orElseThrow(() -> new RuntimeException("MaritalStatus not found"));

        return mapToResponse(data);
    }

    // 🔍 Get all
    @GetMapping
    public List<MaritalStatusResponseDTO> getAll() {
        return maritalStatusService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<MaritalStatusResponseDTO> getActive() {
        return maritalStatusService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By admin
    @GetMapping("/admin/{adminId}")
    public List<MaritalStatusResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return maritalStatusService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<MaritalStatusResponseDTO> search(@RequestParam String keyword) {
        return maritalStatusService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 Mapping Methods
    // ===============================

    private MaritalStatus mapToEntity(MaritalStatusRequestDTO dto) {

        MaritalStatus entity = new MaritalStatus();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private MaritalStatusResponseDTO mapToResponse(MaritalStatus entity) {

        MaritalStatusResponseDTO dto = new MaritalStatusResponseDTO();

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