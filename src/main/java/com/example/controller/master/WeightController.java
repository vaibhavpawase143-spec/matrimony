package com.example.controller.master;

import com.example.dto.request.WeightRequestDTO;
import com.example.dto.response.WeightResponseDTO;
import com.example.model.Admin;
import com.example.model.Weight;
import com.example.service.WeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/weights")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService service;

    // =========================
    // ✅ CREATE / UPDATE
    // =========================
    @PostMapping
    public WeightResponseDTO save(@Valid @RequestBody WeightRequestDTO dto) {

        Weight weight = mapToEntity(dto);

        Weight saved = service.save(weight);

        return mapToResponse(saved);
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @GetMapping("/{id}")
    public WeightResponseDTO getById(@PathVariable Long id) {

        Weight weight = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Weight not found"));

        return mapToResponse(weight);
    }

    // =========================
    // 🔍 GET ALL
    // =========================
    @GetMapping
    public List<WeightResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 GET BY ADMIN
    // =========================
    @GetMapping("/admin/{adminId}")
    public List<WeightResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 ACTIVE
    // =========================
    @GetMapping("/admin/{adminId}/active")
    public List<WeightResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 INACTIVE
    // =========================
    @GetMapping("/admin/{adminId}/inactive")
    public List<WeightResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<WeightResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ❌ DELETE (SOFT)
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        service.delete(id);

        return "Weight deactivated successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private Weight mapToEntity(WeightRequestDTO dto) {

        Weight weight = new Weight();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        weight.setAdmin(admin);

        weight.setValue(dto.getValue());

        if (dto.getIsActive() != null) {
            weight.setIsActive(dto.getIsActive());
        }

        return weight;
    }

    private WeightResponseDTO mapToResponse(Weight weight) {

        return WeightResponseDTO.builder()
                .id(weight.getId())
                .adminId(weight.getAdmin() != null ? weight.getAdmin().getId() : null)
                .adminName(weight.getAdmin() != null ? weight.getAdmin().getName() : null)
                .value(weight.getValue())
                .isActive(weight.getIsActive())
                .createdAt(weight.getCreatedAt())
                .updatedAt(weight.getUpdatedAt())
                .build();
    }
}