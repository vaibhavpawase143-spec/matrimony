package com.example.controller.admin;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SubscriptionPlanResponseDTO;
import com.example.dto.response.SubscriptionPlanStatsDTO;
import com.example.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/subscription-plans")
@RequiredArgsConstructor
public class AdminSubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    // ================= STATISTICS =================

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<SubscriptionPlanStatsDTO> getStatistics() {

        return new ApiResponse<>(
                true,
                "Subscription plan statistics retrieved successfully",
                subscriptionPlanService.getStatistics()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<SubscriptionPlanResponseDTO>> getAllPlans(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction,

            @RequestParam(required = false) String search,

            @RequestParam(required = false) Boolean active
    ) {

        SubscriptionPlanFilterDTO filter = new SubscriptionPlanFilterDTO();

        filter.setKeyword(search);
        filter.setIsActive(active);

        return new ApiResponse<>(
                true,
                "Subscription plans retrieved successfully",
                subscriptionPlanService.getAllPlans(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}