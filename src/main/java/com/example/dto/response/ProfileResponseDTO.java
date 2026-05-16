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
    private String gender;
    private LocalDate dateOfBirth;

    private Long religionId;
    private String religionName;

    private Long casteId;
    private String casteName;

    private Long subCasteId;
    private String subCasteName;

    private Long educationLevelId;
    private String educationLevelName;

    private Long occupationId;
    private String occupationName;

    private Long heightId;
    private String heightValue;

    private Long weightId;
    private String weightValue;

    private Long cityId;
    private String cityName;

    private Long motherTongueId;
    private String motherTongueName;

    private Long maritalStatusId;
    private String maritalStatusName;

    // Physical details
    private String complexion;
    private String bodyType;

    // Education & Career
    private String annualIncome;
    private String companyName;

    // Location
    private String country;
    private String state;
    private String address;

    // Lifestyle
    private String diet;
    private String smoking;
    private String drinking;

    // Family details
    private String fatherName;
    private String fatherOccupation;
    private String motherName;
    private String motherOccupation;
    private String siblingsCount;

    // Partner preferences
    private String preferredAgeMin;
    private String preferredAgeMax;
    private String preferredLocation;
    private String preferredEducation;
    private String otherExpectations;

    // About
    private String about;
    private String aboutMe;

    private Boolean isActive;
    private Integer currentStep;
    private Boolean profileCompleted;

    private String message; // For error/status messages

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}