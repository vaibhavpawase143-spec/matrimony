package com.example.dto.response;

import java.time.LocalDateTime;

public class CasteResponseDTO {

    private Long id;
    private String name;
    private Boolean isActive;
    private Long adminId;
    private Long religionId;
    private String religionName;
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

    public Long getAdminId() {
        return adminId;
    }

    public Long getReligionId() {
        return religionId;
    }

    public String getReligionName() {
        return religionName;
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

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setReligionId(Long religionId) {
        this.religionId = religionId;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}