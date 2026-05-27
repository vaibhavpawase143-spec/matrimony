package com.example.dto.responce;

import java.time.LocalDateTime;

public class BodyTypeResponseDTO {

    private Long id;
    private String value;
    private Boolean isActive;
    private Long adminId;
    private LocalDateTime createdAt;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
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

    public void setValue(String value) {
        this.value = value;
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