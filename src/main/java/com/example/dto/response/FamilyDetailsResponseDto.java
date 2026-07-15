package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Soft Delete
    private LocalDateTime deletedAt;

    private Long deletedBy;
}