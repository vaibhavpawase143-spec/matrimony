package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "weights",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_weight_admin_value",
                        columnNames = {"admin_id", "value"}
                )
        },
        indexes = {
                @Index(name = "idx_weight_value", columnList = "value"),
                @Index(name = "idx_weight_admin", columnList = "admin_id"),
                @Index(name = "idx_weight_active", columnList = "is_active"),
                @Index(name = "idx_weight_deleted", columnList = "deleted_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // RELATIONSHIP
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // =====================================================
    // BASIC DETAILS
    // =====================================================

    @Column(nullable = false, length = 20)
    private String value;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // =====================================================
    // AUDIT FIELDS
    // =====================================================

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    // =====================================================
    // ENTITY LIFECYCLE
    // =====================================================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}