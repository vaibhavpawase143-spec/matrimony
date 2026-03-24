package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterestResponseDTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}