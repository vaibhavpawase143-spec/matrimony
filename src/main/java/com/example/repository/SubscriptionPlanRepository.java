package com.example.repository;

import com.example.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long>,
        JpaSpecificationExecutor<SubscriptionPlan> {

    // =====================================================
    // BASIC
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    Optional<SubscriptionPlan> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = "admin")
    Optional<SubscriptionPlan> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findAllByDeletedAtIsNull();

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    @EntityGraph(attributePaths = "admin")
    Optional<SubscriptionPlan> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByIsActiveTrueAndDeletedAtIsNull();

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    @EntityGraph(attributePaths = "admin")
    List<SubscriptionPlan> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );

    // =====================================================
    // STATISTICS
    // =====================================================

    long countByDeletedAtIsNull();

    long countByIsActiveTrueAndDeletedAtIsNull();

    long countByIsActiveFalseAndDeletedAtIsNull();
}