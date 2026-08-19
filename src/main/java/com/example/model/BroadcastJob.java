package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "broadcast_jobs")
public class BroadcastJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private BroadcastJobStatus status;

    @Column(name = "last_processed_user_id", nullable = false)
    private Long lastProcessedUserId;

    @Column(name = "total_recipients", nullable = false)
    private Long totalRecipients;

    @Column(name = "enqueued_recipients", nullable = false)
    private Long enqueuedRecipients;

    @Column(name = "processed_recipients", nullable = false)
    private Long processedRecipients;

    @Column(name = "successful_recipients", nullable = false)
    private Long successfulRecipients;

    @Column(name = "failed_recipients", nullable = false)
    private Long failedRecipients;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "created_by_admin_id")
    private Long createdByAdminId;

    @Column(name = "is_test_mode")
    private Boolean isTestMode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = BroadcastJobStatus.PENDING;
        }
        if (this.lastProcessedUserId == null) {
            this.lastProcessedUserId = 0L;
        }
        if (this.totalRecipients == null) {
            this.totalRecipients = 0L;
        }
        if (this.enqueuedRecipients == null) {
            this.enqueuedRecipients = 0L;
        }
        if (this.processedRecipients == null) {
            this.processedRecipients = 0L;
        }
        if (this.successfulRecipients == null) {
            this.successfulRecipients = 0L;
        }
        if (this.failedRecipients == null) {
            this.failedRecipients = 0L;
        }
        if (this.isTestMode == null) {
            this.isTestMode = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
