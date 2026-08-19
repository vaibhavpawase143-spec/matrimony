package com.example.repository;

import com.example.model.Notification;
import com.example.model.NotificationType;
import org.springframework.data.repository.query.Param;
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

    boolean existsBySenderIdAndReceiverIdAndTypeAndReadFalseAndDeletedFalse(
            Long senderId,
            Long receiverId,
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

    boolean existsByReceiverIdAndTitleAndDeletedFalse(Long receiverId, String title);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = """
        INSERT INTO notifications (sender_id, receiver_id, title, message, type, read, deleted, created_at)
        SELECT NULL, u.id, :title, :message, :type, false, false, :now
        FROM users u
        WHERE u.id IN (:userIds)
          AND NOT EXISTS (
              SELECT 1 FROM notifications n
              WHERE n.receiver_id = u.id AND n.title = :title
          )
    """, nativeQuery = true)
    int bulkInsertAppNotifications(
            @Param("userIds") List<Long> userIds,
            @Param("title") String title,
            @Param("message") String message,
            @Param("type") String type,
            @Param("now") java.time.LocalDateTime now
    );

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = """
        INSERT INTO notifications (sender_id, receiver_id, title, message, type, read, deleted, created_at, reference_id, event_type)
        SELECT NULL, u.id, :title, :message, :type, false, false, :now, :referenceId, :eventType
        FROM users u
        WHERE u.id IN (:userIds)
          AND NOT EXISTS (
              SELECT 1 FROM notifications n
              WHERE n.receiver_id = u.id
                AND (
                     (:referenceId IS NOT NULL AND n.reference_id = :referenceId AND n.event_type = :eventType)
                     OR
                     (:referenceId IS NULL AND n.title = :title)
                )
          )
    """, nativeQuery = true)
    int bulkInsertAppNotificationsWithMetadata(
            @Param("userIds") List<Long> userIds,
            @Param("title") String title,
            @Param("message") String message,
            @Param("type") String type,
            @Param("now") java.time.LocalDateTime now,
            @Param("referenceId") Long referenceId,
            @Param("eventType") String eventType
    );
}