package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "states",
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
    // State name
    @Column(nullable = false, length = 120)
    private String name;

    // Active / Inactive
    @Column(nullable = false)
    private Boolean status = true;

    // Many states belong to one country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // One state has many cities
    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<City> cities;

    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public State() {
    }

    public State(String name, Boolean status, Country country) {
        this.name = name;
        this.status = status;
        this.country = country;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
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

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}