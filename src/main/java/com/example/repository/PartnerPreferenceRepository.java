package com.example.repository;

import com.example.model.PartnerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {

    // 🔍 Prevent duplicate preference for same user
    boolean existsByUser_Id(Long userId);

    // 🔍 Find by userId
    Optional<PartnerPreference> findByUser_Id(Long userId);

    // 🔍 Filter by religion
    List<PartnerPreference> findByReligion_Id(Long religionId);

    // 🔍 Filter by caste
    List<PartnerPreference> findByCaste_Id(Long casteId);

    // 🔍 Filter by city
    List<PartnerPreference> findByCity_Id(Long cityId);
}