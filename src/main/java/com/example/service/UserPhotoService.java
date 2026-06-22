package com.example.service;

import com.example.dto.response.UserGalleryResponseDTO;
import com.example.model.PhotoType;
import com.example.model.UserPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPhotoService {

    // =========================
    // UPLOAD
    // =========================

    String upload(
            MultipartFile file,
            PhotoType type
    );

    List<String> uploadMultiple(
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

    List<UserPhoto> getMyPhotos();

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

    void setPrimary(
            Long photoId
    );

    void deletePhoto(Long photoId);
}