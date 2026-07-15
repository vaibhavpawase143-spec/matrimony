package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BodyTypeRequestDTO {

    @NotBlank(message = "Body type is required")
    @Size(min = 2, max = 50, message = "Body type must be between 2-50 characters")
    private String value;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getValue() {
        return value;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}