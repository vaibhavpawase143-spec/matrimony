package com.example.repository;

import com.example.model.User;
import com.example.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    UserSubscription findByUser(User user);

    // ==========================================
    // USER HISTORY
    // ==========================================

    @Query("""
    SELECT us
    FROM UserSubscription us
    JOIN FETCH us.user
    JOIN FETCH us.subscriptionPlan
    WHERE us.user.id = :userId
    ORDER BY us.createdAt DESC
""")
    List<UserSubscription> findByUserId(
            @Param("userId") Long userId
    );
    // ==========================================
    // ACTIVE SUBSCRIPTION (FETCH USER + PLAN)
    // ==========================================

    @Query("""
        SELECT us
        FROM UserSubscription us
        JOIN FETCH us.user
        JOIN FETCH us.subscriptionPlan
        WHERE us.user.id = :userId
        AND us.isActive = true
    """)
    Optional<UserSubscription> findByUserIdAndIsActiveTrue(
            @Param("userId") Long userId
    );

    // ==========================================
    // CHECK ACTIVE
    // ==========================================

    boolean existsByUserIdAndIsActiveTrue(Long userId);

    // ==========================================
    // PLAN
    // ==========================================

    List<UserSubscription> findBySubscriptionPlanId(Long planId);

    List<UserSubscription> findBySubscriptionPlanIdAndIsActiveTrue(Long planId);

    // ==========================================
    // INACTIVE
    // ==========================================

    List<UserSubscription> findByIsActiveFalse();

    List<UserSubscription> findByUserIdAndIsActiveFalse(Long userId);
    Optional<UserSubscription> findFirstByUser_IdAndIsActiveTrueAndStatusAndEndDateAfter(
            Long userId,
            String status,
            java.time.LocalDateTime now
    );
}