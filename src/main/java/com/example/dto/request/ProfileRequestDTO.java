package com.example.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileRequestDTO {

    // =====================================================
    // USER
    // =====================================================

    private Long userId;

    // =====================================================
    // BASIC DETAILS
    // =====================================================

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate dateOfBirth;

    private String email;

    private String phone;

    private String imageUrl;

    // =====================================================
    // MASTER TABLE IDS
    // =====================================================

    // Religion
    private Long religionId;

    // Caste
    private Long casteId;

    // Sub Caste
    private Long subCasteId;

    // Mother Tongue
    private Long motherTongueId;

    // Marital Status
    private Long maritalStatusId;

    // Gender
    private Long genderId;

    // Education
    private Long educationLevelId;

    // Occupation
    private Long occupationId;

    // Height
    private Long heightId;

    // Weight
    private Long weightId;

    // Body Type
    private Long bodyTypeId;

    // Complexion
    private Long complexionId;

    // Country
    private Long countryId;

    // State
    private Long stateId;

    // City
    private Long cityId;

    // =====================================================
    // NEW MASTER TABLE IDS
    // =====================================================

    // Income
    private Long incomeId;

    // Diet
    private Long dietId;

    // Smoking
    private Long smokingId;

    // Drinking
    private Long drinkingId;

    // =====================================================
    // ABOUT
    // =====================================================

    @Size(
            max = 1000,
            message = "About cannot exceed 1000 characters"
    )
    private String about;

    private String aboutMe;

    // =====================================================
    // EDUCATION & CAREER
    // =====================================================

    private String companyName;

    // =====================================================
    // LOCATION
    // =====================================================

    private String address;

    // =====================================================
    // FAMILY DETAILS
    // =====================================================

    private String fatherName;

    private String fatherOccupation;

    private String motherName;

    private String motherOccupation;

    private Integer siblingsCount;
    private Integer preferredAgeMin;
    private Integer preferredAgeMax;

    private String preferredLocation;

    private String preferredEducation;

    private String otherExpectations;
}