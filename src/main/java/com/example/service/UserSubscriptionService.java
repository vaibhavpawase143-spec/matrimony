package com.example.service;

import com.example.model.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionService {

    // ✅ Create / Activate subscription
    UserSubscription create(UserSubscription subscription);

    // 🔍 Get by ID
    Optional<UserSubscription> getById(Long id);

    // 🔍 Active subscription
    Optional<UserSubscription> getActiveByUser(Long userId);

    // 🔍 Check active
    boolean hasActiveSubscription(Long userId);

    // 🔍 History (all)
    List<UserSubscription> getByUser(Long userId);

    // 🔍 Inactive history
    List<UserSubscription> getInactiveByUser(Long userId);

    // 🔍 By plan
    List<UserSubscription> getByPlan(Long planId);

    // 🔍 Active by plan
    List<UserSubscription> getActiveByPlan(Long planId);

    // 🔍 All inactive
    List<UserSubscription> getAllInactive();

    // ❌ Deactivate
    void deactivate(Long id);

    // 🔍 Get all
    List<UserSubscription> getAll();
}