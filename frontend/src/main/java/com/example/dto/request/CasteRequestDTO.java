package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CasteRequestDTO {

    @NotBlank(message = "Caste name is required")
    @Size(min = 2, max = 100, message = "Caste name must be 2-100 characters")
    private String name;

    @NotNull(message = "Religion ID is required")
    private Long religionId;

    private Boolean isActive = true;

    // ================= GETTERS & SETTERS =================

    public String getName() {
        return name;
    }

    public Long getReligionId() {
        return religionId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReligionId(Long religionId) {
        this.religionId = religionId;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}