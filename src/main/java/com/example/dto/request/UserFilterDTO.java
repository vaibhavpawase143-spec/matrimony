package com.example.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserFilterDTO {

    // ==========================================
    // SEARCH
    // ==========================================

    private String search;

    // ==========================================
    // USER STATUS
    // ==========================================

    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isBlocked;

    private Boolean emailVerified;
    private Boolean phoneVerified;

    private String role;

    // ==========================================
    // PROFILE FILTERS
    // ==========================================

    private Long genderId;

    private Long religionId;

    private Long casteId;

    private Long subCasteId;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private Long maritalStatusId;

    private Long educationLevelId;

    private Long occupationId;

    // ==========================================
    // PREMIUM
    // ==========================================

    private Boolean isPremium;

    // ==========================================
    // AGE
    // ==========================================

    private Integer minAge;

    private Integer maxAge;

    // ==========================================
    // REGISTRATION DATE
    // ==========================================

    private LocalDate registeredFrom;

    private LocalDate registeredTo;
    private String sortBy;
    private String direction;

}