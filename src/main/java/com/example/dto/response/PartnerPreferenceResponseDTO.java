package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartnerPreferenceResponseDTO {

    private Long id;
    private Long userId;

    private Integer minAge;
    private Integer maxAge;

    private Double minHeight;
    private Double maxHeight;

    private Long religionId;
    private Long casteId;
    private Long cityId;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}