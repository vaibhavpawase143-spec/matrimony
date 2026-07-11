package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponseDTO {

    private Long userId;

    private Long id;

    private String firstName;

    private String lastName;

    private Boolean profileCompleted;

    private Long profileId;

    private String name;

    private String imageUrl;

    private Integer age;

    private String city;

    private String religion;

    private String caste;

    private String occupation;

    private Boolean isPremium;

    private int matchScore;

    private String matchPercentage;
}