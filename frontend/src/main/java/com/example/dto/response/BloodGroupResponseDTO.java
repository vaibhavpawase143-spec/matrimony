package com.example.dto.response;

import java.time.LocalDateTime;

public class BloodGroupResponseDTO {

    private Long id;
    private String type;
    private Boolean isActive;
    private Long adminId;
    private LocalDateTime createdAt;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean getIsActive() {
        return isActive;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}