package com.example.service;

import com.example.model.UserPhoto;
import com.example.model.PhotoType;

import java.util.List;
import java.util.Optional;

public interface UserPhotoService {

    // ✅ Upload / Save photo
    UserPhoto save(UserPhoto photo);

    // 🔍 Get by ID
    Optional<UserPhoto> getById(Long id);

    // 🔍 All photos of a user
    List<UserPhoto> getByUser(Long userId);

    // 🔍 Photos by type (PROFILE, GALLERY, KUNDALI)
    List<UserPhoto> getByUserAndType(Long userId, PhotoType photoType);

    // 🔍 Single photo (like profile pic)
    Optional<UserPhoto> getSingleByUserAndType(Long userId, PhotoType photoType);

    // 🔍 Check exists
    boolean exists(Long userId, PhotoType photoType);

    // ❌ Delete by type
    void deleteByUserAndType(Long userId, PhotoType photoType);

    // 🔍 Get all
    List<UserPhoto> getAll();
}