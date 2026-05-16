package com.example.controller.master;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ReligionResponseDTO;
import com.example.model.Admin;
import com.example.model.Religion;
import com.example.service.ReligionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/religions")
@RequiredArgsConstructor
public class ReligionController {

    private final ReligionService service;

    // =========================
    // ✅ CREATE
    // =========================
    @PostMapping
    public ReligionResponseDTO create(@Valid @RequestBody ReligionRequestDTO dto) {

        Religion entity = mapToEntity(dto);
        Religion saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // ✅ UPDATE
    // =========================
    @PutMapping("/{id}")
    public ReligionResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody ReligionRequestDTO dto) {

        Religion existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Religion not found"));

        updateEntity(existing, dto);

        Religion updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // ✅ GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ReligionResponseDTO getById(@PathVariable Long id) {

        Religion r = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Religion not found"));

        return mapToResponse(r);
    }

    // =========================
    // ✅ GET ALL
    // =========================
    @GetMapping
    public List<ReligionResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<ReligionResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<ReligionResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<ReligionResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<ReligionResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ❌ DELETE
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Religion deleted successfully";
    }

    // =========================
    // 🔁 MAPPING
    // =========================

    private Religion mapToEntity(ReligionRequestDTO dto) {

        Religion r = new Religion();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        r.setAdmin(admin);

        r.setName(dto.getName());

        if (dto.getIsActive() != null) {
            r.setIsActive(dto.getIsActive());
        }

        return r;
    }

    private void updateEntity(Religion r, ReligionRequestDTO dto) {

        if (dto.getName() != null) {
            r.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            r.setIsActive(dto.getIsActive());
        }
    }

    private ReligionResponseDTO mapToResponse(Religion r) {

        return ReligionResponseDTO.builder()
                .id(r.getId())
                .adminId(r.getAdmin() != null ? r.getAdmin().getId() : null)

                // ✅ FIXED (no lazy load)
                .adminName(null)

                .name(r.getName())
                .isActive(r.getIsActive())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}