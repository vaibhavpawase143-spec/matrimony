package com.example.service;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.request.SubscriptionPlanRequestDTO;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionPlanService {

    // =====================================================
    // CRUD
    // =====================================================

    SubscriptionPlanResponseDTO create(SubscriptionPlanRequestDTO requestDto);

    SubscriptionPlanResponseDTO update(Long id,
                                       SubscriptionPlanRequestDTO requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =====================================================
    // GET
    // =====================================================

    SubscriptionPlanResponseDTO getById(Long id);

    List<SubscriptionPlanResponseDTO> getAll();

    List<SubscriptionPlanResponseDTO> getDeleted();

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<SubscriptionPlanResponseDTO> getActive();

    List<SubscriptionPlanResponseDTO> getInactive();

    // =====================================================
    // ADMIN
    // =====================================================

    List<SubscriptionPlanResponseDTO> getByAdmin(Long adminId);

    List<SubscriptionPlanResponseDTO> getActiveByAdmin(Long adminId);

    List<SubscriptionPlanResponseDTO> getInactiveByAdmin(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<SubscriptionPlanResponseDTO> search(String keyword);

    List<SubscriptionPlanResponseDTO> searchByAdmin(Long adminId,
                                                    String keyword);

    // =====================================================
    // STATISTICS
    // =====================================================

    SubscriptionPlanStatsDTO getStatistics();

    // =====================================================
    // PAGINATION
    // =====================================================

    Page<SubscriptionPlanResponseDTO> getAllPlans(
            SubscriptionPlanFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );
}