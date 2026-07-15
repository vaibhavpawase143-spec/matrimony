package com.example.service;

import com.example.dto.request.UserSubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDto;
import com.example.model.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    // ✅ NEW METHOD
    SubscriptionResponseDto subscribeUser(UserSubscriptionRequestDTO requestDto);

    UserSubscription create(UserSubscription subscription);

    Optional<UserSubscription> getById(Long id);

    Optional<UserSubscription> getActiveByUser(Long userId);

    boolean hasActiveSubscription(Long userId);

    List<UserSubscription> getByUser(Long userId);

    List<UserSubscription> getByPlan(Long planId);

    List<UserSubscription> getActiveByPlan(Long planId);

    void deactivate(Long id);

    List<UserSubscription> getAll();
}