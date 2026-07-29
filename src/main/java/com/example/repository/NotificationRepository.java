package com.example.repository;

import com.example.model.Notification;
import com.example.model.NotificationType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    boolean existsByReceiverIdAndMatchedUserIdAndTypeAndDeletedFalse(
            Long receiverId,
            Long matchedUserId,
            NotificationType type
    );

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
    @Query("""
    SELECT n
    FROM Notification n
    WHERE n.deleted = false
      AND n.type IN :types
    ORDER BY n.createdAt DESC
""")
    Page<Notification> findAdminNotifications(
            @Param("types") List<NotificationType> types,
            Pageable pageable
    );

    long countByTypeInAndDeletedFalseAndReadFalse(
            List<NotificationType> types
    );

    List<Notification> findByTypeInAndDeletedFalseAndReadFalse(
            List<NotificationType> types
    );
    long countByDeletedFalseAndReadFalse();
    List<Notification> findByDeletedFalseAndReadFalse();
    boolean existsBySubscriptionIdAndTitleAndDeletedFalse(
            Long subscriptionId,
            String title
    );

}