package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDetailsResponseDTO {

    // Partner User Id
    private Long userId;

    // Partner Full Name
    private String fullName;

    // Partner Profile Photo
    private String profilePhoto;

    // Match Percentage
    private Integer matchPercentage;

    // Total Fields Compared
    private Integer totalFields;

    // Total Matched Fields
    private Integer matchedFields;

    // All Field Comparison
    private List<FieldMatchDTO> fieldMatches;

}