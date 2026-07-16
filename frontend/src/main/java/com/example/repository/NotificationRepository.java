package com.example.repository;

import com.example.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ Get all notifications for a user (not deleted, latest first)
    List<Notification> findByReceiverIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);

    // ✅ Count unread notifications
    long countByReceiverIdAndReadFalseAndDeletedFalse(Long userId);
}