package com.example.service;

import com.example.model.Notification;
import com.example.model.NotificationType;

import java.util.List;

public interface NotificationService {

    void create(Long senderId, Long receiverId, NotificationType type);

    List<Notification> getAll(Long userId);   // ✅ FIXED

    long unreadCount(Long userId);

    void markRead(Long id);

    void delete(Long id);
}