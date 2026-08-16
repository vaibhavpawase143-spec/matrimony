package com.example.dto.response;

import com.example.model.PhotoType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhotoResponseDTO {

    private Long id;

    private Long userId;
    private String userName;

    private PhotoType photoType;

    private String photoUrl;

    @JsonProperty("primaryPhoto")
    private Boolean primaryPhoto;

    @JsonProperty("isPrimary")
    public Boolean getIsPrimary() {
        return primaryPhoto;
    }

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}