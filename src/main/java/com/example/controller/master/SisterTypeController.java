package com.example.controller.master;

import com.example.dto.request.SisterTypeRequestDTO;
import com.example.dto.response.SisterTypeResponseDTO;
import com.example.model.Admin;
import com.example.model.SisterType;
import com.example.service.SisterTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/sister-types")
@RequiredArgsConstructor
public class SisterTypeController {

    private final SisterTypeService service;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public SisterTypeResponseDTO create(@Valid @RequestBody SisterTypeRequestDTO dto) {

        SisterType entity = mapToEntity(dto);
        SisterType saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public SisterTypeResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SisterTypeRequestDTO dto) {

        SisterType existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SisterType not found"));

        updateEntity(existing, dto);

        SisterType updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public SisterTypeResponseDTO getById(@PathVariable Long id) {

        SisterType s = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SisterType not found"));

        return mapToResponse(s);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<SisterTypeResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<SisterTypeResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<SisterTypeResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<SisterTypeResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<SisterTypeResponseDTO> search(
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
        return "SisterType deleted successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private SisterType mapToEntity(SisterTypeRequestDTO dto) {

        SisterType s = new SisterType();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        s.setAdmin(admin);

        s.setValue(dto.getValue());

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }

        return s;
    }

    private void updateEntity(SisterType s, SisterTypeRequestDTO dto) {

        // 🚫 DO NOT update admin

        if (dto.getValue() != null) {
            s.setValue(dto.getValue());
        }

        if (dto.getIsActive() != null) {
            s.setIsActive(dto.getIsActive());
        }
    }

    private SisterTypeResponseDTO mapToResponse(SisterType s) {

        return SisterTypeResponseDTO.builder()
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