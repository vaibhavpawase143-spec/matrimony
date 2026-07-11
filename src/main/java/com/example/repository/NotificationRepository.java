package com.example.repository;

import com.example.model.Notification;
import com.example.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ Get all notifications for a user (not deleted, latest first)
    List<Notification> findByReceiverIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);
    List<Notification>
    findByReceiverIdAndReadFalseAndDeletedFalse(
            Long userId
    );
    // ✅ Count unread notifications
    long countByReceiverIdAndReadFalseAndDeletedFalse(Long userId);
    boolean existsByReceiverIdAndMatchedUserIdAndTypeAndDeletedFalse(
            Long receiverId,
            Long matchedUserId,
            NotificationType type
    );
}