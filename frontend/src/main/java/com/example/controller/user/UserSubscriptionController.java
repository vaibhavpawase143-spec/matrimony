package com.example.controller.user;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.UserSubscriptionResponseDTO;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;
import com.example.service.SubscriptionPlanService;
import com.example.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-subscriptions")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService service;
    private final SubscriptionPlanService planService;

    // =========================
    // ✅ BUY / ACTIVATE PLAN
    // =========================
    @PostMapping
    public UserSubscriptionResponseDTO create(
            @Valid @RequestBody UserSubscriptionRequestDTO dto) {

        SubscriptionPlan plan = planService.getById(dto.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        UserSubscription sub = new UserSubscription();

        User user = new User();
        user.setId(dto.getUserId());
        sub.setUser(user);

        sub.setSubscriptionPlan(plan);

        // 🔥 Calculate dates
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(plan.getDuration());

        sub.setStartDate(start);
        sub.setEndDate(end);
        sub.setStatus("ACTIVE");

        UserSubscription saved = service.create(sub);

        return mapToResponse(saved);
    }

    // =========================
    // 🔍 GET ACTIVE SUBSCRIPTION
    // =========================
    @GetMapping("/user/{userId}/active")
    public UserSubscriptionResponseDTO getActive(@PathVariable Long userId) {

        UserSubscription sub = service.getActiveByUser(userId)
                .orElseThrow(() -> new RuntimeException("No active subscription"));

        return mapToResponse(sub);
    }

    // =========================
    // 🔍 CHECK ACTIVE
    // =========================
    @GetMapping("/user/{userId}/has-active")
    public Boolean hasActive(@PathVariable Long userId) {
        return service.hasActiveSubscription(userId);
    }

    // =========================
    // 🔍 HISTORY
    // =========================
    @GetMapping("/user/{userId}")
    public List<UserSubscriptionResponseDTO> getHistory(@PathVariable Long userId) {

        return service.getByUser(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 BY PLAN
    // =========================
    @GetMapping("/plan/{planId}")
    public List<UserSubscriptionResponseDTO> getByPlan(@PathVariable Long planId) {

        return service.getByPlan(planId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ❌ DEACTIVATE
    // =========================
    @DeleteMapping("/{id}")
    public String deactivate(@PathVariable Long id) {

        service.deactivate(id);

        return "Subscription deactivated";
    }

    // =========================
    // 🔍 GET ALL (ADMIN)
    // =========================
    @GetMapping
    public List<UserSubscriptionResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private UserSubscriptionResponseDTO mapToResponse(UserSubscription sub) {

        return UserSubscriptionResponseDTO.builder()
                .id(sub.getId())
                .userId(sub.getUser() != null ? sub.getUser().getId() : null)
                .userName(sub.getUser() != null ? sub.getUser().getFullName() : null)
                .planId(sub.getSubscriptionPlan() != null ? sub.getSubscriptionPlan().getId() : null)
                .planName(sub.getSubscriptionPlan() != null ? sub.getSubscriptionPlan().getName() : null)
                .startDate(sub.getStartDate())
                .endDate(sub.getEndDate())
                .isActive(sub.getIsActive())
                .status(sub.getStatus())
                .createdAt(sub.getCreatedAt())
                .updatedAt(sub.getUpdatedAt())
                .build();
    }
}