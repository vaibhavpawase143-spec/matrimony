package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CityRequestDTO {

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 100, message = "City name must be 2-100 characters")
    private String name;

    @NotNull(message = "State ID is required")
    private Long stateId;

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getName() {
        return name;
    }

    public Long getStateId() {
        return stateId;
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

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}