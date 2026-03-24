package com.example.dto.responce;

import java.time.LocalDateTime;

public class CityResponseDTO {

    private Long id;
    private String name;
    private Boolean isActive;

    private Long stateId;
    private String stateName;

    private Long adminId;

    private LocalDateTime createdAt;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public Long getAdminId() {
        return adminId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}