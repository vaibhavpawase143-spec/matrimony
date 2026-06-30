package com.example.controller.admin;

import com.example.dto.request.UserSubscriptionFilterDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.UserSubscriptionResponseDTO;
import com.example.dto.response.UserSubscriptionStatsDTO;
import com.example.service.AdminUserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminUserSubscriptionController {

    private final AdminUserSubscriptionService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<UserSubscriptionResponseDTO>> getAllSubscriptions(

            @RequestParam(required = false) String search,

            @RequestParam(required = false) Boolean isActive,

            @RequestParam(required = false) String status,

            @RequestParam(required = false) Long planId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction
    ) {

        UserSubscriptionFilterDTO filter =
                UserSubscriptionFilterDTO.builder()
                        .search(search)
                        .isActive(isActive)
                        .status(status)
                        .planId(planId)
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .build();

        Page<UserSubscriptionResponseDTO> result =
                service.getAllSubscriptions(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ApiResponse.<Page<UserSubscriptionResponseDTO>>builder()
                .success(true)
                .message("User subscriptions retrieved successfully")
                .data(result)
                .build();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionResponseDTO> getSubscriptionById(
            @PathVariable Long id) {

        return ApiResponse.<UserSubscriptionResponseDTO>builder()
                .success(true)
                .message("Subscription retrieved successfully")
                .data(service.getSubscriptionById(id))
                .build();
    }
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionResponseDTO> cancelSubscription(
            @PathVariable Long id,
            @RequestParam String reason) {

        return ApiResponse.<UserSubscriptionResponseDTO>builder()
                .success(true)
                .message("Subscription cancelled successfully")
                .data(service.cancelSubscription(id, reason))
                .build();
    }
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionResponseDTO> activateSubscription(
            @PathVariable Long id) {

        return ApiResponse.<UserSubscriptionResponseDTO>builder()
                .success(true)
                .message("Subscription activated successfully")
                .data(service.activateSubscription(id))
                .build();
    }
    @PutMapping("/{id}/expire")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionResponseDTO> expireSubscription(
            @PathVariable Long id) {

        return ApiResponse.<UserSubscriptionResponseDTO>builder()
                .success(true)
                .message("Subscription expired successfully")
                .data(service.expireSubscription(id))
                .build();
    }
    @PutMapping("/{id}/refund")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionResponseDTO> refundSubscription(
            @PathVariable Long id,
            @RequestParam BigDecimal refundAmount,
            @RequestParam String refundReason) {

        return ApiResponse.<UserSubscriptionResponseDTO>builder()
                .success(true)
                .message("Subscription refunded successfully")
                .data(service.refundSubscription(
                        id,
                        refundAmount,
                        refundReason))
                .build();
    }
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<UserSubscriptionStatsDTO> getStatistics() {

        return ApiResponse.<UserSubscriptionStatsDTO>builder()
                .success(true)
                .message("Subscription statistics retrieved successfully")
                .data(service.getStatistics())
                .build();
    }


}