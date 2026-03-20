package com.example.repository;

import com.example.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // 🔍 Get profile by user (OneToOne relationship)
    Optional<Profile> findByUser_Id(Long userId);

    // 🔍 Check if profile exists for user
    boolean existsByUser_Id(Long userId);

    // 🔍 Filter by religion
    List<Profile> findByReligion_Id(Long religionId);

    // 🔍 Filter by caste
    List<Profile> findByCaste_Id(Long casteId);

    // 🔍 Filter by city
    List<Profile> findByCity_Id(Long cityId);

    // 🔍 Filter by education level
    List<Profile> findByEducationLevel_Id(Long educationLevelId);

    // 🔍 Filter by occupation
    List<Profile> findByOccupation_Id(Long occupationId);
}