package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}),
        indexes = {
                @Index(name = "idx_profile_user", columnList = "user_id"),
                @Index(name = "idx_profile_city", columnList = "city_id"),
                @Index(name = "idx_profile_caste", columnList = "caste_id"),
                @Index(name = "idx_profile_religion", columnList = "religion_id"),
                @Index(name = "idx_profile_dob", columnList = "date_of_birth"),
                @Index(name = "idx_profile_active", columnList = "is_active")
        }
)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id")
    private Caste caste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

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

    @Column(length = 1000)
    private String about;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Profile() {}
    // 🔥 PREMIUM / BOOST SYSTEM
    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "boost_score")
    private Integer boostScore = 0;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ FIXED GETTER
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Caste getCaste() {
        return caste;
    }

    public void setCaste(Caste caste) {
        this.caste = caste;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Integer getBoostScore() {
        return boostScore;
    }

    public void setBoostScore(Integer boostScore) {
        this.boostScore = boostScore;
    }
// other getters/setters same as yours...
}