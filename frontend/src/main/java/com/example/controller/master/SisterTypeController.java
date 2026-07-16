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

    @PostMapping
    public SisterTypeResponseDTO create(@Valid @RequestBody SisterTypeRequestDTO dto) {
        SisterType entity = mapToEntity(dto);
        SisterType saved = service.save(entity);
        return mapToResponse(saved);
    }

    @PutMapping("/{id}")
    public SisterTypeResponseDTO update(@PathVariable Long id,
                                        @Valid @RequestBody SisterTypeRequestDTO dto) {

        SisterType existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SisterType not found"));

        updateEntity(existing, dto);
        SisterType updated = service.save(existing);

        return mapToResponse(updated);
    }

    @GetMapping("/{id}")
    public SisterTypeResponseDTO getById(@PathVariable Long id) {
        SisterType s = service.getById(id)
                .orElseThrow(() -> new RuntimeException("SisterType not found"));
        return mapToResponse(s);
    }

    @GetMapping
    public List<SisterTypeResponseDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "SisterType deleted successfully";
    }

    // ================= MAPPING =================

    private SisterType mapToEntity(SisterTypeRequestDTO dto) {
        SisterType s = new SisterType();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        s.setAdmin(admin);

        s.setValue(dto.getValue());
        if (dto.getIsActive() != null) s.setIsActive(dto.getIsActive());

        return s;
    }

    private void updateEntity(SisterType s, SisterTypeRequestDTO dto) {
        if (dto.getValue() != null) s.setValue(dto.getValue());
        if (dto.getIsActive() != null) s.setIsActive(dto.getIsActive());
    }

    private SisterTypeResponseDTO mapToResponse(SisterType s) {
        return SisterTypeResponseDTO.builder()
                .id(s.getId())
                .adminId(s.getAdmin() != null ? s.getAdmin().getId() : null)

                // ✅ FIX (remove lazy access)
                .adminName(null)

                .value(s.getValue())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}