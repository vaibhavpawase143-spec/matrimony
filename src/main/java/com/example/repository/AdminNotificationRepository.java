package com.example.repository;

import com.example.model.Admin;
import com.example.model.AdminNotification;
import com.example.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {

    Page<AdminNotification> findByAdminAndDeletedFalse(
            Admin admin,
            Pageable pageable
    );

    Page<AdminNotification> findByAdminAndTypeInAndDeletedFalse(
            Admin admin,
            Collection<NotificationType> types,
            Pageable pageable
    );

    long countByAdminAndReadFalseAndDeletedFalse(Admin admin);

    long countByAdminAndTypeInAndReadFalseAndDeletedFalse(
            Admin admin,
            Collection<NotificationType> types
    );

    boolean existsByAdminAndTitleAndMessage(Admin admin, String title, String message);
}