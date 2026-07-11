package com.example.repository;

import com.example.model.UserSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    // ================= ADMIN QUERIES =================

    // Find by status
    Page<UserSubscription> findByStatus(String status, Pageable pageable);

    // Find by plan with pagination
    Page<UserSubscription> findBySubscriptionPlanId(Long planId, Pageable pageable);

    // Count active subscriptions
    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.status = 'ACTIVE'")
    long countActiveSubscriptions();

    // Count expiring subscriptions
    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.status = 'ACTIVE' AND u.endDate <= :expiryDate")
    long countExpiringSubscriptions(@Param("expiryDate") LocalDateTime expiryDate);

    // Count by status
    long countByStatus(String status);

    // Count cancelled this month
    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.status = 'CANCELLED' AND MONTH(u.cancelledAt) = MONTH(CURRENT_DATE) AND YEAR(u.cancelledAt) = YEAR(CURRENT_DATE)")
    long countCancelledThisMonth();

    // Find expiring subscriptions
    @Query("SELECT u FROM UserSubscription u WHERE u.status = 'ACTIVE' AND u.endDate <= :expiryDate ORDER BY u.endDate ASC")
    List<UserSubscription> findExpiringSubscriptions(@Param("expiryDate") LocalDateTime expiryDate);

    // Count by plan
    long countBySubscriptionPlanId(Long planId);

    // Additional methods for AdminAnalyticsService
    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.isActive = true")
    long countActive();

    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.status = 'EXPIRED'")
    long countExpired();

    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.createdAt > :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(u) FROM UserSubscription u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}