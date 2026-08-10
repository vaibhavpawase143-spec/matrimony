package com.example.repository;

import com.example.model.User;
import com.example.model.UserSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends
        JpaRepository<UserSubscription, Long>,
        JpaSpecificationExecutor<UserSubscription> {

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

    @Query("""
        SELECT us
        FROM UserSubscription us
        JOIN FETCH us.user
        JOIN FETCH us.subscriptionPlan
        WHERE us.user.id IN :userIds
        AND us.isActive = true
    """)
    List<UserSubscription> findByUserIdInAndIsActiveTrue(
            @Param("userIds") List<Long> userIds
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
    @EntityGraph(attributePaths = {
            "user",
            "user.profile",
            "user.profile.gender",
            "subscriptionPlan"
    })
    Page<UserSubscription> findAll(
            Specification<UserSubscription> specification,
            Pageable pageable
    );
    @EntityGraph(attributePaths = {
            "user",
            "user.profile",
            "user.profile.gender",
            "subscriptionPlan"
    })
    Optional<UserSubscription> findWithDetailsById(Long id);
    long countByIsActiveTrue();

    long countByIsActiveFalse();

    long countByStatus(String status);
    @Query(value = """
SELECT COUNT(*)
FROM user_subscriptions
WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE)
""", nativeQuery = true)
    Long countCurrentMonthSubscriptions();

    @Query(value = """
SELECT COUNT(*)
FROM user_subscriptions
WHERE created_at >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
AND created_at < DATE_TRUNC('month', CURRENT_DATE)
""", nativeQuery = true)
    Long countPreviousMonthSubscriptions();
    // ==========================================
// EXPIRED ACTIVE SUBSCRIPTIONS
// ==========================================

    List<UserSubscription> findByIsActiveTrueAndEndDateBefore(
            java.time.LocalDateTime dateTime
    );

    @Query("""
        SELECT us
        FROM UserSubscription us
        JOIN FETCH us.user
        JOIN FETCH us.subscriptionPlan
        WHERE us.isActive = true
        AND us.status = 'ACTIVE'
        AND us.endDate <= :now
    """)
    List<UserSubscription> findActiveSubscriptionsDueForExpiry(@Param("now") java.time.LocalDateTime now);

    @org.springframework.data.jpa.repository.Modifying
    @Query("""
        UPDATE UserSubscription us
        SET us.isActive = false, us.status = 'EXPIRED'
        WHERE us.id = :id AND us.isActive = true AND us.status = 'ACTIVE'
    """)
    int expireSubscriptionAtomically(@Param("id") Long id);

    @Query("""
SELECT us
FROM UserSubscription us
JOIN FETCH us.user
WHERE us.isActive = true
AND us.status = 'ACTIVE'
""")
    List<UserSubscription> findAllActiveSubscriptions();
}