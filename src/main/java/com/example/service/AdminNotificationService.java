package com.example.service;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.AdminNotificationResponse;
import com.example.dto.response.BroadcastJobResponseDTO;
import com.example.model.BroadcastJobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNotificationService {

    void sendNotification(AdminNotificationRequestDTO request);

    BroadcastJobResponseDTO broadcastNotification(AdminNotificationRequestDTO request);

    Page<AdminNotificationResponse> getNotificationHistory(
            Pageable pageable,
            String keyword
    );

    long getUnreadCount();

    Page<AdminNotificationResponse> getBroadcastLifecycleNotifications(Pageable pageable);

    long getBroadcastLifecycleUnreadCount();

    void publishBroadcastLifecycleNotification(Long broadcastJobId, BroadcastJobStatus status, String title, String summaryMessage);

    void markAsRead(Long notificationId);

    void markAllAsRead();

    void markAllBroadcastLifecycleAsRead();

    void publishSuccessStoryPublishedNotification(Long storyId, String partnerOneName, String partnerTwoName);

    void deleteNotification(Long notificationId);
}