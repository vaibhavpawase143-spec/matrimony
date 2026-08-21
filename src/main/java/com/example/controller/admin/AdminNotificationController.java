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

import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.service.AdminBroadcastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;
    private final AdminBroadcastService adminBroadcastService;

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
    public ResponseEntity<ApiResponse<BroadcastJobResponseDTO>> broadcastNotification(
            @Valid @RequestBody AdminNotificationRequestDTO request) {

        BroadcastJobResponseDTO responseDTO = adminNotificationService.broadcastNotification(request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                ApiResponse.<BroadcastJobResponseDTO>builder()
                        .success(true)
                        .message("Broadcast notification accepted for processing")
                        .data(responseDTO)
                        .build()
        );
    }

    // =====================================================
    // RESUME INTERRUPTED BROADCAST JOB
    // =====================================================

    @PostMapping("/broadcast/{jobId}/resume")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BroadcastJobResponseDTO>> resumeBroadcastJob(@PathVariable Long jobId) {
        BroadcastJobResponseDTO response = adminBroadcastService.resumeBroadcastJob(jobId);
        return ResponseEntity.ok(
                ApiResponse.<BroadcastJobResponseDTO>builder()
                        .success(true)
                        .message("Broadcast job resumed successfully")
                        .data(response)
                        .build()
        );
    }

    // =====================================================
    // CANCEL INTERRUPTED OR ACTIVE BROADCAST JOB
    // =====================================================

    @PostMapping("/broadcast/{jobId}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<BroadcastJobResponseDTO>> cancelBroadcastJob(@PathVariable Long jobId) {
        BroadcastJobResponseDTO response = adminBroadcastService.cancelBroadcastJob(jobId);
        return ResponseEntity.ok(
                ApiResponse.<BroadcastJobResponseDTO>builder()
                        .success(true)
                        .message("Broadcast job cancelled successfully")
                        .data(response)
                        .build()
        );
    }

    // =====================================================
    // GET ACTIVE BROADCAST JOB
    // =====================================================

    @GetMapping("/broadcast/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BroadcastJobResponseDTO> getActiveBroadcastJob() {
        BroadcastJobResponseDTO activeJob = adminBroadcastService.getActiveBroadcastJob();
        return ApiResponse.<BroadcastJobResponseDTO>builder()
                .success(true)
                .message("Active broadcast job status fetched successfully.")
                .data(activeJob)
                .build();
    }

    // =====================================================
    // GET BROADCAST JOB BY ID
    // =====================================================

    @GetMapping("/broadcast/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<BroadcastJobResponseDTO> getBroadcastJobById(@PathVariable Long jobId) {
        BroadcastJobResponseDTO job = adminBroadcastService.getBroadcastJobById(jobId);
        return ApiResponse.<BroadcastJobResponseDTO>builder()
                .success(true)
                .message("Broadcast job status fetched successfully.")
                .data(job)
                .build();
    }

    // =====================================================
    // GET BROADCAST JOB HISTORY
    // =====================================================

    @GetMapping("/broadcast/history")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<BroadcastJobResponseDTO>> getBroadcastHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BroadcastJobResponseDTO> history = adminBroadcastService.getBroadcastHistory(PageRequest.of(page, size));
        return ApiResponse.<Page<BroadcastJobResponseDTO>>builder()
                .success(true)
                .message("Broadcast history fetched successfully.")
                .data(history)
                .build();
    }

    // =====================================================
    // GET BROADCAST RECIPIENT DELIVERY DETAILS
    // =====================================================

    @GetMapping("/broadcast/{broadcastJobId}/recipients")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<com.example.dto.response.BroadcastRecipientStatusResponseDTO>> getBroadcastRecipients(
            @PathVariable Long broadcastJobId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) com.example.model.AppNotificationStatus appStatus,
            @RequestParam(required = false) com.example.model.RecipientEmailStatus emailStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        int limitedSize = Math.min(Math.max(1, size), 200);
        Page<com.example.dto.response.BroadcastRecipientStatusResponseDTO> recipients = adminBroadcastService.getBroadcastRecipients(
                broadcastJobId, search, appStatus, emailStatus, PageRequest.of(page, limitedSize)
        );

        return ApiResponse.<Page<com.example.dto.response.BroadcastRecipientStatusResponseDTO>>builder()
                .success(true)
                .message("Broadcast recipient delivery status fetched successfully.")
                .data(recipients)
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

    @GetMapping("/broadcast-lifecycle")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<AdminNotificationResponse>> getBroadcastLifecycleNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AdminNotificationResponse> notifications = adminNotificationService.getBroadcastLifecycleNotifications(PageRequest.of(page, size));

        return ApiResponse.<Page<AdminNotificationResponse>>builder()
                .success(true)
                .message("Broadcast lifecycle notifications fetched successfully.")
                .data(notifications)
                .build();
    }

    @GetMapping("/broadcast-lifecycle/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Long> getBroadcastLifecycleUnreadCount() {

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Broadcast lifecycle unread notification count fetched successfully.")
                .data(adminNotificationService.getBroadcastLifecycleUnreadCount())
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

    @PutMapping("/broadcast-lifecycle/read-all")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> markAllBroadcastLifecycleAsRead() {

        adminNotificationService.markAllBroadcastLifecycleAsRead();

        return ApiResponse.<String>builder()
                .success(true)
                .message("All broadcast lifecycle notifications marked as read successfully.")
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