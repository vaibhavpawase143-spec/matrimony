package com.example.controller.user;

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
    // 📸 UPLOAD
    // =========================
    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file,
            @RequestParam PhotoType type
    ) {
        return service.upload(file, type);
    }

    // =========================
    // 📸 MULTIPLE
    // =========================
    @PostMapping("/upload-multiple")
    public List<String> uploadMultiple(
            @RequestParam List<MultipartFile> files
    ) {
        return service.uploadMultiple(files);
    }

    // =========================
    // ❌ DELETE
    // =========================
    @DeleteMapping("/delete")
    public String delete(@RequestParam PhotoType type) {
        service.delete(type);
        return "Photo deleted successfully";
    }

    // =========================
    // 🔍 GET MY PHOTOS
    // =========================
    @GetMapping("/me")
    public List<UserPhoto> myPhotos() {
        return service.getMyPhotos();
    }

    @GetMapping("/me/profile")
    public String myProfilePhoto() {
        return service.getMyProfilePhoto();
    }
}