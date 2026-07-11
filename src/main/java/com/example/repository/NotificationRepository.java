package com.example.repository;

import com.example.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ================= USER =================

    // Get all notifications for a user (latest first)
    List<Notification> findByReceiverIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);

    // Get unread notifications
    List<Notification> findByReceiverIdAndReadFalseAndDeletedFalse(Long userId);

    // Count unread notifications
    long countByReceiverIdAndReadFalseAndDeletedFalse(Long userId);

    // ================= ADMIN =================

    // Notification history
    Page<Notification> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    // Search by title
    Page<Notification> findByTitleContainingIgnoreCaseAndDeletedFalse(
            String keyword,
            Pageable pageable
    );

    // Search by message
    Page<Notification> findByMessageContainingIgnoreCaseAndDeletedFalse(
            String keyword,
            Pageable pageable
    );

    // Search by notification type
    Page<Notification> findByTypeAndDeletedFalse(
            com.example.model.NotificationType type,
            Pageable pageable
    );
}