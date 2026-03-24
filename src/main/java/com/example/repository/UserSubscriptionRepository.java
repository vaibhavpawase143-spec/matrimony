package com.example.repository;

import com.example.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    // 🔍 Get all subscriptions of user (history)
    List<UserSubscription> findByUserId(Long userId);

    // 🔍 Get active subscription of user
    Optional<UserSubscription> findByUserIdAndIsActiveTrue(Long userId);

    // 🔍 Check if user has active subscription
    boolean existsByUserIdAndIsActiveTrue(Long userId);

    // 🔍 Get subscriptions by plan
    List<UserSubscription> findBySubscriptionPlanId(Long planId);

    // 🔍 Active subscriptions by plan
    List<UserSubscription> findBySubscriptionPlanIdAndIsActiveTrue(Long planId);

    // 🔍 Inactive subscriptions (all)
    List<UserSubscription> findByIsActiveFalse();

    // 🔍 Inactive subscriptions by user (history)
    List<UserSubscription> findByUserIdAndIsActiveFalse(Long userId);
}