package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_blocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"blockerId", "blockedId"})
)
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long blockerId;

    @Column(nullable = false)
    private Long blockedId;

    @Column(nullable = false)
    private Boolean isActive = true; // 🔥 soft delete

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= AUTO =================
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

    // ================= GETTERS =================

    public Long getId() { return id; }
    public Long getBlockerId() { return blockerId; }
    public Long getBlockedId() { return blockedId; }
    public Boolean getIsActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ================= SETTERS =================

    public void setId(Long id) { this.id = id; }
    public void setBlockerId(Long blockerId) { this.blockerId = blockerId; }
    public void setBlockedId(Long blockedId) { this.blockedId = blockedId; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}