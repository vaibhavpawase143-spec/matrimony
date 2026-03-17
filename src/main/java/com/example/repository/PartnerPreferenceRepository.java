package com.example.repository;

import com.example.model.PartnerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {

    // 🔍 Get preference by user (VERY IMPORTANT - OneToOne)
    Optional<PartnerPreference> findByUserId(Long userId);

    // 🔍 Check if preference already exists
    boolean existsByUserId(Long userId);

    // 🔍 Filter by religion
    List<PartnerPreference> findByReligionId(Long religionId);

    // 🔍 Filter by caste
    List<PartnerPreference> findByCasteId(Long casteId);

    // 🔍 Filter by city
    List<PartnerPreference> findByCityId(Long cityId);
}