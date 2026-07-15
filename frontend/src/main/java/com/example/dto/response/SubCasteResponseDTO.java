package com.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCasteResponseDTO {

    private Long id;

    private Long adminId;
    private String adminName;

    private Long casteId;
    private String casteName;

    private String name;

    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}