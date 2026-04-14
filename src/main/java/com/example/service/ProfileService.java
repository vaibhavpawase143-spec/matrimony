package com.example.service;

import com.example.model.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    // ================= CREATE / UPDATE =================
    Profile saveProfile(Profile profile);

    // ================= READ =================
    Optional<Profile> getById(Long id);

    Optional<Profile> getByUserId(Long userId);

    List<Profile> getAll();

    // 🔥 FIXED (will use JOIN FETCH method)
    List<Profile> getActiveProfiles();

    // ================= FILTERS =================
    List<Profile> getByReligion(Long religionId);

    List<Profile> getByCaste(Long casteId);

    List<Profile> getByCity(Long cityId);

    List<Profile> getByEducation(Long educationLevelId);

    List<Profile> getByOccupation(Long occupationId);

    // ================= ADVANCED FILTERS =================
    List<Profile> getByReligionAndCaste(Long religionId, Long casteId);

    List<Profile> getByCityAndEducation(Long cityId, Long educationLevelId);

    List<Profile> getByOccupationAndCity(Long occupationId, Long cityId);

    List<Profile> getActiveByReligionAndCity(Long religionId, Long cityId);

    // ================= DELETE =================
    void delete(Long id);
}