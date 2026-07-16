package com.example.controller.master;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.request.SubscriptionPlanRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import com.example.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/subscription-plans")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ApiResponse<SubscriptionPlanResponseDTO> create(
            @Valid @RequestBody SubscriptionPlanRequestDTO requestDto) {

        return ApiResponse.success(
                "Subscription plan created successfully.",
                subscriptionPlanService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanRequestDTO requestDto) {

        return ApiResponse.success(
                "Subscription plan updated successfully.",
                subscriptionPlanService.update(id, requestDto)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<SubscriptionPlanResponseDTO> getById(
            @PathVariable Long id) {

        return ApiResponse.success(
                "Subscription plan fetched successfully.",
                subscriptionPlanService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getAll() {

        return ApiResponse.success(
                "Subscription plans fetched successfully.",
                subscriptionPlanService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted subscription plans fetched successfully.",
                subscriptionPlanService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active subscription plans fetched successfully.",
                subscriptionPlanService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive subscription plans fetched successfully.",
                subscriptionPlanService.getInactive()
        );
    }

    // =====================================================
    // ADMIN
    // =====================================================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Subscription plans fetched successfully.",
                subscriptionPlanService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active subscription plans fetched successfully.",
                subscriptionPlanService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive subscription plans fetched successfully.",
                subscriptionPlanService.getInactiveByAdmin(adminId)
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                subscriptionPlanService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<SubscriptionPlanResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                subscriptionPlanService.searchByAdmin(adminId, keyword)
        );
    }

    // =====================================================
    // STATISTICS
    // =====================================================

    @GetMapping("/statistics")
    public ApiResponse<SubscriptionPlanStatsDTO> getStatistics() {

        return ApiResponse.success(
                "Subscription plan statistics fetched successfully.",
                subscriptionPlanService.getStatistics()
        );
    }

    // =====================================================
    // PAGINATION
    // =====================================================

    @PostMapping("/filter")
    public ApiResponse<Page<SubscriptionPlanResponseDTO>> getAllPlans(
            @RequestBody(required = false) SubscriptionPlanFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        return ApiResponse.success(
                "Subscription plans fetched successfully.",
                subscriptionPlanService.getAllPlans(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(
            @PathVariable Long id) {

        subscriptionPlanService.softDelete(id);

        return ApiResponse.success(
                "Subscription plan deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PutMapping("/{id}/restore")
    public ApiResponse<Void> restore(
            @PathVariable Long id) {

        subscriptionPlanService.restore(id);

        return ApiResponse.success(
                "Subscription plan restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(
            @PathVariable Long id) {

        subscriptionPlanService.hardDelete(id);

        return ApiResponse.success(
                "Subscription plan permanently deleted successfully.",
                null
        );
    }
}