package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "broadcast_recipient_status",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_broadcast_recipient_job_user", columnNames = {"broadcast_job_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_brs_job_id", columnList = "broadcast_job_id"),
                @Index(name = "idx_brs_job_app_status", columnList = "broadcast_job_id, app_notification_status"),
                @Index(name = "idx_brs_job_email_status", columnList = "broadcast_job_id, email_status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BroadcastRecipientStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "broadcast_job_id", nullable = false)
    private Long broadcastJobId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_notification_status", nullable = false)
    @Builder.Default
    private AppNotificationStatus appNotificationStatus = AppNotificationStatus.QUEUED;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", nullable = false)
    @Builder.Default
    private RecipientEmailStatus emailStatus = RecipientEmailStatus.QUEUED;

    @Column(name = "email_attempt_count", nullable = false)
    @Builder.Default
    private Integer emailAttemptCount = 0;

    @Column(name = "email_error", columnDefinition = "TEXT")
    private String emailError;

    @Column(name = "notification_processed_at")
    private LocalDateTime notificationProcessedAt;

    @Column(name = "email_queued_at")
    private LocalDateTime emailQueuedAt;

    @Column(name = "email_accepted_at")
    private LocalDateTime emailAcceptedAt;

    @Column(name = "email_delivered_at")
    private LocalDateTime emailDeliveredAt;

    @Column(name = "aggregate_processed", nullable = false)
    @Builder.Default
    private Boolean aggregateProcessed = false;

    @Column(name = "aggregate_processed_at")
    private LocalDateTime aggregateProcessedAt;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
