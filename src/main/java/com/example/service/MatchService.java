package com.example.service;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.SwipeType;
import com.example.model.User;
import com.example.dto.response.MatchDetailsResponseDTO;

import java.util.List;

public interface MatchService {

    PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size);

    List<MatchResponseDTO> getTopMatches(
            Long userId,
            int page,
            int size
    );
    long countRecommendedMatches(
            Long userId,
            int minimumScore
    );

    MatchExplanationResponseDTO getMatchExplanation(Long userId, Long profileId);

    int calculateMatchScore(Long currentUserId, Long candidateUserId);

    int calculateMatchScore(User currentUser, User candidateUser);

    // 🔥 NEW
    void swipe(Long fromUserId, Long toUserId, SwipeType type);
    List<User> getMyMatches(Long userId);

    MatchDetailsResponseDTO getMatchDetails(Long userId, Long partnerId);
}