package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "family_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_id"})
        },
        indexes = {
                @Index(name = "idx_family_details_profile", columnList = "profile_id"),
                @Index(name = "idx_family_details_family", columnList = "family_id")
        }
)
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id") // add nullable=false if required
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

    @Column(length = 150)
    private String fatherOccupation;

    @Column(length = 150)
    private String motherOccupation;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FamilyDetails() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public FamilyType getFamilyType() {
        return familyType;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public BrotherType getBrotherType() {
        return brotherType;
    }

    public void setBrotherType(BrotherType brotherType) {
        this.brotherType = brotherType;
    }

    public SisterType getSisterType() {
        return sisterType;
    }

    public void setSisterType(SisterType sisterType) {
        this.sisterType = sisterType;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}