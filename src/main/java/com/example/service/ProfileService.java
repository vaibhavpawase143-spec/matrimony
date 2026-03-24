package com.example.service;

import com.example.model.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    // ✅ Create / Save profile
    Profile saveProfile(Profile profile);

    // ✅ Get by ID
    Optional<Profile> getById(Long id);

    // 🔍 Get by userId
    Optional<Profile> getByUserId(Long userId);

    // 🔍 Get all
    List<Profile> getAll();

    // 🔥 Active profiles
    List<Profile> getActiveProfiles();

    // 🔍 Filters
    List<Profile> getByReligion(Long religionId);

    List<Profile> getByCaste(Long casteId);

    List<Profile> getByCity(Long cityId);

    List<Profile> getByEducation(Long educationLevelId);

    List<Profile> getByOccupation(Long occupationId);

    // 🔥 Advanced filters
    List<Profile> getByReligionAndCaste(Long religionId, Long casteId);

    List<Profile> getByCityAndEducation(Long cityId, Long educationLevelId);

    List<Profile> getByOccupationAndCity(Long occupationId, Long cityId);

    List<Profile> getActiveByReligionAndCity(Long religionId, Long cityId);

    // ✅ Delete
    void delete(Long id);
}