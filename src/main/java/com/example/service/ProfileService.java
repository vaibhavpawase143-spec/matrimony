package com.example.service;

import com.example.model.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    Profile create(Profile profile);

    Profile update(Long userId, Profile profile);

    Optional<Profile> getByUserId(Long userId);

    void delete(Long userId);

    List<Profile> getAll();

    List<Profile> getByReligion(Long religionId);

    List<Profile> getByCaste(Long casteId);

    List<Profile> getByCity(Long cityId);

    List<Profile> getByEducation(Long educationLevelId);

    List<Profile> getByOccupation(Long occupationId);
}