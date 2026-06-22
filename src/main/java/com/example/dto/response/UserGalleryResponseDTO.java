package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGalleryResponseDTO {

    // Photos that the frontend is allowed to display
    private List<UserPhotoResponseDTO> photos;

    // Does this profile actually have gallery photos?
    private boolean hasGallery;

    // Is the gallery hidden because the viewer is not premium?
    private boolean premiumRequired;

    // Total uploaded photos
    private int totalPhotos;
}