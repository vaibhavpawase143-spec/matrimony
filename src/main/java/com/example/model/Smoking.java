package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "smoking",
        uniqueConstraints = @UniqueConstraint(columnNames = {"admin_id", "value"}),
        indexes = {
                @Index(name = "idx_smoking_value", columnList = "value")
        }
)
public class Smoking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Example: Non-Smoker, Occasional, Regular
    @Column(nullable = false, length = 50)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Smoking() {}

    // 🔥 Lifecycle hooks (fixed)
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ===== Setters =====

    public void setValue(String value) {
        this.value = value;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}