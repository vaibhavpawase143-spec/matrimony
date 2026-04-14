package com.example.serviceimpl;

import com.example.model.*;
import com.example.repository.*;
import com.example.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhotoServiceImpl implements UserPhotoService {

    private final UserPhotoRepository repository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FileStorageService fileStorageService;

    private static final String BASE_URL = "http://localhost:9090/uploads/";

    // =========================
    // 📸 UPLOAD / UPDATE
    // =========================
    @Override
    @Transactional
    public String upload(MultipartFile file, PhotoType type) {

        User user = getLoggedInUser();

        repository.findFirstByUserIdAndPhotoType(user.getId(), type)
                .ifPresent(existing -> {
                    deletePhysical(existing.getPhotoUrl());
                    repository.delete(existing);
                });

        String fileName = fileStorageService.storeFile(file);
        String fileUrl = BASE_URL + fileName;

        UserPhoto photo = new UserPhoto();
        photo.setUser(user);
        photo.setPhotoType(type);
        photo.setPhotoUrl(fileUrl);

        repository.save(photo);

        if (type == PhotoType.PROFILE) {
            updateProfile(user, fileUrl);
        }

        return fileUrl;
    }

    // =========================
    // 📸 MULTIPLE
    // =========================
    @Override
    @Transactional
    public List<String> uploadMultiple(List<MultipartFile> files) {

        User user = getLoggedInUser();

        return files.stream().map(file -> {

            String fileName = fileStorageService.storeFile(file);
            String fileUrl = BASE_URL + fileName;

            UserPhoto photo = new UserPhoto();
            photo.setUser(user);
            photo.setPhotoType(PhotoType.OTHER);
            photo.setPhotoUrl(fileUrl);

            repository.save(photo);

            return fileUrl;

        }).toList();
    }

    // =========================
    // ❌ DELETE
    // =========================
    @Override
    @Transactional
    public void delete(PhotoType type) {

        User user = getLoggedInUser();

        UserPhoto photo = repository
                .findFirstByUserIdAndPhotoType(user.getId(), type)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        deletePhysical(photo.getPhotoUrl());
        repository.delete(photo);

        if (type == PhotoType.PROFILE) {
            updateProfile(user, null);
        }
    }

    // =========================
    // 🔍 GET MY PHOTOS
    // =========================
    @Override
    public List<UserPhoto> getMyPhotos() {
        return repository.findByUserId(getLoggedInUser().getId());
    }

    @Override
    public String getMyProfilePhoto() {
        return repository
                .findFirstByUserIdAndPhotoType(getLoggedInUser().getId(), PhotoType.PROFILE)
                .map(UserPhoto::getPhotoUrl)
                .orElse(null);
    }

    // =========================
    // 🔧 HELPERS
    // =========================

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void deletePhysical(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        fileStorageService.deleteFile(fileName);
    }

    private void updateProfile(User user, String url) {

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setImageUrl(url);
        profileRepository.save(profile);
    }
}