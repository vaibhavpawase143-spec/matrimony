package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(
        name = "partner_preferences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}),
        indexes = {
                @Index(name = "idx_pref_user", columnList = "user_id"),
                @Index(name = "idx_pref_religion", columnList = "religion_id"),
                @Index(name = "idx_pref_caste", columnList = "caste_id"),
                @Index(name = "idx_pref_city", columnList = "city_id")
        }
)
public class PartnerPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 FIX HERE (IMPORTANT)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    private Integer minAge;
    private Integer maxAge;
    @ManyToOne
    @JoinColumn(name="education_level_id")
    private EducationLevel educationLevel;
    @Column(length=1000)
    private String otherExpectations;
    @ManyToOne
    @JoinColumn(name="occupation_id")
    private Occupation occupation;

    @ManyToOne
    @JoinColumn(name="marital_status_id")
    private MaritalStatus maritalStatus;

    @ManyToOne
    @JoinColumn(name="smoking_id")
    private Smoking smoking;

    @ManyToOne
    @JoinColumn(name="drinking_id")
    private Drinking drinking;

    @ManyToOne
    @JoinColumn(name="diet_id")
    private Diet diet;
    private Long minHeight;
    private Long maxHeight;
    private Long minWeight;

    private Long maxWeight;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Religion religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Caste caste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private City city;
    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(
            EducationLevel educationLevel
    ){
        this.educationLevel =
                educationLevel;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(
            Occupation occupation
    ){
        this.occupation =
                occupation;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(
            MaritalStatus maritalStatus
    ){
        this.maritalStatus =
                maritalStatus;
    }

    public Smoking getSmoking() {
        return smoking;
    }

    public void setSmoking(
            Smoking smoking
    ){
        this.smoking =
                smoking;
    }

    public Drinking getDrinking() {
        return drinking;
    }

    public void setDrinking(
            Drinking drinking
    ){
        this.drinking =
                drinking;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(
            Diet diet
    ){
        this.diet =
                diet;
    }
    // 🔥 Audit fields
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PartnerPreference() {}

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

    public Integer getMinAge() { return minAge; }

    public Integer getMaxAge() { return maxAge; }

    public Long getMinHeight() { return minHeight; }

    public Long getMaxHeight() { return maxHeight; }

    public Religion getReligion() { return religion; }

    public Caste getCaste() { return caste; }

    public City getCity() { return city; }

    public Boolean getIsActive() { return isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ===== Setters =====

    public void setUser(User user) { this.user = user; }

    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public void setMinHeight(Long minHeight) { this.minHeight = minHeight; }

    public void setMaxHeight(Long maxHeight) { this.maxHeight = maxHeight; }

    public void setReligion(Religion religion) { this.religion = religion; }

    public void setCaste(Caste caste) { this.caste = caste; }

    public void setCity(City city) { this.city = city; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Long getMinWeight() {

        return minWeight;

    }

    public Long getMaxWeight() {

        return maxWeight;

    }
    public void setMinWeight(
            Long minWeight
    ) {

        this.minWeight = minWeight;

    }

    public void setMaxWeight(
            Long maxWeight
    ) {

        this.maxWeight = maxWeight;

    }

}