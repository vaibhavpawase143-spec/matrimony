package com.example.dto.request;

import com.example.model.PhotoType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPhotoRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Photo type is required")
    private PhotoType photoType;

    @NotBlank(message = "Photo URL is required")
    private String photoUrl;
}