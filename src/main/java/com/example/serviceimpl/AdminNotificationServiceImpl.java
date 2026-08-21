package com.example.serviceimpl;

import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.AdminNotificationResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.model.*;
import com.example.repository.AdminNotificationRepository;
import com.example.repository.AdminRepository;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminNotificationService;
import com.example.service.CurrentAdminService;
import com.example.queue.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNotificationServiceImpl implements AdminNotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService adminAuditLogService;
    private final AdminRepository adminRepository;
    private final AdminNotificationRepository adminNotificationRepository;
    private final NotificationProducer notificationProducer;
    @Autowired
    @Lazy
    private com.example.service.AdminBroadcastService adminBroadcastService;

    @Override
    public void sendNotification(AdminNotificationRequestDTO request) {

        if (request.getReceiverIds() == null || request.getReceiverIds().isEmpty()) {
            throw new IllegalArgumentException("Receiver list cannot be empty.");
        }

        Admin currentAdmin = currentAdminService.getCurrentAdmin();
        int recipientCount = request.getReceiverIds().size();

        // Enqueue jobs asynchronously into Message Queue
        notificationProducer.enqueueBulkNotifications(
                request.getReceiverIds(),
                request.getTitle(),
                request.getMessage(),
                request.getType()
        );

        adminAuditLogService.log(
                currentAdmin.getId(),
                "NOTIFICATION_MANAGEMENT",
                "NOTIFICATION_SENT",
                "NOTIFICATION",
                null,
                "Admin sent notification job to " + recipientCount + " selected users via Queue",
                null,
                "Title=" + request.getTitle()
                        + ", Type=" + request.getType()
                        + ", Recipients=" + recipientCount,
                "SYSTEM",
                "SYSTEM"
        );
    }

    @Override
    public BroadcastJobResponseDTO broadcastNotification(AdminNotificationRequestDTO request) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        // 1. Initiate scalable chunked broadcast
        Long jobId = adminBroadcastService.initiateBroadcast(
                request.getTitle(),
                request.getMessage(),
                request.getType(),
                currentAdmin != null ? currentAdmin.getId() : null
        );

        adminAuditLogService.log(
                currentAdmin.getId(),
                "NOTIFICATION_MANAGEMENT",
                "NOTIFICATION_BROADCAST",
                "NOTIFICATION",
                null,
                "Admin broadcast notification job queued for all active users. JobID=" + jobId,
                null,
                "Title=" + request.getTitle()
                        + ", Type=" + request.getType(),
                "SYSTEM",
                "SYSTEM"
        );

        return adminBroadcastService.getActiveBroadcastJob();
    }

    @Override
    public Page<AdminNotificationResponse> getNotificationHistory(
            Pageable pageable,
            String keyword
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<AdminNotification> notificationPage =
                adminNotificationRepository.findByAdminAndDeletedFalse(
                        currentAdmin,
                        sortedPageable
                );

        List<AdminNotificationResponse> responses =
                notificationPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageImpl<>(
                responses,
                pageable,
                notificationPage.getTotalElements()
        );
    }

    @Override
    public long getUnreadCount() {
        Admin admin = currentAdminService.getCurrentAdmin();
        return adminNotificationRepository.countByAdminAndReadFalseAndDeletedFalse(admin);
    }

    @Override
    public Page<AdminNotificationResponse> getBroadcastLifecycleNotifications(Pageable pageable) {
        Admin currentAdmin = currentAdminService.getCurrentAdmin();
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<AdminNotification> page = adminNotificationRepository.findByAdminAndTypeInAndDeletedFalse(
                currentAdmin,
                List.of(NotificationType.ANNOUNCEMENT),
                sortedPageable
        );

        List<AdminNotificationResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public long getBroadcastLifecycleUnreadCount() {
        Admin admin = currentAdminService.getCurrentAdmin();
        return adminNotificationRepository.countByAdminAndTypeInAndReadFalseAndDeletedFalse(
                admin,
                List.of(NotificationType.ANNOUNCEMENT)
        );
    }

    @Override
    public void publishBroadcastLifecycleNotification(Long broadcastJobId, BroadcastJobStatus status, String title, String summaryMessage) {
        List<Admin> activeAdmins = adminRepository.findAllActiveAdmins();
        for (Admin admin : activeAdmins) {
            String fullTitle = title != null ? title : "Admin Broadcast Notification";
            String fullMessage = summaryMessage;

            // If reaching a terminal state (COMPLETED, COMPLETED_WITH_FAILURES, FAILED),
            // mark previous IN_PROGRESS lifecycle notifications for this admin as read to prevent duplicate unread counts.
            if (status == BroadcastJobStatus.COMPLETED ||
                status == BroadcastJobStatus.COMPLETED_WITH_FAILURES ||
                status == BroadcastJobStatus.FAILED) {
                
                List<AdminNotification> existingInApp = adminNotificationRepository.findByAdminAndDeletedFalse(admin, Pageable.unpaged())
                        .getContent().stream()
                        .filter(n -> n.getType() == NotificationType.ANNOUNCEMENT && !n.getRead())
                        .filter(n -> "Broadcast In Progress".equalsIgnoreCase(n.getTitle()) || (n.getMessage() != null && n.getMessage().contains("Job #" + broadcastJobId)))
                        .toList();

                if (!existingInApp.isEmpty()) {
                    existingInApp.forEach(n -> n.setRead(true));
                    adminNotificationRepository.saveAll(existingInApp);
                }
            }

            if (!adminNotificationRepository.existsByAdminAndTitleAndMessage(admin, fullTitle, fullMessage)) {
                AdminNotification notification = AdminNotification.builder()
                        .admin(admin)
                        .title(fullTitle)
                        .message(fullMessage)
                        .type(NotificationType.ANNOUNCEMENT)
                        .read(false)
                        .deleted(false)
                        .build();

                AdminNotification saved = adminNotificationRepository.save(notification);

                AdminNotificationResponse response = AdminNotificationResponse.builder()
                        .id(saved.getId())
                        .adminId(admin.getId())
                        .title(saved.getTitle())
                        .message(saved.getMessage())
                        .type(saved.getType())
                        .read(saved.getRead())
                        .deleted(saved.getDeleted())
                        .createdAt(saved.getCreatedAt())
                        .build();

                messagingTemplate.convertAndSend(
                        "/topic/admin-notifications/" + admin.getId(),
                        response
                );

                log.info("[BROADCAST LIFECYCLE NOTIFICATION] Created for AdminId={} | JobID={} | Status={} | Title={}",
                        admin.getId(), broadcastJobId, status, fullTitle);
            }
        }
    }

    @Override
    public void publishSuccessStoryPublishedNotification(Long storyId, String partnerOneName, String partnerTwoName) {
        List<Admin> activeAdmins = adminRepository.findAllActiveAdmins();
        String title = "Success Story Published ❤️";
        String message = partnerOneName + " & " + partnerTwoName + " success story was published successfully.";
        String eventType = "SUCCESS_STORY_PUBLISHED";

        for (Admin admin : activeAdmins) {
            if (!adminNotificationRepository.existsByAdminAndReferenceIdAndEventTypeAndDeletedFalse(admin, storyId, eventType) &&
                !adminNotificationRepository.existsByAdminAndTitleAndMessage(admin, title, message)) {
                AdminNotification notification = AdminNotification.builder()
                        .admin(admin)
                        .title(title)
                        .message(message)
                        .type(NotificationType.ANNOUNCEMENT)
                        .referenceId(storyId)
                        .eventType(eventType)
                        .read(false)
                        .deleted(false)
                        .build();

                AdminNotification saved = adminNotificationRepository.save(notification);

                AdminNotificationResponse response = mapToResponse(saved);

                messagingTemplate.convertAndSend(
                        "/topic/admin-notifications/" + admin.getId(),
                        response
                );

                log.info("[STORY ADMIN NOTIFICATION PERSISTED] AdminId={} | StoryID={} | Title={}",
                        admin.getId(), storyId, title);
            }
        }
    }

    private AdminNotificationResponse mapToResponse(AdminNotification notification) {
        if (notification == null) return null;
        Long sId = notification.getReferenceId();
        return AdminNotificationResponse.builder()
                .id(notification.getId())
                .adminId(notification.getAdmin() != null ? notification.getAdmin().getId() : null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.getRead())
                .deleted(notification.getDeleted())
                .referenceId(sId)
                .eventType(notification.getEventType())
                .storyId(sId)
                .createdAt(notification.getCreatedAt())
                .build();
    }

    @Override
    public void markAsRead(Long notificationId) {
        AdminNotification notification = adminNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getDeleted() && !notification.getRead()) {
            notification.setRead(true);
            adminNotificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead() {
        Admin admin = currentAdminService.getCurrentAdmin();
        List<AdminNotification> notifications = adminNotificationRepository.findByAdminAndDeletedFalse(admin, Pageable.unpaged()).getContent();
        notifications.forEach(notification -> notification.setRead(true));
        adminNotificationRepository.saveAll(notifications);
    }

    @Override
    public void markAllBroadcastLifecycleAsRead() {
        Admin admin = currentAdminService.getCurrentAdmin();
        Page<AdminNotification> page = adminNotificationRepository.findByAdminAndTypeInAndDeletedFalse(
                admin,
                List.of(NotificationType.ANNOUNCEMENT),
                Pageable.unpaged()
        );
        List<AdminNotification> notifications = page.getContent();
        notifications.forEach(notification -> notification.setRead(true));
        adminNotificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        AdminNotification notification = adminNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notification.setDeleted(true);
        adminNotificationRepository.save(notification);
    }
}