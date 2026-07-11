package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FamilyRequestDto {

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    @NotBlank(message = "Family name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private Boolean isActive = true;

    // Getters & Setters
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}