package com.example.controller.admin;
import com.example.dto.response.AdminNotificationResponse;
import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.ApiResponse;
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
                .message("Notification job enqueued successfully. Dispatching in background.")
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
                .message("Broadcast notification job enqueued successfully. Processing in background.")
                .build();
    }

    // =====================================================
    // GET NOTIFICATION HISTORY
    // =====================================================

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<AdminNotificationResponse>> getNotificationHistory(

            @RequestParam(defaultValue = "") String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Page<AdminNotificationResponse> notifications =
                adminNotificationService.getNotificationHistory(
                        PageRequest.of(page, size),
                        keyword
                );

        return ApiResponse.<Page<AdminNotificationResponse>>builder()
                .success(true)
                .message("Notification history fetched successfully.")
                .data(notifications)
                .build();
    }
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Long> getUnreadCount() {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Unread notification count fetched successfully.")
                .data(adminNotificationService.getUnreadCount())
                .build();
    }
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> markAsRead(
            @PathVariable Long id) {

        adminNotificationService.markAsRead(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Notification marked as read successfully.")
                .build();
    }
    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> markAllAsRead() {

        adminNotificationService.markAllAsRead();

        return ApiResponse.<String>builder()
                .success(true)
                .message("All notifications marked as read successfully.")
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> deleteNotification(
            @PathVariable Long id) {

        adminNotificationService.deleteNotification(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Notification deleted successfully.")
                .build();
    }
}