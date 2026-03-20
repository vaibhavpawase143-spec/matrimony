package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long planId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean isActive = true; // ✅ ADD THIS

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {   // ✅ ADD
        return isActive;
    }

    public void setIsActive(Boolean active) {  // ✅ ADD
        isActive = active;
    }
}