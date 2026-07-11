package com.example.serviceimpl;

import com.example.dto.response.MatchResponseDTO;
import com.example.model.UserDetails;
import com.example.repository.UserDetailsRepository;
import com.example.service.CompatibilityService;
import com.example.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserDetailsRepository repository;
    private final CompatibilityService compatibilityService;

    @Override
    public List<MatchResponseDTO> getRecommendations(Long userId) {

        UserDetails currentUser = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserDetails> allUsers = repository.findAll();

        return allUsers.stream()
                .filter(u -> !u.getUserId().equals(userId))
                .map(u -> {

                    double score = compatibilityService.calculateScore(currentUser, u);
                    int finalScore = (int) score;

                    return MatchResponseDTO.builder()
                            .userId(u.getUserId())
                            .name(u.getFullName())
                            .city(u.getCity())
                            .religion(u.getReligion())
                            .caste(u.getCaste())
                            .matchScore(finalScore)
                            .matchPercentage(finalScore + "% 💘")
                            .build();
                })
                .sorted(Comparator.comparingInt(MatchResponseDTO::getMatchScore).reversed())
                .collect(Collectors.toList());
    }
}