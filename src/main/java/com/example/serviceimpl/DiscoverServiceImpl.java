package com.example.serviceimpl;

import com.example.dto.response.DiscoverProfileDTO;
import com.example.model.Profile;
import com.example.repository.PartnerPreferenceRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.DiscoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiscoverServiceImpl implements DiscoverService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PartnerPreferenceRepository preferenceRepository;

    @Override
    public Page<DiscoverProfileDTO> discoverProfiles(
            Long userId,
            Pageable pageable
    ) {

        Profile myProfile = profileRepository
                .findByUserIdWithRelations(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (myProfile.getGender() == null) {
            throw new RuntimeException("Gender not found");
        }

        Long oppositeGenderId;

        if (myProfile.getGender().getId().equals(1L)) {
            // Female -> Show Male
            oppositeGenderId = 2L;
        } else if (myProfile.getGender().getId().equals(2L)) {
            // Male -> Show Female
            oppositeGenderId = 1L;
        } else {
            // Other -> Show Other (temporary)
            oppositeGenderId = 3L;
        }

        Page<Profile> profiles =
                profileRepository.findDiscoverProfilesByGender(
                        userId,
                        oppositeGenderId,
                        pageable
                );

        return profiles.map(this::mapToDTO);
    }
    private DiscoverProfileDTO mapToDTO(Profile profile) {

        DiscoverProfileDTO dto = new DiscoverProfileDTO();

        dto.setProfileId(profile.getId());
        dto.setUserId(profile.getUser().getId());

        dto.setFirstName(profile.getUser().getFirstName());
        dto.setLastName(profile.getUser().getLastName());

        dto.setImageUrl(profile.getImageUrl());

        if (profile.getDateOfBirth() != null) {

            dto.setAge(
                    LocalDate.now().getYear()
                            - profile.getDateOfBirth().getYear()
            );
        }

        if (profile.getCity() != null) {

            dto.setCityId(profile.getCity().getId());
            dto.setCityName(profile.getCity().getName());
        }

        if (profile.getReligion() != null) {

            dto.setReligionId(profile.getReligion().getId());
            dto.setReligionName(profile.getReligion().getName());
        }

        if (profile.getOccupation() != null) {

            dto.setOccupationId(profile.getOccupation().getId());
            dto.setOccupationName(profile.getOccupation().getName());
        }

        if (profile.getHeight() != null) {

            dto.setHeightId(profile.getHeight().getId());
            dto.setHeightValue(profile.getHeight().getHeight());
        }

        dto.setPremium(profile.getIsPremium());

        dto.setVerified(
                profile.getUser().getEmailVerified()
                        && profile.getUser().getPhoneVerified()
        );

        // Temporary until we implement score calculation
        dto.setMatchScore(0);
        dto.setMatchPercentage("0%");

        return dto;
    }

}