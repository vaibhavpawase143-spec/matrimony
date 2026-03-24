package com.example.dto.responce;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeResponseDTO {

    private Long id;
    private String range;
    private Boolean isActive;
    private Long adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}