package com.example.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProfileResponseDTO {

    private Long id;

    private String imageUrl;

    private Long userId;

    private String userName;

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    // =====================================================
    // RELIGION
    // =====================================================

    private Long religionId;

    private String religionName;

    // =====================================================
    // CASTE
    // =====================================================

    private Long casteId;

    private String casteName;

    // =====================================================
    // SUB CASTE
    // =====================================================

    private Long subCasteId;

    private String subCasteName;

    // =====================================================
    // EDUCATION
    // =====================================================

    private Long educationLevelId;

    private String educationLevelName;

    // =====================================================
    // OCCUPATION
    // =====================================================

    private Long occupationId;

    private String occupationName;

    // =====================================================
    // HEIGHT
    // =====================================================

    private Long heightId;

    private String heightValue;

    // =====================================================
    // WEIGHT
    // =====================================================

    private Long weightId;

    private String weightValue;

    // =====================================================
    // CITY
    // =====================================================

    private Long cityId;

    private String cityName;

    // =====================================================
    // STATE
    // =====================================================

    private Long stateId;

    private String stateName;

    // =====================================================
    // COUNTRY
    // =====================================================

    private Long countryId;

    private String countryName;

    // =====================================================
    // MOTHER TONGUE
    // =====================================================

    private Long motherTongueId;

    private String motherTongueName;

    // =====================================================
    // MARITAL STATUS
    // =====================================================

    private Long maritalStatusId;

    private String maritalStatusName;

    // =====================================================
    // GENDER
    // =====================================================

    private Long genderId;

    private String genderName;

    // =====================================================
    // BODY TYPE
    // =====================================================

    private Long bodyTypeId;

    private String bodyTypeName;

    // =====================================================
    // COMPLEXION
    // =====================================================

    private Long complexionId;

    private String complexionName;

    private String manglikStatusName;
    private String familyTypeName;
    private String familyStatusName;
    private String familyValueName;
    private String qualificationName;
    private String fieldOfStudyName;
    private String employedStatusName;
    private String disabilityStatusName;
    private String bloodGroupName;
    private String brotherTypeName;
    private String sisterTypeName;
    // =====================================================
    // EXTRA DETAILS
    // =====================================================
    private Long profileTypeId;
    private Long manglikStatusId;

    private Long familyTypeId;

    private Long familyStatusId;

    private Long familyValueId;

    private Long qualificationId;

    private Long fieldOfStudyId;

    private Long employedStatusId;

    private Long disabilityStatusId;

    private Long bloodGroupId;


    private String profileTypeName;


    private String companyName;

    private String address;

// =====================================================
// INCOME
// =====================================================

    private Long incomeId;
    private String incomeValue;

// =====================================================
// DIET
// =====================================================

    private Long dietId;
    private String dietValue;

// =====================================================
// SMOKING
// =====================================================

    private Long smokingId;
    private String smokingValue;

// =====================================================
// DRINKING
// =====================================================

    private Long drinkingId;
    private String drinkingValue;

    // =====================================================
    // FAMILY
    // =====================================================

    private String fatherName;

    private String fatherOccupation;

    private String motherName;

    private String motherOccupation;

    private Integer siblingsCount;


    // =====================================================
    // ABOUT
    // =====================================================

    private String about;

    // =====================================================
    // SYSTEM
    // =====================================================

    private Boolean isActive;
    private Boolean verified;
    private Integer currentStep;

    private Boolean profileCompleted;

    private Boolean isPremium;

    private Integer boostScore;

    private String message;

    // =====================================================
    // AUDIT
    // =====================================================

    private LocalDateTime createdAt;
    private String aboutMe;
    private String cityValue;
    private LocalDateTime updatedAt;
}