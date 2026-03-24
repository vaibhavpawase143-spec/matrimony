package com.example.controller.user;

import com.example.dto.request.UserPhotoRequestDTO;
import com.example.dto.response.UserPhotoResponseDTO;
import com.example.model.PhotoType;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.service.UserPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService service;

    // =========================
    // ✅ UPLOAD / SAVE PHOTO
    // =========================
    @PostMapping
    public UserPhotoResponseDTO save(@Valid @RequestBody UserPhotoRequestDTO dto) {

        UserPhoto photo = mapToEntity(dto);

        UserPhoto saved = service.save(photo);

        return mapToResponse(saved);
    }

    // =========================
    // 🔍 GET ALL BY USER
    // =========================
    @GetMapping("/user/{userId}")
    public List<UserPhotoResponseDTO> getByUser(@PathVariable Long userId) {

        return service.getByUser(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 GET BY TYPE
    // =========================
    @GetMapping("/user/{userId}/type/{type}")
    public List<UserPhotoResponseDTO> getByType(
            @PathVariable Long userId,
            @PathVariable PhotoType type) {

        return service.getByUserAndType(userId, type)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔍 GET SINGLE (PROFILE PHOTO)
    // =========================
    @GetMapping("/user/{userId}/single/{type}")
    public UserPhotoResponseDTO getSingle(
            @PathVariable Long userId,
            @PathVariable PhotoType type) {

        UserPhoto photo = service.getSingleByUserAndType(userId, type)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        return mapToResponse(photo);
    }

    // =========================
    // 🔍 CHECK EXISTS
    // =========================
    @GetMapping("/exists")
    public Boolean exists(
            @RequestParam Long userId,
            @RequestParam PhotoType type) {

        return service.exists(userId, type);
    }

    // =========================
    // ❌ DELETE BY TYPE
    // =========================
    @DeleteMapping
    public String delete(
            @RequestParam Long userId,
            @RequestParam PhotoType type) {

        service.deleteByUserAndType(userId, type);
        return "Photo deleted successfully";
    }

    // =========================
    // 🔍 GET ALL (ADMIN)
    // =========================
    @GetMapping
    public List<UserPhotoResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private UserPhoto mapToEntity(UserPhotoRequestDTO dto) {

        UserPhoto photo = new UserPhoto();

        User user = new User();
        user.setId(dto.getUserId());
        photo.setUser(user);

        photo.setPhotoType(dto.getPhotoType());
        photo.setPhotoUrl(dto.getPhotoUrl());

        return photo;
    }

    private UserPhotoResponseDTO mapToResponse(UserPhoto photo) {

        return UserPhotoResponseDTO.builder()
                .id(photo.getId())
                .userId(photo.getUser() != null ? photo.getUser().getId() : null)
                .userName(photo.getUser() != null ? photo.getUser().getFullName() : null)
                .photoType(photo.getPhotoType())
                .photoUrl(photo.getPhotoUrl())
                .createdAt(photo.getCreatedAt())
                .updatedAt(photo.getUpdatedAt())
                .build();
    }
}