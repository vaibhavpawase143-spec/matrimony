package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyResponseDto {

    private Long id;

    private Long adminId;

    private String name;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Soft Delete
    private LocalDateTime deletedAt;

    private Long deletedBy;
}