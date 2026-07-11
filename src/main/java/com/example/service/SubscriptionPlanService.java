package com.example.service;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import com.example.model.SubscriptionPlan;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    // ✅ Create
    SubscriptionPlan create(SubscriptionPlan plan);

    // 🔍 Get by ID
    Optional<SubscriptionPlan> getById(Long id);

    // 🔍 Get all
    List<SubscriptionPlan> getAll();

    // 🔍 By admin
    List<SubscriptionPlan> getByAdmin(Long adminId);

    // 🔍 Active / Inactive (admin-based)
    List<SubscriptionPlan> getActiveByAdmin(Long adminId);

    List<SubscriptionPlan> getInactiveByAdmin(Long adminId);

    // 🔍 Search (admin-based)
    List<SubscriptionPlan> searchByAdmin(Long adminId, String keyword);

    // ✅ Update
    SubscriptionPlan update(Long id, SubscriptionPlan plan);

    // ❌ Soft delete
    void delete(Long id);
    SubscriptionPlanStatsDTO getStatistics();
    Page<SubscriptionPlanResponseDTO> getAllPlans(
            SubscriptionPlanFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );
}