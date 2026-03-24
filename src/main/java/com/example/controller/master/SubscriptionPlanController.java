package com.example.controller.master;

import com.example.dto.request.SubscriptionPlanRequestDTO;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.model.Admin;
import com.example.model.SubscriptionPlan;
import com.example.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService service;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public SubscriptionPlanResponseDTO create(
            @Valid @RequestBody SubscriptionPlanRequestDTO dto) {

        SubscriptionPlan entity = mapToEntity(dto);

        SubscriptionPlan saved = service.create(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public SubscriptionPlanResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanRequestDTO dto) {

        SubscriptionPlan entity = mapToEntity(dto);

        SubscriptionPlan updated = service.update(id, entity);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public SubscriptionPlanResponseDTO getById(@PathVariable Long id) {

        SubscriptionPlan plan = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        return mapToResponse(plan);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<SubscriptionPlanResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<SubscriptionPlanResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<SubscriptionPlanResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<SubscriptionPlanResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<SubscriptionPlanResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE (SOFT)
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Subscription plan deactivated successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private SubscriptionPlan mapToEntity(SubscriptionPlanRequestDTO dto) {

        SubscriptionPlan plan = new SubscriptionPlan();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        plan.setAdmin(admin);

        plan.setName(dto.getName());
        plan.setPrice(dto.getPrice());
        plan.setDuration(dto.getDuration());
        plan.setDescription(dto.getDescription());

        if (dto.getIsActive() != null) {
            plan.setIsActive(dto.getIsActive());
        }

        return plan;
    }

    private SubscriptionPlanResponseDTO mapToResponse(SubscriptionPlan plan) {

        return SubscriptionPlanResponseDTO.builder()
                .id(plan.getId())
                .adminId(plan.getAdmin() != null ? plan.getAdmin().getId() : null)
                .adminName(plan.getAdmin() != null ? plan.getAdmin().getName() : null)
                .name(plan.getName())
                .price(plan.getPrice())
                .duration(plan.getDuration())
                .description(plan.getDescription())
                .isActive(plan.getIsActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}