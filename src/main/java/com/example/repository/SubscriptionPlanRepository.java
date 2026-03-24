package com.example.repository;

import com.example.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    // 🔍 Find by name (admin-specific)
    Optional<SubscriptionPlan> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Check duplicate (admin-specific)
    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    // 🔍 Get all records by admin
    List<SubscriptionPlan> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<SubscriptionPlan> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Inactive records by admin
    List<SubscriptionPlan> findByAdminIdAndIsActiveFalse(Long adminId);

    // 🔍 Search (admin + keyword)
    List<SubscriptionPlan> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);
}