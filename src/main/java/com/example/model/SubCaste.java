package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "sub_castes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"caste_id", "name"}),
        indexes = {
                @Index(name = "idx_subcaste_name", columnList = "name"),
                @Index(name = "idx_subcaste_caste", columnList = "caste_id")
        }
)
public class SubCaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id", nullable = false)
    private Caste caste;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SubCaste() {}

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

    public String getName() {
        return name;
    }

    public Caste getCaste() {
        return caste;
    }

    public Boolean getIsActive() {   // ✅ FIXED
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCaste(Caste caste) {
        this.caste = caste;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}