package com.example.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequestDTO {

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

    private Long religionId;
    private Long casteId;
    private Long subCasteId;
    private Long motherTongueId;
    private Long maritalStatusId;
    private Long profileTypeId;
    private Long genderId;

    // Education
    private Long educationLevelId;
    private String educationOther;

    // Occupation
    private Long occupationId;

    private Long heightId;
    private Long weightId;
    private Long bodyTypeId;
    private Long complexionId;
    private Long countryId;
    private Long stateId;
    private Long cityId;

    // =====================================================
    // NEW MASTER TABLE IDS
    // =====================================================

    private Long incomeId;
    private Long dietId;
    private Long smokingId;
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
    private String qualificationOther;

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
    // =====================================================
    // EXTENDED FAMILY DETAILS
    // =====================================================

    private String aunt;

    private Integer sistersCount;

    private Integer brothersCount;

    private String nanihalDetails;

    private String bestFriend;

    private Integer unclesCount;

    private String uncle1Name;

    private String uncle2Name;

    private String uncle3Name;

    private String uncle4Name;

    private Long manglikStatusId;
    private Long familyTypeId;
    private Long qualificationId;
    private Long fieldOfStudyId;
    private Long employedId;
    private Long disabilityStatusId;
    private Long bloodGroupId;
    private Long familyStatusId;
    private Long familyValueId;
}