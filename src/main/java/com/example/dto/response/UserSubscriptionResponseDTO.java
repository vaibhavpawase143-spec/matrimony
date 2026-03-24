package com.example.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionResponseDTO {

    private Long id;

    private Long userId;
    private String userName;

    private Long planId;
    private String planName;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean isActive;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}