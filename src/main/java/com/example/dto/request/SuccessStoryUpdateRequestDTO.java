package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessStoryUpdateRequestDTO {

    @NotBlank(message = "Partner one name is required")
    @Size(max = 150, message = "Partner one name must not exceed 150 characters")
    private String partnerOneName;

    @NotBlank(message = "Partner two name is required")
    @Size(max = 150, message = "Partner two name must not exceed 150 characters")
    private String partnerTwoName;

    private String partnerOneImageUrl;
    private String partnerTwoImageUrl;
    private String coupleImageUrl;

    @NotBlank(message = "Short story is required")
    @Size(max = 1000, message = "Short story must not exceed 1000 characters")
    private String shortStory;

    private String fullStory;

    private LocalDate weddingDate;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Builder.Default
    private Boolean consentGiven = false;

    @Min(value = 0, message = "Display order cannot be negative")
    @Builder.Default
    private Integer displayOrder = 0;
}
