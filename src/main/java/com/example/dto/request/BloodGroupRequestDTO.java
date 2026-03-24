package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BloodGroupRequestDTO {

    @NotBlank(message = "Blood group type is required")
    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "Invalid blood group (e.g., A+, O-, AB+)"
    )
    private String type;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getType() {
        return type;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}