package com.example.controller.master;

import com.example.dto.request.QualificationRequestDTO;
import com.example.dto.response.QualificationResponseDTO;
import com.example.model.Admin;
import com.example.model.Qualification;
import com.example.service.QualificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/qualifications")
@RequiredArgsConstructor
public class QualificationController {

    private final QualificationService service;

    // =========================
    // ✅ CREATE
    // =========================
    @PostMapping
    public QualificationResponseDTO create(@Valid @RequestBody QualificationRequestDTO dto) {

        Qualification entity = mapToEntity(dto);

        Qualification saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // ✅ UPDATE
    // =========================
    @PutMapping("/{id}")
    public QualificationResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody QualificationRequestDTO dto) {

        Qualification existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Qualification not found"));

        updateEntity(existing, dto);

        Qualification updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // ✅ GET BY ID
    // =========================
    @GetMapping("/{id}")
    public QualificationResponseDTO getById(@PathVariable Long id) {

        Qualification q = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Qualification not found"));

        return mapToResponse(q);
    }

    // =========================
    // ✅ GET ALL
    // =========================
    @GetMapping
    public List<QualificationResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<QualificationResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<QualificationResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<QualificationResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<QualificationResponseDTO> search(
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
        return "Qualification deleted successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private Qualification mapToEntity(QualificationRequestDTO dto) {

        Qualification q = new Qualification();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        q.setAdmin(admin);

        q.setName(dto.getName());

        if (dto.getIsActive() != null) {
            q.setIsActive(dto.getIsActive());
        }

        return q;
    }

    private void updateEntity(Qualification q, QualificationRequestDTO dto) {

        // 🔥 Do NOT change admin (important)

        if (dto.getName() != null) {
            q.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            q.setIsActive(dto.getIsActive());
        }
    }

    private QualificationResponseDTO mapToResponse(Qualification q) {

        return QualificationResponseDTO.builder()
                .id(q.getId())
                .adminId(q.getAdmin() != null ? q.getAdmin().getId() : null)
                .adminName(q.getAdmin() != null ? q.getAdmin().getName() : null) // adjust field
                .name(q.getName())
                .isActive(q.getIsActive())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }
}