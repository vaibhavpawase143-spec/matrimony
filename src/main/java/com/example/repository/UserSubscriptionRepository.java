package com.example.repository;

import com.example.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    // 🔍 Get all subscriptions of user
    List<UserSubscription> findByUserId(Long userId);

    // 🔍 Get active subscription of user (FIXED)
    Optional<UserSubscription> findByUserIdAndIsActiveTrue(Long userId);

    // 🔍 Check if user has active subscription (FIXED)
    boolean existsByUserIdAndIsActiveTrue(Long userId);

    // 🔍 Get subscriptions by plan
    List<UserSubscription> findByPlanId(Long planId);

    // 🔍 Get inactive subscriptions (FIXED)
    List<UserSubscription> findByIsActiveFalse();
}