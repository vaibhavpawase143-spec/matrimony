package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MotherTongueResponseDTO {

    private Long id;
    private String name;
    private Boolean isActive;
    private Long adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}