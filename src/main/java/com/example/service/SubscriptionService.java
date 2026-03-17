package com.example.service;

import com.example.model.UserSubscription;

public interface SubscriptionService {

    UserSubscription subscribeUser(Long userId, Long planId);

    UserSubscription getActiveSubscription(Long userId);

    boolean isSubscriptionActive(Long userId);
}