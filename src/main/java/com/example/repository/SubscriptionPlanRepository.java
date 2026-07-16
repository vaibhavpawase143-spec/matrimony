package com.example.repository;

import com.example.model.SubscriptionPlan;
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

    Optional<SubscriptionPlan> findByIdAndDeletedAtIsNull(Long id);

    Optional<SubscriptionPlan> findByIdAndDeletedAtIsNotNull(Long id);

    List<SubscriptionPlan> findAllByDeletedAtIsNull();

    List<SubscriptionPlan> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<SubscriptionPlan> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SubscriptionPlan> findByIsActiveTrueAndDeletedAtIsNull();

    List<SubscriptionPlan> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SubscriptionPlan> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<SubscriptionPlan> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<SubscriptionPlan> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SubscriptionPlan> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

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