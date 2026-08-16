package com.example.controller.user;

import com.example.dto.response.UserGalleryResponseDTO;
import com.example.dto.response.UserPhotoResponseDTO;
import com.example.model.PhotoType;
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
    public UserPhotoResponseDTO upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", required = false, defaultValue = "OTHER") PhotoType type
    ) {
        return service.upload(file, type);
    }

    // =========================
    // 📸 MULTIPLE UPLOAD
    // =========================

    @PostMapping("/upload-multiple")
    public List<UserPhotoResponseDTO> uploadMultiple(
            @RequestParam("files") List<MultipartFile> files
    ) {
        return service.uploadMultiple(files);
    }

    // =========================
    // ❌ DELETE BY TYPE
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
    public List<UserPhotoResponseDTO> myPhotos() {
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
    public UserPhotoResponseDTO setPrimary(
            @PathVariable Long photoId
    ) {
        return service.setPrimary(photoId);
    }

    // =========================
    // 📊 PHOTO COUNT
    // =========================

    @GetMapping("/count/{userId}")
    public long getPhotoCount(
            @PathVariable Long userId
    ) {
        return service.getPhotoCount(userId);
    }

    // =========================
    // ❌ DELETE BY PHOTO ID
    // =========================

    @DeleteMapping("/{photoId}")
    public String deletePhoto(
            @PathVariable Long photoId
    ) {
        service.deletePhoto(photoId);
        return "Photo deleted";
    }
}