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
    private String email;
    private String phone;
    private String imageUrl;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}