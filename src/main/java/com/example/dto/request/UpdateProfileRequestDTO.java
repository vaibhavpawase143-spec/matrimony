package com.example.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateProfileRequestDTO {

    private Long userId;

    // Basic fields
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;

    // About
    @Size(max = 1000, message = "About cannot exceed 1000 characters")
    private String about;
    private String aboutMe;

    // Profile photo
    private String imageUrl;

    // Religion & Caste
    private Long religionId;
    private Long casteId;
    private Long subCasteId;
    private Long motherTongueId;

    // Personal
    private Long maritalStatusId;
    private Long heightId;
    private Long weightId;

    // Physical details
    private String complexion;
    private String bodyType;

    // Education & Career
    private Long educationLevelId;
    private Long occupationId;
    private String annualIncome;
    private String companyName;

    // Location
    private String country;
    private String state;
    private Long cityId;
    private String city;
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
}