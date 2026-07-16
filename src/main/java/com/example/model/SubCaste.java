package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "sub_castes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sub_caste_caste_name",
                        columnNames = {"caste_id", "name"}
                )
        },
        indexes = {
                @Index(name = "idx_sub_caste_name", columnList = "name"),
                @Index(name = "idx_sub_caste_caste", columnList = "caste_id"),
                @Index(name = "idx_sub_caste_admin", columnList = "admin_id"),
                @Index(name = "idx_sub_caste_active", columnList = "is_active"),
                @Index(name = "idx_sub_caste_deleted", columnList = "deleted_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // RELATIONSHIPS
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id", nullable = false)
    private Caste caste;

    // =====================================================
    // BASIC DETAILS
    // =====================================================

    @Column(nullable = false, length = 120)
    private String name;

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