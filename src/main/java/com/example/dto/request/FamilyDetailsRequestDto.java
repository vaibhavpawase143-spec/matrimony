package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FamilyDetailsRequestDto {

    @NotNull(message = "Profile ID is required")
    private Long profileId;

    private Long familyTypeId;
    private Long familyId;
    private Long brotherTypeId;
    private Long sisterTypeId;

    @Size(max = 150)
    private String fatherOccupation;

    @Size(max = 150)
    private String motherOccupation;

    private Boolean isActive = true;

    public @NotNull(message = "Profile ID is required") Long getProfileId() {
        return profileId;
    }

    public void setProfileId(@NotNull(message = "Profile ID is required") Long profileId) {
        this.profileId = profileId;
    }

    public Long getFamilyTypeId() {
        return familyTypeId;
    }

    public void setFamilyTypeId(Long familyTypeId) {
        this.familyTypeId = familyTypeId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getBrotherTypeId() {
        return brotherTypeId;
    }

    public void setBrotherTypeId(Long brotherTypeId) {
        this.brotherTypeId = brotherTypeId;
    }

    public Long getSisterTypeId() {
        return sisterTypeId;
    }

    public void setSisterTypeId(Long sisterTypeId) {
        this.sisterTypeId = sisterTypeId;
    }

    public @Size(max = 150) String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(@Size(max = 150) String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public @Size(max = 150) String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(@Size(max = 150) String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}