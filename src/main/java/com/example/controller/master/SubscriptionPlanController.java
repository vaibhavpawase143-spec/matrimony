package com.example.controller.master; // master folder

import com.example.model.SubscriptionPlan;
import com.example.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService subscriptionPlanService;

    // Create new Subscription Plan
    @PostMapping
    public ResponseEntity<SubscriptionPlan> create(@RequestBody SubscriptionPlan plan) {
        return ResponseEntity.ok(subscriptionPlanService.create(plan));
    }

    // Get all plans
    @GetMapping
    public ResponseEntity<List<SubscriptionPlan>> getAll() {
        return ResponseEntity.ok(subscriptionPlanService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionPlan>> getAllActive() {
        return ResponseEntity.ok(subscriptionPlanService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<SubscriptionPlan>> getAllInactive() {
        return ResponseEntity.ok(subscriptionPlanService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> getById(@PathVariable Long id) {
        Optional<SubscriptionPlan> plan = subscriptionPlanService.getById(id);
        return plan.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> update(@PathVariable Long id, @RequestBody SubscriptionPlan updated) {
        return ResponseEntity.ok(subscriptionPlanService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<SubscriptionPlan>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(subscriptionPlanService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<SubscriptionPlan>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(subscriptionPlanService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<SubscriptionPlan>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(subscriptionPlanService.getActiveByAdmin(adminId));
    }
}