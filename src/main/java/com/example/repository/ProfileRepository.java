package com.example.repository;

import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // 🔍 Get profile by user (VERY IMPORTANT - OneToOne)
    Optional<Profile> findByUserId(Long userId);

    // 🔍 Check if profile exists for user
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
}