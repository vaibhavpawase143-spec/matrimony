package com.example.repository;

import com.example.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    // 🔍 Get all active records
    List<SubscriptionPlan> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<SubscriptionPlan> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<SubscriptionPlan> findByAdminId(Long adminId);

    // 🔍 Active records by admin
    List<SubscriptionPlan> findByAdminIdAndIsActiveTrue(Long adminId);

    // 🔍 Search (dropdown/filter)
    List<SubscriptionPlan> findByNameContainingIgnoreCase(String keyword);
}