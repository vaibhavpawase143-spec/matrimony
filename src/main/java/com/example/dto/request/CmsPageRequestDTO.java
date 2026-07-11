package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CmsPageRequestDTO {

    @NotBlank(message = "Page key is required")
    @Size(max = 100)
    private String pageKey;

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private Boolean published;

    private Boolean isActive;
}