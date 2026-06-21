package com.example.controller.user;

import com.example.dto.response.UserGalleryResponseDTO;
import com.example.model.PhotoType;
import com.example.model.UserPhoto;
import com.example.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService service;

    // =========================
    // 📸 SINGLE UPLOAD
    // =========================

    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file,
            @RequestParam PhotoType type
    ) {

        return service.upload(
                file,
                type
        );
    }

    // =========================
    // 📸 MULTIPLE UPLOAD
    // =========================

    @PostMapping("/upload-multiple")
    public List<String> uploadMultiple(
            @RequestParam List<MultipartFile> files
    ) {

        return service.uploadMultiple(
                files
        );
    }

    // =========================
    // ❌ DELETE
    // =========================

    @DeleteMapping("/delete")
    public String delete(
            @RequestParam PhotoType type
    ) {

        service.delete(type);

        return "Photo deleted successfully";
    }

    // =========================
    // 🔍 MY PHOTOS
    // =========================

    @GetMapping("/me")
    public List<UserPhoto> myPhotos() {

        return service.getMyPhotos();
    }

    // =========================
    // 🔍 MY PRIMARY PHOTO
    // =========================

    @GetMapping("/me/profile")
    public String myProfilePhoto() {

        return service.getMyProfilePhoto();
    }

    // =========================
    // 🔍 USER GALLERY
    // =========================

    @GetMapping("/user/{userId}")
    public UserGalleryResponseDTO getUserPhotos(
            @PathVariable Long userId
    ) {

        return service.getPhotosByUserId(userId);

    }

    // =========================
    // ⭐ SET PRIMARY PHOTO
    // =========================

    @PutMapping("/primary/{photoId}")
    public String setPrimary(
            @PathVariable Long photoId
    ) {

        service.setPrimary(
                photoId
        );

        return "Primary photo updated";
    }

    // =========================
    // 📊 PHOTO COUNT
    // =========================

    @GetMapping("/count/{userId}")
    public long getPhotoCount(
            @PathVariable Long userId
    ) {

        return service.getPhotoCount(
                userId
        );
    }
    @DeleteMapping("/{photoId}")
    public String deletePhoto(
            @PathVariable Long photoId
    ) {

        service.deletePhoto(photoId);

        return "Photo deleted";

    }
}