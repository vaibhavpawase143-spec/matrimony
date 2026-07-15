package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchExplanationResponseDTO {

    private Long profileId;

    private int totalScore;
    private String matchPercentage;

    // 🔥 ADD THIS (IMPORTANT)
    private String reason;

    private boolean religionMatch;
    private boolean casteMatch;
    private boolean cityMatch;
    private boolean ageMatch;
}