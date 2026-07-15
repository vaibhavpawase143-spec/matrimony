package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "family_status",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_family_status_name_admin", columnNames = {"name", "admin_id"})
        },
        indexes = {
                @Index(name = "idx_family_status_admin", columnList = "admin_id"),
                @Index(name = "idx_family_status_active", columnList = "is_active")
        }
)
public class FamilyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // ✅ FIXED
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // ✅ FIXED (consistent with project)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // for query usage
    @Column(name = "admin_id", insertable = false, updatable = false)
    private Long adminId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FamilyStatus() {}

    public FamilyStatus(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Long getAdminId() {
        return adminId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}