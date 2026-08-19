package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessStoryResponseDTO {

    private Long id;
    private String partnerOneName;
    private String partnerTwoName;
    private String partnerOneImageUrl;
    private String partnerTwoImageUrl;
    private String coupleImageUrl;
    private String shortStory;
    private String fullStory;
    private LocalDate weddingDate;
    private String location;
    private Boolean consentGiven;
    private Boolean isPublished;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
