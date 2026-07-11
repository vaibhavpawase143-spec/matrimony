package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeightResponseDTO {

    private Long id;

    // IMPORTANT FOR FRONTEND DROPDOWN
    private String name;

    private String height;

    private Boolean isActive;

    private Long adminId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}