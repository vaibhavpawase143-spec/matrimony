package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeightResponseDTO {

    private Long id;

    // Frontend dropdown compatibility
    private String name;

    private String height;

    private Long adminId;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Soft Delete
    private LocalDateTime deletedAt;

    private Long deletedBy;
}