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
@Transactional(readOnly = true)
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
    public UserPhotoResponseDTO upload(
            MultipartFile file,
            PhotoType type
    ) {
        User user = getLoggedInUser();

        long totalPhotos = repository.countByUserId(user.getId());

        String fileName = fileStorageService.storeFile(file);
        String fileUrl = BASE_URL + fileName;

        UserPhoto photo = new UserPhoto();
        photo.setUser(user);

        // If explicit profile photo update OR if user currently has 0 photos, set as primary PROFILE photo
        if (type == PhotoType.PROFILE || totalPhotos == 0) {
            if (totalPhotos >= 8) {
                repository.findFirstByUserIdAndPhotoType(user.getId(), PhotoType.PROFILE)
                        .ifPresentOrElse(
                                oldProfile -> {
                                    deletePhysical(oldProfile.getPhotoUrl());
                                    repository.delete(oldProfile);
                                },
                                () -> {
                                    repository.findFirstByUserIdOrderByCreatedAtAsc(user.getId())
                                            .ifPresent(oldest -> {
                                                deletePhysical(oldest.getPhotoUrl());
                                                repository.delete(oldest);
                                            });
                                }
                        );
            }
            repository.clearPrimaryPhotos(user.getId());
            photo.setPrimaryPhoto(true);
            photo.setPhotoType(PhotoType.PROFILE);
            updateProfile(user, fileUrl);
        } else {
            if (totalPhotos >= 8) {
                throw new RuntimeException("Maximum 8 photos allowed. Please delete a photo first.");
            }
            photo.setPrimaryPhoto(false);
            photo.setPhotoType(type != null ? type : PhotoType.OTHER);
        }

        photo.setPhotoUrl(fileUrl);
        UserPhoto saved = repository.save(photo);

        return mapToResponseDTO(saved);
    }

    // =========================
    // 📸 MULTIPLE UPLOAD (GALLERY)
    // =========================
    @Override
    @Transactional
    public List<UserPhotoResponseDTO> uploadMultiple(
            List<MultipartFile> files
    ) {
        User user = getLoggedInUser();

        long existingCount = repository.countByUserId(user.getId());

        if (existingCount + files.size() > 8) {
            throw new RuntimeException("Maximum 8 photos allowed");
        }

        boolean hasPrimary = repository.findFirstByUserIdAndPrimaryPhotoTrue(user.getId()).isPresent();

        return files.stream().map(file -> {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = BASE_URL + fileName;

            UserPhoto photo = new UserPhoto();
            photo.setUser(user);
            photo.setPhotoType(PhotoType.OTHER);
            photo.setPhotoUrl(fileUrl);

            // ONLY if user has NO primary photo at all, make the very first uploaded photo primary
            if (!hasPrimary && repository.countByUserId(user.getId()) == 0) {
                photo.setPrimaryPhoto(true);
                photo.setPhotoType(PhotoType.PROFILE);
                updateProfile(user, fileUrl);
            } else {
                photo.setPrimaryPhoto(false);
            }

            UserPhoto saved = repository.save(photo);
            return mapToResponseDTO(saved);
        }).collect(Collectors.toList());
    }

    // =========================
    // ❌ DELETE BY TYPE
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
    public List<UserPhotoResponseDTO> getMyPhotos() {
        User user = getLoggedInUser();
        List<UserPhoto> photos = repository.findByUserId(user.getId());
        return photos.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public String getMyProfilePhoto() {
        return repository
                .findFirstByUserIdAndPrimaryPhotoTrue(getLoggedInUser().getId())
                .map(UserPhoto::getPhotoUrl)
                .orElse(null);
    }

    @Override
    public UserGalleryResponseDTO getPhotosByUserId(Long userId) {
        User currentUser = getLoggedInUser();

        List<UserPhoto> allPhotos = repository.findByUserId(userId);

        boolean hasGallery = allPhotos.size() > 1;
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

        List<UserPhotoResponseDTO> photoDtos = visiblePhotos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return UserGalleryResponseDTO.builder()
                .photos(photoDtos)
                .hasGallery(hasGallery)
                .premiumRequired(premiumRequired)
                .totalPhotos(allPhotos.size())
                .build();
    }

    @Override
    public long getPhotoCount(Long userId) {
        return repository.countByUserId(userId);
    }

    // =========================
    // ⭐ SET PRIMARY PHOTO
    // =========================
    @Override
    @Transactional
    public UserPhotoResponseDTO setPrimary(Long photoId) {
        User user = getLoggedInUser();

        UserPhoto photo = repository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (!photo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // 1. Unset all existing primary photos for this user
        repository.clearPrimaryPhotos(user.getId());

        // 2. Mark the selected photo as primary
        photo.setPrimaryPhoto(true);
        photo.setPhotoType(PhotoType.PROFILE);
        UserPhoto saved = repository.save(photo);

        // 3. Keep profile entity imageUrl in sync
        updateProfile(user, photo.getPhotoUrl());

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deletePhoto(Long photoId) {
        User user = getLoggedInUser();

        UserPhoto photo = repository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (!photo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        boolean wasPrimary = Boolean.TRUE.equals(photo.getPrimaryPhoto());

        deletePhysical(photo.getPhotoUrl());
        repository.delete(photo);

        // If the deleted photo was primary, make the oldest remaining photo primary automatically
        if (wasPrimary) {
            repository.findFirstByUserIdOrderByCreatedAtAsc(user.getId())
                    .ifPresentOrElse(
                            nextPhoto -> {
                                nextPhoto.setPrimaryPhoto(true);
                                nextPhoto.setPhotoType(PhotoType.PROFILE);
                                repository.save(nextPhoto);
                                updateProfile(user, nextPhoto.getPhotoUrl());
                            },
                            () -> updateProfile(user, null)
                    );
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

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void deletePhysical(String url) {
        if (url == null || url.isBlank()) return;
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        fileStorageService.deleteFile(fileName);
    }

    private void updateProfile(User user, String url) {
        profileRepository.findByUser(user)
                .ifPresent(profile -> {
                    profile.setImageUrl(url);
                    profileRepository.save(profile);
                });
    }

    private UserPhotoResponseDTO mapToResponseDTO(UserPhoto photo) {
        if (photo == null) return null;
        Long userId = null;
        String userName = null;
        if (photo.getUser() != null) {
            try {
                userId = photo.getUser().getId();
                userName = photo.getUser().getFullName();
            } catch (Exception ignored) {}
        }
        return UserPhotoResponseDTO.builder()
                .id(photo.getId())
                .userId(userId)
                .userName(userName)
                .photoType(photo.getPhotoType())
                .photoUrl(photo.getPhotoUrl())
                .primaryPhoto(Boolean.TRUE.equals(photo.getPrimaryPhoto()))
                .createdAt(photo.getCreatedAt())
                .updatedAt(photo.getUpdatedAt())
                .build();
    }
}