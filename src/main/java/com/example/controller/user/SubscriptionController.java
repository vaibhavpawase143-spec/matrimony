package com.example.controller.user;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.model.SubscriptionPlan;
import com.example.model.UserSubscription;
import com.example.service.SubscriptionService;
import com.example.service.SubscriptionPlanService;

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

    // ✅ 1. Get all plans
    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAll());
    }

    // ✅ 2. Get plan by ID
    @GetMapping("/plan/{id}")
    public ResponseEntity<SubscriptionPlan> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(
                subscriptionPlanService.getById(id)
                        .orElseThrow(() -> new RuntimeException("Plan not found"))
        );
    }

    // ✅ 3. Subscribe user
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResponseDto> subscribeUser(
            @RequestBody UserSubscriptionRequestDTO requestDto) {

        return ResponseEntity.ok(subscriptionService.subscribeUser(requestDto));
    }

    // ✅ 4. Get user subscription
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSubscription> getUserSubscription(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                subscriptionService.getActiveByUser(userId)
                        .orElseThrow(() -> new RuntimeException("No active subscription"))
        );
    }

    // ✅ 5. Cancel subscription
    @PutMapping("/cancel/{userId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long userId) {

        UserSubscription sub = subscriptionService.getActiveByUser(userId)
                .orElseThrow(() -> new RuntimeException("No active subscription"));

        subscriptionService.deactivate(sub.getId());

        return ResponseEntity.ok("Subscription cancelled successfully");
    }
}