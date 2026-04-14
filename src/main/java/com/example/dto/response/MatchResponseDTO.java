package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponseDTO {

    private Long userId;
    private String name;
    private String city;
    private String religion;
    private String caste;

    private int matchScore;
    private String matchPercentage; // 🔥 NEW
}