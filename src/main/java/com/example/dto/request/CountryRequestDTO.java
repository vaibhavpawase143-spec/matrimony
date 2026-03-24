package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CountryRequestDTO {

    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 120, message = "Country name must be 2-120 characters")
    private String name;

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getName() {
        return name;
    }

    public Long getAdminId() {
        return adminId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}