package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(
        name = "admin_audit_logs",
        indexes = {
                @Index(name = "idx_audit_admin", columnList = "admin_id"),
                @Index(name = "idx_audit_action", columnList = "action"),
                @Index(name = "idx_audit_created_at", columnList = "created_at")
        }
)
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String module;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entityType;

    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    private String ipAddress;

    private String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    // ================= LIFECYCLE =================

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


}

