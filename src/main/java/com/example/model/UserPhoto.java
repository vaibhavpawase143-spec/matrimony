package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(
        name = "user_photos",
        indexes = {
                @Index(
                        name = "idx_user_photo_user",
                        columnList = "user_id"
                )
        }
)
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // PROFILE / COVER / KUNDALI / OTHER
    @Enumerated(EnumType.STRING)
    @Column(name = "photo_type", nullable = false, length = 50)
    private PhotoType photoType;

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    // Main profile photo
    @Column(name = "is_primary")
    private Boolean primaryPhoto = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPhoto() {}

    // =====================
    // Lifecycle
    // =====================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.primaryPhoto == null) {
            this.primaryPhoto = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =====================
    // Getters
    // =====================

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

    public Boolean getPrimaryPhoto() {
        return primaryPhoto;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // =====================
    // Setters
    // =====================

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPrimaryPhoto(Boolean primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }
}