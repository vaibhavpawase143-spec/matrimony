package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BodyTypeRequestDTO {

    @NotBlank(message = "Body type is required")
    @Size(min = 2, max = 50, message = "Body type must be between 2-50 characters")
    private String name;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}