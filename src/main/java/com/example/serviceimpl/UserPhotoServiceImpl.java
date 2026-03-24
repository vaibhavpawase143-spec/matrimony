package com.example.serviceimpl;

import com.example.model.UserPhoto;
import com.example.model.PhotoType;
import com.example.repository.UserPhotoRepository;
import com.example.service.UserPhotoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPhotoServiceImpl implements UserPhotoService {

    private final UserPhotoRepository repository;

    public UserPhotoServiceImpl(UserPhotoRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (handle single-type photos like PROFILE)
    @Override
    public UserPhoto save(UserPhoto photo) {

        Long userId = photo.getUser().getId();
        PhotoType type = photo.getPhotoType();

        // Replace single-type photos
        if (type == PhotoType.PROFILE || type == PhotoType.KUNDALI) {
            if (repository.existsByUserIdAndPhotoType(userId, type)) {
                repository.deleteByUserIdAndPhotoType(userId, type);
            }
        }

        return repository.save(photo);
    }

    // 🔍 Get by ID
    @Override
    public Optional<UserPhoto> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 All photos of user
    @Override
    public List<UserPhoto> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    // 🔍 By type
    @Override
    public List<UserPhoto> getByUserAndType(Long userId, PhotoType photoType) {
        return repository.findByUserIdAndPhotoType(userId, photoType);
    }

    // 🔍 Single photo (profile)
    @Override
    public Optional<UserPhoto> getSingleByUserAndType(Long userId, PhotoType photoType) {
        return repository.findFirstByUserIdAndPhotoType(userId, photoType);
    }

    // 🔍 Exists
    @Override
    public boolean exists(Long userId, PhotoType photoType) {
        return repository.existsByUserIdAndPhotoType(userId, photoType);
    }

    // ❌ Delete
    @Override
    public void deleteByUserAndType(Long userId, PhotoType photoType) {
        repository.deleteByUserIdAndPhotoType(userId, photoType);
    }

    // 🔍 Get all
    @Override
    public List<UserPhoto> getAll() {
        return repository.findAll();
    }
}