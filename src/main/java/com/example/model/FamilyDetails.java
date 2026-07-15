package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "family_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_id"})
        },
        indexes = {
                @Index(name = "idx_family_details_profile", columnList = "profile_id"),
                @Index(name = "idx_family_details_family", columnList = "family_id"),
                @Index(name = "idx_family_details_deleted_at", columnList = "deleted_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_type_id")
    private FamilyType familyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brother_type_id")
    private BrotherType brotherType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sister_type_id")
    private SisterType sisterType;

    @Column(name = "father_occupation", length = 150)
    private String fatherOccupation;

    @Column(name = "mother_occupation", length = 150)
    private String motherOccupation;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Soft Delete
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}