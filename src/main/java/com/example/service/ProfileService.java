package com.example.service;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.PartnerPreference;
import com.example.model.PremiumPlan;
import com.example.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    // =====================================================
    // CREATE / UPDATE
    // =====================================================

    Profile saveProfile(Profile profile);

    ProfileResponseDTO createProfile(
            ProfileRequestDTO dto
    );

    ProfileResponseDTO updateMyProfile(
            UpdateProfileRequestDTO dto
    );

    // =====================================================
    // READ
    // =====================================================

    Optional<Profile> getById(Long id);

    Optional<Profile> getByUserId(Long userId);

    Page<Profile> getAll(Pageable pageable);

    List<Profile> getActiveProfiles();

    ProfileResponseDTO getMyProfile();

    ProfileResponseDTO getProfileById(
            Long id
    );

    // =====================================================
    // FILTERS
    // =====================================================

    Page<Profile> getByReligion(Long religionId, Pageable pageable);

    Page<Profile> getByCaste(Long casteId, Pageable pageable);

    Page<Profile> getByCity(Long cityId, Pageable pageable);

    Page<Profile> getByEducation(Long educationLevelId, Pageable pageable);

    Page<Profile> getByOccupation(Long occupationId, Pageable pageable);

    // =====================================================
    // ADVANCED FILTERS
    // =====================================================

    Page<Profile> getByReligionAndCaste(
            Long religionId,
            Long casteId,
            Pageable pageable
    );

    Page<Profile> getByCityAndEducation(
            Long cityId,
            Long educationLevelId,
            Pageable pageable
    );

    Page<Profile> getByOccupationAndCity(
            Long occupationId,
            Long cityId,
            Pageable pageable
    );
    List<ProfileResponseDTO> getDiscoverProfiles();

    Page<Profile> getActiveByReligionAndCity(
            Long religionId,
            Long cityId,
            Pageable pageable
    );



    // =====================================================
    // SEARCH
    // =====================================================

    Page<ProfileResponseDTO> searchProfiles(
            PartnerPreference pref,
            Pageable pageable
    );

    // =====================================================
    // DTO MAPPING
    // =====================================================

    ProfileResponseDTO mapToDTO(
            Profile profile
    );

    // =====================================================
    // DELETE
    // =====================================================

    void delete(Long id);


    // =====================================================
    // PREMIUM
    // =====================================================

    void activatePremium(
            Long userId,
            PremiumPlan plan
    );
}