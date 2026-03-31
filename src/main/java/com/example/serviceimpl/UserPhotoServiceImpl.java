package com.example.serviceimpl;

import com.example.model.UserPhoto;
import com.example.model.PhotoType;
import com.example.repository.UserPhotoRepository;
import com.example.service.UserPhotoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserPhotoServiceImpl implements UserPhotoService {

    private final UserPhotoRepository repository;

    public UserPhotoServiceImpl(UserPhotoRepository repository) {
        this.repository = repository;
    }

    // =========================
    // ✅ SAVE / REPLACE PHOTO
    // =========================
    @Override
    @Transactional
    public UserPhoto save(UserPhoto photo) {

        Long userId = photo.getUser().getId();
        PhotoType type = photo.getPhotoType();

        // 🔥 Replace existing (PROFILE / KUNDALI)
        if (repository.existsByUserIdAndPhotoType(userId, type)) {
            repository.deleteByUserIdAndPhotoType(userId, type);
        }

        return repository.save(photo);
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @Override
    public Optional<UserPhoto> getById(Long id) {
        return repository.findById(id);
    }

    // =========================
    // 🔍 GET ALL BY USER
    // =========================
    @Override
    public List<UserPhoto> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // =========================
    // 🔍 GET BY TYPE
    // =========================
    @Override
    public List<UserPhoto> getByUserAndType(Long userId, PhotoType photoType) {
        return repository.findByUserIdAndPhotoType(userId, photoType);
    }

    // =========================
    // 🔍 GET SINGLE
    // =========================
    @Override
    public Optional<UserPhoto> getSingleByUserAndType(Long userId, PhotoType photoType) {
        return repository.findFirstByUserIdAndPhotoType(userId, photoType);
    }

    // =========================
    // 🔍 EXISTS
    // =========================
    @Override
    public boolean exists(Long userId, PhotoType photoType) {
        return repository.existsByUserIdAndPhotoType(userId, photoType);
    }

    // =========================
    // ❌ DELETE
    // =========================
    @Override
    @Transactional
    public void deleteByUserAndType(Long userId, PhotoType photoType) {

        if (!repository.existsByUserIdAndPhotoType(userId, photoType)) {
            throw new RuntimeException("Photo not found");
        }

        repository.deleteByUserIdAndPhotoType(userId, photoType);
    }

    // =========================
    // 🔍 GET ALL
    // =========================
    @Override
    public List<UserPhoto> getAll() {
        return repository.findAll();
    }
}