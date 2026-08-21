package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "admin_notifications",
        indexes = {
                @Index(name = "idx_admin_notification_admin", columnList = "admin_id"),
                @Index(name = "idx_admin_notification_read", columnList = "read"),
                @Index(name = "idx_admin_notification_created", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Receiver Admin
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    /**
     * Notification Title
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * Notification Message
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Notification Type
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    /**
     * Read Status
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean read = false;

    /**
     * Soft Delete
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "event_type", length = 50)
    private String eventType;

    /**
     * Creation Time
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (read == null) {
            read = false;
        }

        if (deleted == null) {
            deleted = false;
        }
    }
}