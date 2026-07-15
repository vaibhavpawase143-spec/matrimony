package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_photos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "photo_type"})
        },
        indexes = {
                @Index(name = "idx_user_photo_user", columnList = "user_id")
        }
)
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Owner of photo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔥 Controlled values
    @Enumerated(EnumType.STRING)
    @Column(name = "photo_type", nullable = false, length = 50)
    private PhotoType photoType;

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPhoto() {}

    // 🔥 Lifecycle hooks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public PhotoType getPhotoType() {
        return photoType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ===== Setters =====

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}