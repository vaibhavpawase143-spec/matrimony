package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShortlistRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Profile ID is required")
    private Long profileId;
}