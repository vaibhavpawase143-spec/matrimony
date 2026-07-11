package com.example.service;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.model.SubscriptionPlan;
import com.example.model.User;
import com.example.model.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    // =====================================
    // SUBSCRIBE
    // =====================================

    SubscriptionResponseDto subscribeUser(UserSubscriptionRequestDTO requestDto);
    UserSubscription activateSubscription(
            User user,
            SubscriptionPlan plan
    );
    // =====================================
    // CRUD
    // =====================================

    UserSubscription create(UserSubscription subscription);

    Optional<UserSubscription> getById(Long id);

    List<UserSubscription> getAll();

    // =====================================
    // CURRENT USER
    // =====================================

    UserSubscription getMySubscription();

    // =====================================
    // USER
    // =====================================

    Optional<UserSubscription> getActiveByUser(Long userId);

    boolean hasActiveSubscription(Long userId);

    List<UserSubscription> getByUser(Long userId);

    List<UserSubscription> getInactiveByUser(Long userId);

    // =====================================
    // PLAN
    // =====================================

    List<UserSubscription> getByPlan(Long planId);

    List<UserSubscription> getActiveByPlan(Long planId);

    List<UserSubscription> getAllInactive();

    // =====================================
    // CANCEL
    // =====================================

    void deactivate(Long id);
// =====================================
// PREMIUM CHECK
// =====================================
List<UserSubscription> getMySubscriptionHistory();
    boolean isCurrentUserPremium();
    Optional<UserSubscription> getSubscriptionById(Long id);
}