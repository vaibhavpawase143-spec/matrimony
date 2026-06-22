package com.example.serviceimpl;

import com.example.dto.response.UserGalleryResponseDTO;
import com.example.dto.response.UserPhotoResponseDTO;
import com.example.model.PhotoType;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.repository.ProfileRepository;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;
import com.example.service.FileStorageService;
import com.example.service.SubscriptionService;
import com.example.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPhotoServiceImpl implements UserPhotoService {

    private final UserPhotoRepository repository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FileStorageService fileStorageService;
    private final SubscriptionService subscriptionService;
    private static final String BASE_URL = "http://localhost:9090/uploads/";

    // =========================
    // 📸 UPLOAD / UPDATE
    // =========================
    @Override
    @Transactional
    public String upload(
            MultipartFile file,
            PhotoType type
    ) {

        User user =
                getLoggedInUser();

        long totalPhotos =
                repository.countByUserId(
                        user.getId()
                );

        if (totalPhotos >= 8) {

            throw new RuntimeException(
                    "Maximum 8 photos allowed"
            );
        }

        String fileName =
                fileStorageService.storeFile(file);

        String fileUrl =
                BASE_URL + fileName;

        UserPhoto photo =
                new UserPhoto();

        photo.setUser(user);
        photo.setPhotoType(type);
        photo.setPhotoUrl(fileUrl);

        if (totalPhotos == 0) {

            photo.setPrimaryPhoto(true);

            updateProfile(
                    user,
                    fileUrl
            );
        }

        repository.save(photo);

        return fileUrl;
    }
    // =========================
    // 📸 MULTIPLE
    // =========================
    @Override
    @Transactional
    public List<String> uploadMultiple(
            List<MultipartFile> files
    ) {

        User user =
                getLoggedInUser();

        long existing =
                repository.countByUserId(
                        user.getId()
                );

        if (existing + files.size() > 8) {

            throw new RuntimeException(
                    "Maximum 8 photos allowed"
            );

        }

        return files.stream().map(file -> {

            String fileName =
                    fileStorageService.storeFile(file);

            String fileUrl =
                    BASE_URL + fileName;

            UserPhoto photo =
                    new UserPhoto();

            photo.setUser(user);
            photo.setPhotoType(PhotoType.OTHER);
            photo.setPhotoUrl(fileUrl);

            if (
                    repository.countByUserId(
                            user.getId()
                    ) == 0
            ) {

                photo.setPrimaryPhoto(true);

                updateProfile(
                        user,
                        fileUrl
                );
            }

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
                .findFirstByUserIdAndPrimaryPhotoTrue(
                        getLoggedInUser().getId()
                )
                .map(UserPhoto::getPhotoUrl)
                .orElse(null);
    }

    @Override
    public UserGalleryResponseDTO getPhotosByUserId(Long userId) {

        User currentUser = getLoggedInUser();

        List<UserPhoto> allPhotos =
                repository.findByUserId(userId);

        boolean hasGallery =
                allPhotos.size() > 1;

        boolean premiumRequired = false;

        List<UserPhoto> visiblePhotos;

        // Owner
        if (currentUser.getId().equals(userId)) {

            visiblePhotos = allPhotos;

        }

        // Premium viewer
        else if (subscriptionService.isCurrentUserPremium()) {

            visiblePhotos = allPhotos;

        }

        // Free viewer
        else {

            premiumRequired = hasGallery;

            visiblePhotos = repository
                    .findFirstByUserIdAndPrimaryPhotoTrue(userId)
                    .map(List::of)
                    .orElse(List.of());
        }

        List<UserPhotoResponseDTO> photoDtos =
                visiblePhotos.stream()
                        .map(photo ->
                                UserPhotoResponseDTO.builder()
                                        .id(photo.getId())
                                        .userId(photo.getUser().getId())

                                        .photoType(photo.getPhotoType())
                                        .photoUrl(photo.getPhotoUrl())
                                        .createdAt(photo.getCreatedAt())
                                        .updatedAt(photo.getUpdatedAt())
                                        .build()
                        )
                        .collect(Collectors.toList());

        return UserGalleryResponseDTO.builder()
                .photos(photoDtos)
                .hasGallery(hasGallery)
                .premiumRequired(premiumRequired)
                .totalPhotos(allPhotos.size())
                .build();
    }
    @Override
    public long getPhotoCount(
            Long userId
    ) {

        return repository.countByUserId(
                userId
        );
    }

    @Override
    @Transactional
    public void setPrimary(
            Long photoId
    ) {

        User user =
                getLoggedInUser();

        UserPhoto photo =
                repository.findById(photoId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Photo not found"
                                )
                        );

        if (
                !photo.getUser().getId()
                        .equals(user.getId())
        ) {

            throw new RuntimeException(
                    "Access denied"
            );
        }

        repository.clearPrimaryPhotos(
                user.getId()
        );

        photo.setPrimaryPhoto(true);

        repository.save(photo);

        updateProfile(
                user,
                photo.getPhotoUrl()
        );
    }

    @Override
    @Transactional
    public void deletePhoto(Long photoId) {

        User user = getLoggedInUser();

        UserPhoto photo =
                repository.findById(photoId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Photo not found"
                                )
                        );

        if (
                !photo.getUser()
                        .getId()
                        .equals(user.getId())
        ) {

            throw new RuntimeException(
                    "Access denied"
            );

        }

        // Check whether the deleted photo is the primary photo
        boolean wasPrimary =
                Boolean.TRUE.equals(
                        photo.getPrimaryPhoto()
                );
        deletePhysical(
                photo.getPhotoUrl()
        );

        repository.delete(photo);

        // If the deleted photo was primary,
        // make another photo primary automatically
        if (wasPrimary) {

            repository
                    .findFirstByUserIdOrderByCreatedAtAsc(
                            user.getId()
                    )
                    .ifPresent(nextPhoto -> {

                        nextPhoto.setPrimaryPhoto(true);

                        repository.save(nextPhoto);

                        updateProfile(
                                user,
                                nextPhoto.getPhotoUrl()
                        );

                    });

        }

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