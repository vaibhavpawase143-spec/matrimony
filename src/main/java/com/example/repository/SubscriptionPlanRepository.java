package com.example.repository;

import com.example.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    // 🔍 Find by name
    Optional<SubscriptionPlan> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Get active plans (VERY IMPORTANT)
    List<SubscriptionPlan> findByActiveTrue();

    // 🔍 Get inactive plans
    List<SubscriptionPlan> findByActiveFalse();

    // 🔍 Filter by admin
    List<SubscriptionPlan> findByAdminId(Long adminId);

    // 🔍 Active plans by admin
    List<SubscriptionPlan> findByAdminIdAndActiveTrue(Long adminId);

    // 🔍 Search plans
    List<SubscriptionPlan> findByNameContainingIgnoreCase(String keyword);
}