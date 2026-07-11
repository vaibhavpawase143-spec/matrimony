package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FamilyDetailsResponseDto {

    private Long id;
    private Long profileId;
    private Long familyTypeId;
    private Long familyId;
    private Long brotherTypeId;
    private Long sisterTypeId;

    private String fatherOccupation;
    private String motherOccupation;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}