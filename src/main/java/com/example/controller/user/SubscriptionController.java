package com.example.controller.user; // user folder

import com.example.model.UserSubscription;
import com.example.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // Subscribe user to a plan
    @PostMapping("/subscribe")
    public ResponseEntity<UserSubscription> subscribeUser(
            @RequestParam Long userId,
            @RequestParam Long planId
    ) {
        UserSubscription subscription = subscriptionService.subscribeUser(userId, planId);
        return ResponseEntity.ok(subscription);
    }

    // Get active subscription of a user
    @GetMapping("/active/{userId}")
    public ResponseEntity<UserSubscription> getActiveSubscription(@PathVariable Long userId) {
        UserSubscription subscription = subscriptionService.getActiveSubscription(userId);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Check if user's subscription is active
    @GetMapping("/is-active/{userId}")
    public ResponseEntity<Boolean> isSubscriptionActive(@PathVariable Long userId) {
        boolean isActive = subscriptionService.isSubscriptionActive(userId);
        return ResponseEntity.ok(isActive);
    }
}