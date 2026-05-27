package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComplexionRequestDTO {

    @NotBlank(message = "Complexion value is required")
    @Size(min = 2, max = 120, message = "Complexion must be 2-120 characters")
    private String value;

    @NotNull(message = "Admin ID is required")
    private Long adminId;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getValue() {
        return value;
    }

    public Long getAdminId() {
        return adminId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}