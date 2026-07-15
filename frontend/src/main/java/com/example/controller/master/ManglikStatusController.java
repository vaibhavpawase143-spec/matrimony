package com.example.controller.master;

import com.example.dto.request.ManglikStatusRequestDTO;
import com.example.dto.response.ManglikStatusResponseDTO;
import com.example.model.Admin;
import com.example.model.ManglikStatus;
import com.example.service.ManglikStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manglik-status")
@RequiredArgsConstructor
public class ManglikStatusController {

    private final ManglikStatusService manglikStatusService;

    // ✅ Create
    @PostMapping
    public ManglikStatusResponseDTO create(@Valid @RequestBody ManglikStatusRequestDTO dto) {

        ManglikStatus entity = mapToEntity(dto);
        ManglikStatus saved = manglikStatusService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public ManglikStatusResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody ManglikStatusRequestDTO dto
    ) {
        ManglikStatus entity = mapToEntity(dto);
        ManglikStatus updated = manglikStatusService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        manglikStatusService.delete(id);
        return "ManglikStatus deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public ManglikStatusResponseDTO getById(@PathVariable Long id) {
        ManglikStatus data = manglikStatusService.getById(id)
                .orElseThrow(() -> new RuntimeException("ManglikStatus not found"));

        return mapToResponse(data);
    }

    // 🔍 Get all
    @GetMapping
    public List<ManglikStatusResponseDTO> getAll() {
        return manglikStatusService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<ManglikStatusResponseDTO> getActive() {
        return manglikStatusService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By admin
    @GetMapping("/admin/{adminId}")
    public List<ManglikStatusResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return manglikStatusService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<ManglikStatusResponseDTO> search(@RequestParam String keyword) {
        return manglikStatusService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 Mapping Methods
    // ===============================

    private ManglikStatus mapToEntity(ManglikStatusRequestDTO dto) {

        ManglikStatus entity = new ManglikStatus();

        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private ManglikStatusResponseDTO mapToResponse(ManglikStatus entity) {

        ManglikStatusResponseDTO dto = new ManglikStatusResponseDTO();

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