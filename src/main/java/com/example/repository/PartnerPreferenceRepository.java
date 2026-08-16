package com.example.repository;

import com.example.model.PartnerPreference;
import com.example.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {
    PartnerPreference findByUser(User user);

    boolean existsByUserId(Long userId);

    @EntityGraph(attributePaths = {
            "religion", "caste", "city", "educationLevel",
            "occupation", "maritalStatus", "smoking", "drinking", "diet"
    })
    Optional<PartnerPreference> findByUserId(Long userId);

    List<PartnerPreference> findByReligionId(Long religionId);

    List<PartnerPreference> findByCasteId(Long casteId);

    List<PartnerPreference> findByCityId(Long cityId);

    List<PartnerPreference> findByReligionIdAndCasteId(Long religionId, Long casteId);

    List<PartnerPreference> findByReligionIdAndCityId(Long religionId, Long cityId);

    List<PartnerPreference> findByCasteIdAndCityId(Long casteId, Long cityId);
}