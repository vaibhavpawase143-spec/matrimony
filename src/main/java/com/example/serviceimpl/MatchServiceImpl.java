package com.example.serviceimpl;

import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.MatchService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;
    private final PartnerPreferenceRepository prefRepo;

    @Override
    public PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size) {

        // ✅ Validate user
        User currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Get preference
        PartnerPreference pref = prefRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Partner preference not set"));

        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        // 🔥 DB FILTERING
        var spec = com.example.specification.ProfileSpecification.matchPreferences(pref);

        var profilePage = profileRepo.findAll(spec, pageable);

        List<MatchResponseDTO> content = profilePage.getContent().stream()
                .filter(p -> p.getUser() != null && !p.getUser().getId().equals(userId))
                .map(profile -> {

                    int score = calculateScore(profile, pref);

                    return MatchResponseDTO.builder()
                            .userId(profile.getUser().getId())
                            .name(profile.getUser().getFullName())
                            .city(profile.getCity() != null ? profile.getCity().getName() : null)
                            .religion(profile.getReligion() != null ? profile.getReligion().getName() : null)
                            .caste(profile.getCaste() != null ? profile.getCaste().getName() : null)
                            .matchScore(score)
                            .matchPercentage(score + "%")
                            .build();
                })
                .sorted(Comparator.comparingInt(MatchResponseDTO::getMatchScore).reversed())
                .toList();

        return new PageResponse<>(
                content,
                profilePage.getNumber(),
                profilePage.getSize(),
                profilePage.getTotalElements(),
                profilePage.getTotalPages(),
                profilePage.isLast()
        );
    }
    @Override
    public List<MatchResponseDTO> getTopMatches(Long userId, int limit) {

        User currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartnerPreference pref = prefRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Partner preference not set"));

        // 🎯 DOB calculation
        LocalDate today = LocalDate.now();
        LocalDate minDob = null;
        LocalDate maxDob = null;

        if (pref.getMinAge() != null && pref.getMaxAge() != null) {
            maxDob = today.minusYears(pref.getMinAge());
            minDob = today.minusYears(pref.getMaxAge());
        }

        // 🚀 DB CALL
        List<Object[]> results = profileRepo.findMatchesWithScore(
                userId,
                pref.getReligion() != null ? pref.getReligion().getId() : null,
                pref.getCaste() != null ? pref.getCaste().getId() : null,
                pref.getCity() != null ? pref.getCity().getId() : null,
                minDob,
                maxDob
        );

        return results.stream()
                .limit(limit)
                .map(obj -> {

                    Profile profile = (Profile) obj[0];
                    int score = ((Number) obj[1]).intValue();

                    return MatchResponseDTO.builder()
                            .userId(profile.getUser().getId())
                            .name(profile.getUser().getFullName())
                            .city(profile.getCity() != null ? profile.getCity().getName() : null)
                            .religion(profile.getReligion() != null ? profile.getReligion().getName() : null)
                            .caste(profile.getCaste() != null ? profile.getCaste().getName() : null)
                            .matchScore(score)
                            .matchPercentage(score + "%")
                            .build();
                })
                .toList();
    }

    // 🔥 MATCH LOGIC
    private int calculateScore(Profile profile, PartnerPreference pref) {

        double totalScore = 0;

        totalScore += religionScore(profile, pref) * 20;
        totalScore += casteScore(profile, pref) * 15;
        totalScore += cityScore(profile, pref) * 15;
        totalScore += ageScore(profile, pref) * 20;
        totalScore += heightScore(profile, pref) * 10;
        totalScore += educationScore(profile, pref) * 10;

        totalScore += bonusScore(profile) * 10;

        return (int) totalScore;
    }
    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }
    private double religionScore(Profile p, PartnerPreference pref) {
        if (pref.getReligion() == null) return 0.5;
        if (p.getReligion() == null) return 0;

        return pref.getReligion().getId().equals(p.getReligion().getId()) ? 1 : 0;
    }
    private double casteScore(Profile p, PartnerPreference pref) {
        if (pref.getCaste() == null) return 0.5;
        if (p.getCaste() == null) return 0;

        return pref.getCaste().getId().equals(p.getCaste().getId()) ? 1 : 0;
    }
    private double cityScore(Profile p, PartnerPreference pref) {
        if (pref.getCity() == null) return 0.5;
        if (p.getCity() == null) return 0;

        return pref.getCity().getId().equals(p.getCity().getId()) ? 1 : 0;
    }
    private double ageScore(Profile p, PartnerPreference pref) {

        if (p.getDateOfBirth() == null ||
                pref.getMinAge() == null ||
                pref.getMaxAge() == null) return 0;

        int age = calculateAge(p.getDateOfBirth());

        if (age >= pref.getMinAge() && age <= pref.getMaxAge()) {
            return 1;
        }

        int mid = (pref.getMinAge() + pref.getMaxAge()) / 2;
        int diff = Math.abs(age - mid);

        if (diff <= 2) return 0.8;
        if (diff <= 5) return 0.5;

        return 0;
    }
    private double heightScore(Profile p, PartnerPreference pref) {

        if (p.getHeight() == null ||
                pref.getMinHeight() == null ||
                pref.getMaxHeight() == null) return 0;

        try {
            double height = Double.parseDouble(p.getHeight().getHeight());

            if (height >= pref.getMinHeight() && height <= pref.getMaxHeight()) {
                return 1;
            }

            return 0.3;

        } catch (Exception e) {
            return 0; // invalid format safety
        }
    }
    private double educationScore(Profile p, PartnerPreference pref) {

        if (p.getEducationLevel() == null) return 0;

        return 0.5;
    }
    private double bonusScore(Profile p) {

        int score = 0;

        if (p.getAbout() != null) score++;
        if (p.getImageUrl() != null) score++;
        if (p.getEducationLevel() != null) score++;
        if (p.getCity() != null) score++;

        return score / 4.0;
    }
}