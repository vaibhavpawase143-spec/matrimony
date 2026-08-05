package com.example.seeder;

import com.example.model.PhotoType;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.repository.ProfileRepository;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PhotoSeeder {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final ProfileRepository profileRepository;

    private static final int TOTAL_IMAGES = 23;
    private static final int TOTAL_PHOTOS_PER_USER = 5;

    @Transactional
    public void seedPhotos() {

        List<User> users = userRepository.findByIsActiveTrue();

        int imageIndex = 1;

        for (User user : users) {

            // Skip if photos already exist
            if (userPhotoRepository.countByUserId(user.getId()) > 0) {
                continue;
            }

            String profilePhotoUrl = "/uploads/user" + imageIndex + ".jpg";

            // =========================
            // PROFILE PHOTO
            // =========================
            UserPhoto profilePhoto = UserPhoto.builder()
                    .user(user)
                    .photoType(PhotoType.PROFILE)
                    .photoUrl(profilePhotoUrl)
                    .primaryPhoto(true)
                    .build();

            userPhotoRepository.save(profilePhoto);

            // =========================
            // UPDATE PROFILE IMAGE
            // =========================
            Optional<Profile> optionalProfile =
                    profileRepository.findByUserId(user.getId());

            if (optionalProfile.isPresent()) {

                Profile profile = optionalProfile.get();
                profile.setImageUrl(profilePhotoUrl);

                profileRepository.save(profile);
            }

            imageIndex++;
            if (imageIndex > TOTAL_IMAGES) {
                imageIndex = 1;
            }

            // =========================
            // GALLERY PHOTOS
            // =========================
            for (int i = 1; i < TOTAL_PHOTOS_PER_USER; i++) {

                UserPhoto galleryPhoto = UserPhoto.builder()
                        .user(user)
                        .photoType(PhotoType.OTHER)
                        .photoUrl("/uploads/user" + imageIndex + ".jpg")
                        .primaryPhoto(false)
                        .build();

                userPhotoRepository.save(galleryPhoto);

                imageIndex++;

                if (imageIndex > TOTAL_IMAGES) {
                    imageIndex = 1;
                }
            }
        }

        System.out.println("Photo seeding completed successfully.");
    }
}