package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "states",
        uniqueConstraints = @UniqueConstraint(columnNames = {"country_id", "name"}),
        indexes = {
                @Index(name = "idx_state_name", columnList = "name"),
                @Index(name = "idx_state_country", columnList = "country_id")
        }
)
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // ⚠️ Avoid heavy loading (keep lazy, no cascade ALL)
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private List<City> cities;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public State() {}

    public State(String name, Boolean isActive, Country country) {
        this.name = name;
        this.isActive = isActive;
        this.country = country;
    }

    // 🔥 Lifecycle hooks (improved)
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

    public Boolean getIsActive() {
        return isActive;
    }

    public Country getCountry() {
        return country;
    }

    public List<City> getCities() {
        return cities;
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

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }


    public void setId(Long stateId) {
        this.id = stateId;
    }
}