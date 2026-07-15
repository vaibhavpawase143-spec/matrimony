package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(
        name = "sister_types",
        uniqueConstraints = @UniqueConstraint(columnNames = {"admin_id", "value"}),
        indexes = {
                @Index(name = "idx_sister_type_value", columnList = "value")
        }
)
public class SisterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // Example: No Sister, 1 Sister, 2 Sisters
    @Column(nullable = false, length = 50)
    private String value;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SisterType() {}

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

    public Admin getAdmin() {
        return admin;
    }

    public String getValue() {
        return value;
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

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


}