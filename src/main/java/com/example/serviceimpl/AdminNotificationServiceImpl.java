package com.example.serviceimpl;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.NotificationResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.Notification;
import com.example.model.User;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminNotificationService;
import com.example.service.CurrentAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNotificationServiceImpl implements AdminNotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService adminAuditLogService;
    @Override
    public void sendNotification(AdminNotificationRequestDTO request) {

        if (request.getReceiverIds() == null || request.getReceiverIds().isEmpty()) {
            throw new IllegalArgumentException("Receiver list cannot be empty.");
        }
        Admin currentAdmin = currentAdminService.getCurrentAdmin();
        int recipientCount = request.getReceiverIds().size();
        for (Long userId : request.getReceiverIds()) {

            User user = userRepository
                    .findByIdAndIsActiveTrue(userId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found with id : " + userId
                            )
                    );

            Notification notification = new Notification();

            // 0 = System/Admin
            notification.setSenderId(0L);

            notification.setReceiverId(user.getId());

            notification.setTitle(request.getTitle());

            notification.setMessage(request.getMessage());

            notification.setType(request.getType());

            notification.setRead(false);

            notification.setDeleted(false);

            notification.setCreatedAt(LocalDateTime.now());

            Notification saved = notificationRepository.save(notification);

            NotificationResponse response = new NotificationResponse();

            response.setId(saved.getId());

            response.setSenderId(saved.getSenderId());
            response.setSenderName("System");
            response.setReceiverId(saved.getReceiverId());

            response.setTitle(saved.getTitle());

            response.setMessage(saved.getMessage());

            response.setType(saved.getType().name());

            response.setRead(saved.isRead());

            response.setCreatedAt(saved.getCreatedAt());

            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + user.getId(),
                    response
            );
        }
        adminAuditLogService.log(
                currentAdmin.getId(),
                "NOTIFICATION_MANAGEMENT",
                "NOTIFICATION_SENT",
                "NOTIFICATION",
                null,
                "Admin sent notification to " + recipientCount + " selected users",
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

        List<User> users = userRepository.findByIsActiveTrue();
        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        int recipientCount = users.size();
        for (User user : users) {

            Notification notification = new Notification();

            notification.setSenderId(0L);

            notification.setReceiverId(user.getId());

            notification.setTitle(request.getTitle());

            notification.setMessage(request.getMessage());

            notification.setType(request.getType());

            notification.setRead(false);

            notification.setDeleted(false);

            notification.setCreatedAt(LocalDateTime.now());

            Notification saved = notificationRepository.save(notification);

            NotificationResponse response = new NotificationResponse();

            response.setId(saved.getId());
            response.setSenderId(saved.getSenderId());
            response.setReceiverId(saved.getReceiverId());
            response.setTitle(saved.getTitle());
            response.setMessage(saved.getMessage());
            response.setType(saved.getType().name());
            response.setRead(saved.isRead());
            response.setCreatedAt(saved.getCreatedAt());

            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + user.getId(),
                    response
            );
        }
        adminAuditLogService.log(
                currentAdmin.getId(),
                "NOTIFICATION_MANAGEMENT",
                "NOTIFICATION_BROADCAST",
                "NOTIFICATION",
                null,
                " Admin  Broadcast notification sent to all active users",
                null,
                "Title=" + request.getTitle()
                        + ", Type=" + request.getType()
                        + ", Recipients=" + recipientCount,
                "SYSTEM",
                "SYSTEM"
        );
    }

    @Override
    public Page<NotificationResponse> getNotificationHistory(
            Pageable pageable,
            String keyword
    ) {

        Page<Notification> notificationPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            notificationPage = notificationRepository
                    .findByDeletedFalseOrderByCreatedAtDesc(pageable);
        } else {
            notificationPage = notificationRepository
                    .findByTitleContainingIgnoreCaseAndDeletedFalse(
                            keyword,
                            pageable
                    );
        }

        List<NotificationResponse> responses = notificationPage
                .getContent()
                .stream()
                .map(notification -> {

                    NotificationResponse response = new NotificationResponse();

                    response.setId(notification.getId());

                    response.setSenderId(notification.getSenderId());

                    response.setReceiverId(notification.getReceiverId());

                    response.setTitle(notification.getTitle());

                    response.setMessage(notification.getMessage());

                    response.setType(notification.getType().name());

                    response.setRead(notification.isRead());

                    response.setCreatedAt(notification.getCreatedAt());

                    return response;

                })
                .collect(Collectors.toList());

        return new PageImpl<>(
                responses,
                pageable,
                notificationPage.getTotalElements()
        );
    }
}