package com.example.service;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.SwipeType;
import com.example.model.User;

import java.util.List;

public interface MatchService {

    PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size);

    List<MatchResponseDTO> getTopMatches(Long userId, int limit);

    MatchExplanationResponseDTO getMatchExplanation(Long userId, Long profileId);

    // 🔥 NEW
    void swipe(Long fromUserId, Long toUserId, SwipeType type);
    List<User> getMyMatches(Long userId);
}