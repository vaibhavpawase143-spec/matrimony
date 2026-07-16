package com.example.repository;

import com.example.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    // 🔍 Active subscription for a user
    Optional<UserSubscription> findByUserIdAndIsActiveTrue(Long userId);

    // 🔍 Check if user has active subscription
    boolean existsByUserIdAndIsActiveTrue(Long userId);

    // 🔍 All subscriptions of a user (history)
    List<UserSubscription> findByUserId(Long userId);

    // 🔍 Subscriptions by plan
    List<UserSubscription> findBySubscriptionPlanId(Long planId);

    // 🔍 Active subscriptions by plan
    List<UserSubscription> findBySubscriptionPlanIdAndIsActiveTrue(Long planId);
}