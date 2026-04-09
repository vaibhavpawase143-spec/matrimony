package com.example.serviceimpl;

import com.example.dto.response.MatchResponseDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.PreferenceMatchService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceMatchServiceImpl implements PreferenceMatchService {

    private final ProfileMatchRepository profileRepository;
    private final UserRepository userRepository;
    private final PartnerPreferenceRepository preferenceRepository;

    @Override
    public List<MatchResponseDTO> findMatches(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartnerPreference pref = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found"));

        List<Profile> profiles = profileRepository.findMatches(
                userId,
                pref.getReligion() != null ? pref.getReligion().getId() : null,
                pref.getCaste() != null ? pref.getCaste().getId() : null,
                pref.getCity() != null ? pref.getCity().getId() : null
        );

        return profiles.stream()
                .filter(p -> matchAge(p, pref))
                .filter(p -> matchHeight(p, pref))
                .map(p -> mapToDTO(p, pref))
                .sorted((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    // ================= AGE =================
    private boolean matchAge(Profile p, PartnerPreference pref) {

        if (p.getDateOfBirth() == null) return false;

        int age = Period.between(p.getDateOfBirth(), LocalDate.now()).getYears();

        if (pref.getMinAge() != null && age < pref.getMinAge()) return false;
        if (pref.getMaxAge() != null && age > pref.getMaxAge()) return false;

        return true;
    }

    // ================= HEIGHT =================
    private boolean matchHeight(Profile p, PartnerPreference pref) {

        if (p.getHeight() == null || p.getHeight().getHeight() == null) {
            return true;
        }

        try {
            Double heightValue = Double.parseDouble(p.getHeight().getHeight());

            if (pref.getMinHeight() != null && heightValue < pref.getMinHeight()) return false;
            if (pref.getMaxHeight() != null && heightValue > pref.getMaxHeight()) return false;

        } catch (Exception e) {
            return true;
        }

        return true;
    }

    // ================= MATCH SCORE =================
    private int calculateScore(Profile p, PartnerPreference pref) {

        int score = 0;

        // Religion
        if (pref.getReligion() != null &&
                p.getReligion() != null &&
                pref.getReligion().getId().equals(p.getReligion().getId())) {
            score += 30;
        }

        // Caste
        if (pref.getCaste() != null &&
                p.getCaste() != null &&
                pref.getCaste().getId().equals(p.getCaste().getId())) {
            score += 20;
        }

        // City
        if (pref.getCity() != null &&
                p.getCity() != null &&
                pref.getCity().getId().equals(p.getCity().getId())) {
            score += 20;
        }

        // Age match bonus
        if (matchAge(p, pref)) {
            score += 15;
        }

        // Height match bonus
        if (matchHeight(p, pref)) {
            score += 15;
        }

        return score;
    }

    // ================= DTO =================
    private MatchResponseDTO mapToDTO(Profile profile, PartnerPreference pref) {

        int score = calculateScore(profile, pref);

        return MatchResponseDTO.builder()
                .userId(profile.getUser().getId())
                .name(profile.getUser().getEmail())

                .city(profile.getCity() != null ? profile.getCity().getName() : null)
                .religion(profile.getReligion() != null ? profile.getReligion().getName() : null)
                .caste(profile.getCaste() != null ? profile.getCaste().getName() : null)

                .matchScore(score)
                .matchPercentage(score + "%")
                .build();
    }
}