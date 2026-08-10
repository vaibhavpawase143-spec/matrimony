package com.example.serviceimpl;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.AdminNotificationResponse;
import com.example.dto.response.NotificationResponse;
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

    private static final List<NotificationType> ADMIN_NOTIFICATION_TYPES = List.of(
            NotificationType.ANNOUNCEMENT,
            NotificationType.SYSTEM,
            NotificationType.MAINTENANCE,
            NotificationType.SUBSCRIPTION,
            NotificationType.WARNING,
            NotificationType.REPORT,
            NotificationType.SUPPORT,
            NotificationType.NEW_USER,
            NotificationType.ADMIN
    );

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
    public void broadcastNotification(AdminNotificationRequestDTO request) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        // 1. Enqueue user broadcast notification jobs asynchronously into Message Queue
        notificationProducer.enqueueBulkNotifications(
                null, // null indicates all active users
                request.getTitle(),
                request.getMessage(),
                request.getType()
        );

        // 2. Broadcast to Admins
        List<Admin> admins = adminRepository.findAllActiveAdmins();

        for (Admin admin : admins) {

            AdminNotification notification = AdminNotification.builder()
                    .admin(admin)
                    .title(request.getTitle())
                    .message(request.getMessage())
                    .type(request.getType())
                    .read(false)
                    .deleted(false)
                    .build();

            AdminNotification saved =
                    adminNotificationRepository.save(notification);

            AdminNotificationResponse response =
                    AdminNotificationResponse.builder()
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
        }

        adminAuditLogService.log(
                currentAdmin.getId(),
                "NOTIFICATION_MANAGEMENT",
                "NOTIFICATION_BROADCAST",
                "NOTIFICATION",
                null,
                "Admin broadcast notification job queued for all active users",
                null,
                "Title=" + request.getTitle()
                        + ", Type=" + request.getType(),
                "SYSTEM",
                "SYSTEM"
        );
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
                        .map(notification ->
                                AdminNotificationResponse.builder()
                                        .id(notification.getId())
                                        .adminId(notification.getAdmin().getId())
                                        .title(notification.getTitle())
                                        .message(notification.getMessage())
                                        .type(notification.getType())
                                        .read(notification.getRead())
                                        .deleted(notification.getDeleted())
                                        .createdAt(notification.getCreatedAt())
                                        .build()
                        )
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

        return adminNotificationRepository
                .countByAdminAndReadFalseAndDeletedFalse(admin);
    }

    @Override
    public void markAsRead(Long notificationId) {

        AdminNotification notification =
                adminNotificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: " + notificationId
                                ));

        if (!notification.getDeleted() && !notification.getRead()) {
            notification.setRead(true);
            adminNotificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead() {

        Admin admin = currentAdminService.getCurrentAdmin();

        List<AdminNotification> notifications =
                adminNotificationRepository
                        .findByAdminAndDeletedFalse(
                                admin,
                                Pageable.unpaged()
                        )
                        .getContent();

        notifications.forEach(notification -> notification.setRead(true));

        adminNotificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(Long notificationId) {

        AdminNotification notification =
                adminNotificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: " + notificationId
                                ));

        notification.setDeleted(true);

        adminNotificationRepository.save(notification);
    }
}