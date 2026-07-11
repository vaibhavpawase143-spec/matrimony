package com.example.service;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminNotificationService {
    void sendNotification(AdminNotificationRequestDTO request);

    void broadcastNotification(AdminNotificationRequestDTO request);

    Page<NotificationResponse> getNotificationHistory(
            Pageable pageable,
            String keyword
    );
}
