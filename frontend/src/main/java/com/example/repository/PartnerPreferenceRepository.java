package com.example.repository;

import com.example.model.PartnerPreference;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {
    PartnerPreference findByUser(User user);

    // 🔍 Prevent duplicate preference for same user
    boolean existsByUserId(Long userId);

    // 🔍 Find by userId
    Optional<PartnerPreference> findByUserId(Long userId);

    // 🔍 Filter by religion
    List<PartnerPreference> findByReligionId(Long religionId);

    // 🔍 Filter by caste
    List<PartnerPreference> findByCasteId(Long casteId);

    // 🔍 Filter by city
    List<PartnerPreference> findByCityId(Long cityId);

    // 🔥 Advanced filtering (VERY USEFUL)
    List<PartnerPreference> findByReligionIdAndCasteId(Long religionId, Long casteId);

    List<PartnerPreference> findByReligionIdAndCityId(Long religionId, Long cityId);

    List<PartnerPreference> findByCasteIdAndCityId(Long casteId, Long cityId);
}