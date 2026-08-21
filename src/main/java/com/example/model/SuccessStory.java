package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "success_stories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partner_one_name", nullable = false, length = 150)
    private String partnerOneName;

    @Column(name = "partner_two_name", nullable = false, length = 150)
    private String partnerTwoName;

    @Column(name = "partner_one_image_url", columnDefinition = "TEXT")
    private String partnerOneImageUrl;

    @Column(name = "partner_two_image_url", columnDefinition = "TEXT")
    private String partnerTwoImageUrl;

    @Column(name = "couple_image_url", columnDefinition = "TEXT")
    private String coupleImageUrl;

    @Column(name = "short_story", nullable = false, length = 1000)
    private String shortStory;

    @Column(name = "full_story", columnDefinition = "TEXT")
    private String fullStory;

    @Column(name = "wedding_date")
    private LocalDate weddingDate;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "consent_given", nullable = false)
    @Builder.Default
    private Boolean consentGiven = false;

    @Column(name = "is_published", nullable = false)
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "publish_version", nullable = false)
    @Builder.Default
    private Integer publishVersion = 0;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.consentGiven == null) this.consentGiven = false;
        if (this.isPublished == null) this.isPublished = false;
        if (this.displayOrder == null) this.displayOrder = 0;
        if (this.publishVersion == null) this.publishVersion = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
