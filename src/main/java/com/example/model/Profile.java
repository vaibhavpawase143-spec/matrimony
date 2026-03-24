package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}),
        indexes = {
                @Index(name = "idx_profile_user", columnList = "user_id"),
                @Index(name = "idx_profile_city", columnList = "city_id"),
                @Index(name = "idx_profile_caste", columnList = "caste_id")
        }
)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 One profile per user
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id")
    private Caste caste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id")
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "height_id")
    private Height height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_id")
    private Weight weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(length = 1000)
    private String about;

    // 🔥 Audit fields
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Profile() {}

    // 🔥 Lifecycle hooks
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

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Religion getReligion() { return religion; }
    public Caste getCaste() { return caste; }
    public EducationLevel getEducationLevel() { return educationLevel; }
    public Occupation getOccupation() { return occupation; }
    public Height getHeight() { return height; }
    public Weight getWeight() { return weight; }
    public City getCity() { return city; }
    public String getAbout() { return about; }
    public Boolean getIsActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ===== Setters =====

    public void setUser(User user) { this.user = user; }
    public void setReligion(Religion religion) { this.religion = religion; }
    public void setCaste(Caste caste) { this.caste = caste; }
    public void setEducationLevel(EducationLevel educationLevel) { this.educationLevel = educationLevel; }
    public void setOccupation(Occupation occupation) { this.occupation = occupation; }
    public void setHeight(Height height) { this.height = height; }
    public void setWeight(Weight weight) { this.weight = weight; }
    public void setCity(City city) { this.city = city; }
    public void setAbout(String about) { this.about = about; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}