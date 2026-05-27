package com.example.service;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.PartnerPreference;
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

    List<Profile> getAll();

    List<Profile> getActiveProfiles();

    ProfileResponseDTO getMyProfile();

    ProfileResponseDTO getProfileById(
            Long id
    );

    // =====================================================
    // FILTERS
    // =====================================================

    List<Profile> getByReligion(
            Long religionId
    );

    List<Profile> getByCaste(
            Long casteId
    );

    List<Profile> getByCity(
            Long cityId
    );

    List<Profile> getByEducation(
            Long educationLevelId
    );

    List<Profile> getByOccupation(
            Long occupationId
    );

    // =====================================================
    // ADVANCED FILTERS
    // =====================================================

    List<Profile> getByReligionAndCaste(
            Long religionId,
            Long casteId
    );

    List<Profile> getByCityAndEducation(
            Long cityId,
            Long educationLevelId
    );

    List<Profile> getByOccupationAndCity(
            Long occupationId,
            Long cityId
    );

    List<Profile> getActiveByReligionAndCity(
            Long religionId,
            Long cityId
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
}