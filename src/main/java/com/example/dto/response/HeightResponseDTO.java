package com.example.dto.responce;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeightResponseDTO {

    private Long id;
    private String height;
    private Boolean isActive;
    private Long adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}