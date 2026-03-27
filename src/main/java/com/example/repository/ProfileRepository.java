package com.example.repository;

import com.example.model.Profile;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);

    // 🔍 Get profile by user (OneToOne)
    Optional<Profile> findByUserId(Long userId);

    // 🔍 Check if profile exists
    boolean existsByUserId(Long userId);

    // 🔍 Filter by religion
    List<Profile> findByReligionId(Long religionId);

    // 🔍 Filter by caste
    List<Profile> findByCasteId(Long casteId);

    // 🔍 Filter by city
    List<Profile> findByCityId(Long cityId);

    // 🔍 Filter by education
    List<Profile> findByEducationLevelId(Long educationLevelId);

    // 🔍 Filter by occupation
    List<Profile> findByOccupationId(Long occupationId);

    // 🔥 Active profiles
    List<Profile> findByIsActiveTrue();

    // 🔥 Advanced filters (REAL USE CASES)

    List<Profile> findByReligionIdAndCasteId(Long religionId, Long casteId);

    List<Profile> findByCityIdAndEducationLevelId(Long cityId, Long educationLevelId);

    List<Profile> findByOccupationIdAndCityId(Long occupationId, Long cityId);

    List<Profile> findByReligionIdAndCityIdAndIsActiveTrue(Long religionId, Long cityId);
}