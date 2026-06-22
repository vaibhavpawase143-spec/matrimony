package com.example.controller.user;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.dto.response.UserSubscriptionResponseDTO;
import com.example.model.SubscriptionPlan;
import com.example.model.UserSubscription;
import com.example.service.SubscriptionPlanService;
import com.example.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionController(
            SubscriptionService subscriptionService,
            SubscriptionPlanService subscriptionPlanService) {

        this.subscriptionService = subscriptionService;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    // =====================================================
    // GET ALL PLANS
    // =====================================================

    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {

        return ResponseEntity.ok(
                subscriptionPlanService.getAll()
        );
    }

    // =====================================================
    // GET PLAN BY ID
    // =====================================================

    @GetMapping("/plan/{id}")
    public ResponseEntity<SubscriptionPlan> getPlanById(
            @PathVariable Long id) {

        SubscriptionPlan plan =
                subscriptionPlanService.getById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Plan not found"));

        return ResponseEntity.ok(plan);
    }

    // =====================================================
    // BUY SUBSCRIPTION
    // =====================================================

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResponseDto> subscribeUser(
            @RequestBody UserSubscriptionRequestDTO requestDto) {

        return ResponseEntity.ok(
                subscriptionService.subscribeUser(requestDto)
        );
    }

    // =====================================================
// MY ACTIVE SUBSCRIPTION
// =====================================================

    @GetMapping("/me")
    public ResponseEntity<UserSubscriptionResponseDTO> getMySubscription() {

        UserSubscription subscription =
                subscriptionService.getMySubscription();

        UserSubscriptionResponseDTO dto =
                UserSubscriptionResponseDTO.builder()
                        .id(subscription.getId())
                        .userId(subscription.getUser().getId())
                        .userName(subscription.getUser().getFullName())
                        .planId(subscription.getSubscriptionPlan().getId())
                        .planName(subscription.getSubscriptionPlan().getName())
                        .startDate(subscription.getStartDate())
                        .endDate(subscription.getEndDate())
                        .isActive(subscription.getIsActive())
                        .status(subscription.getStatus())
                        .createdAt(subscription.getCreatedAt())
                        .updatedAt(subscription.getUpdatedAt())
                        .build();

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/history")
    public ResponseEntity<List<UserSubscriptionResponseDTO>> getHistory() {

        List<UserSubscriptionResponseDTO> history =
                subscriptionService
                        .getMySubscriptionHistory()
                        .stream()
                        .map(subscription ->

                                UserSubscriptionResponseDTO
                                        .builder()
                                        .id(subscription.getId())
                                        .userId(subscription.getUser().getId())
                                        .userName(subscription.getUser().getFullName())
                                        .planId(subscription.getSubscriptionPlan().getId())
                                        .planName(subscription.getSubscriptionPlan().getName())
                                        .startDate(subscription.getStartDate())
                                        .endDate(subscription.getEndDate())
                                        .status(subscription.getStatus())
                                        .isActive(subscription.getIsActive())
                                        .createdAt(subscription.getCreatedAt())
                                        .updatedAt(subscription.getUpdatedAt())
                                        .build()

                        )
                        .toList();

        return ResponseEntity.ok(history);

    }
    // =====================================================
    // CANCEL MY SUBSCRIPTION
    // =====================================================

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelSubscription() {

        UserSubscription subscription =
                subscriptionService.getMySubscription();

        subscriptionService.deactivate(subscription.getId());

        return ResponseEntity.ok(
                "Subscription cancelled successfully"
        );
    }
}