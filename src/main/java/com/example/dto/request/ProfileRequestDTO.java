package com.example.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequestDTO {
    private String imageUrl;
    private Long religionId;
    private Long casteId;
    private Long educationLevelId;
    private Long occupationId;
    private Long heightId;
    private Long weightId;
    private Long cityId;

    @Size(max = 1000, message = "About cannot exceed 1000 characters")
    private String about;

    private Long userId;
}