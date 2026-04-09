package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    private Double minHeight;
    private Double maxHeight;

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

    public Double getMinHeight() { return minHeight; }

    public Double getMaxHeight() { return maxHeight; }

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

    public void setMinHeight(Double minHeight) { this.minHeight = minHeight; }

    public void setMaxHeight(Double maxHeight) { this.maxHeight = maxHeight; }

    public void setReligion(Religion religion) { this.religion = religion; }

    public void setCaste(Caste caste) { this.caste = caste; }

    public void setCity(City city) { this.city = city; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}