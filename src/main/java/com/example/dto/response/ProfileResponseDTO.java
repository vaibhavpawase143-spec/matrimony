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
    private String gender;
    private LocalDate dateOfBirth;

    private Long religionId;
    private String religionName;

    private Long casteId;
    private String casteName;

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

    private String about;

    private Boolean isActive;
    private Integer currentStep;
    private Boolean profileCompleted;

    private String message; // For error/status messages

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}