package com.example.controller.master;

import com.example.dto.request.SmokingRequestDTO;
import com.example.dto.response.SmokingResponseDTO;
import com.example.model.Admin;
import com.example.model.Smoking;
import com.example.service.SmokingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/smoking")
@RequiredArgsConstructor
public class SmokingController {

    private final SmokingService service;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public SmokingResponseDTO create(@Valid @RequestBody SmokingRequestDTO dto) {

        Smoking entity = mapToEntity(dto);
        Smoking saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public SmokingResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SmokingRequestDTO dto) {

        Smoking existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Smoking not found"));

        updateEntity(existing, dto);

        Smoking updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public SmokingResponseDTO getById(@PathVariable Long id) {

        Smoking s = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Smoking not found"));

        return mapToResponse(s);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<SmokingResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<SmokingResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<SmokingResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<SmokingResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<SmokingResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Smoking deleted successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private Smoking mapToEntity(SmokingRequestDTO dto) {

        Smoking s = new Smoking();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        s.setAdmin(admin);

        s.setValue(dto.getValue());

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }

        return s;
    }

    private void updateEntity(Smoking s, SmokingRequestDTO dto) {

        // 🚫 DO NOT update admin

        if (dto.getValue() != null) {
            s.setValue(dto.getValue());
        }

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }
    }

    private SmokingResponseDTO mapToResponse(Smoking s) {

        return SmokingResponseDTO.builder()
                .id(s.getId())
                .adminId(s.getAdmin() != null ? s.getAdmin().getId() : null)
                .adminName(s.getAdmin() != null ? s.getAdmin().getName() : null)
                .value(s.getValue())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}