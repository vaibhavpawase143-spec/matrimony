package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CmsPageResponseDTO {

    private Long id;

    private String pageKey;

    private String title;

    private String content;

    private Boolean published;

    private Boolean isActive;

    private Long createdById;

    private String createdByName;

    private Long updatedById;

    private String updatedByName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}