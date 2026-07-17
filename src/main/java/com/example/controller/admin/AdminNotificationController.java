package com.example.controller.admin;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.NotificationResponse;
import com.example.service.AdminNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    // =====================================================
    // SEND NOTIFICATION TO SELECTED USERS
    // =====================================================

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> sendNotification(
            @Valid @RequestBody AdminNotificationRequestDTO request) {

        adminNotificationService.sendNotification(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Notification sent successfully.")
                .build();
    }

    // =====================================================
    // BROADCAST NOTIFICATION TO ALL ACTIVE USERS
    // =====================================================

    @PostMapping("/broadcast")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> broadcastNotification(
            @Valid @RequestBody AdminNotificationRequestDTO request) {

        adminNotificationService.broadcastNotification(request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Broadcast notification sent successfully.")
                .build();
    }

    // =====================================================
    // GET NOTIFICATION HISTORY
    // =====================================================

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<NotificationResponse>> getNotificationHistory(

            @RequestParam(defaultValue = "") String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Page<NotificationResponse> notifications =
                adminNotificationService.getNotificationHistory(
                        PageRequest.of(page, size),
                        keyword
                );

        return ApiResponse.<Page<NotificationResponse>>builder()
                .success(true)
                .message("Notification history fetched successfully.")
                .data(notifications)
                .build();
    }
}