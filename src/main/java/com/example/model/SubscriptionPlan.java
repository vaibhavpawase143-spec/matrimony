package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "subscription_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"admin_id", "name"})
)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // Example: Gold Plan
    private Double price;           // Plan price
    private Integer durationDays;   // Plan duration

    private String description;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public SubscriptionPlan() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}