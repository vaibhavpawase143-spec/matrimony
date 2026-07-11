package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaqResponseDTO {

    private Long id;

    private String question;

    private String answer;

    private Integer displayOrder;

    private Boolean published;

    private Boolean isActive;

    private Long createdById;

    private String createdByName;

    private Long updatedById;

    private String updatedByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}