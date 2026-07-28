package com.example.service;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.AdminNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNotificationService {

    void sendNotification(AdminNotificationRequestDTO request);

    void broadcastNotification(AdminNotificationRequestDTO request);

    Page<AdminNotificationResponse> getNotificationHistory(
            Pageable pageable,
            String keyword
    );

    long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();

    void deleteNotification(Long notificationId);
}