package com.example.service;

import com.example.dto.response.UserGalleryResponseDTO;
import com.example.dto.response.UserPhotoResponseDTO;
import com.example.model.PhotoType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPhotoService {

    // =========================
    // UPLOAD
    // =========================

    UserPhotoResponseDTO upload(
            MultipartFile file,
            PhotoType type
    );

    List<UserPhotoResponseDTO> uploadMultiple(
            List<MultipartFile> files
    );

    // =========================
    // DELETE
    // =========================

    void delete(
            PhotoType type
    );

    // =========================
    // GET MY PHOTOS
    // =========================

    List<UserPhotoResponseDTO> getMyPhotos();

    String getMyProfilePhoto();

    // =========================
    // PHOTO GALLERY
    // =========================

    UserGalleryResponseDTO getPhotosByUserId(Long userId);

    long getPhotoCount(
            Long userId
    );

    // =========================
    // PRIMARY PHOTO
    // =========================

    UserPhotoResponseDTO setPrimary(
            Long photoId
    );

    void deletePhoto(Long photoId);
}