package com.example.service;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.NotificationResponse;
import com.example.model.Notification;
import com.example.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    // ==========================
    // USER NOTIFICATIONS
    // ==========================

    void create(Long senderId, Long receiverId, NotificationType type);

    List<Notification> getAll(Long userId);

    long unreadCount(Long userId);

    void markRead(Long id);

    void markAllRead(Long userId);

    void delete(Long id);

    // ==========================
// MATCH NOTIFICATIONS
// ==========================

    void createMatchRecommendation(
            Long receiverId,
            Long matchedUserId,
            Integer matchPercentage
    );

    // ==========================
    // ADMIN NOTIFICATIONS
    // ==========================

    /**
     * Send notification to one or multiple selected users.
     */
    void sendNotification(AdminNotificationRequestDTO request);

    /**
     * Broadcast notification to all active users.
     */
    void broadcastNotification(AdminNotificationRequestDTO request);

    /**
     * Get notification history with pagination.
     */
    Page<NotificationResponse> getNotificationHistory(Pageable pageable);
}