package com.example.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfileRequestDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String imageUrl;
    private Long religionId;
    private Long casteId;
    private Long motherTongueId;
    private Long maritalStatusId;
    private Long educationLevelId;
    private Long occupationId;
    private Long heightId;
    private Long weightId;
    private Long cityId;

    @Size(max = 1000, message = "About cannot exceed 1000 characters")
    private String about;

    private Long userId;
}