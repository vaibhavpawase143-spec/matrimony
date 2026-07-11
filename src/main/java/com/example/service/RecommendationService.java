package com.example.service;

import com.example.dto.response.MatchResponseDTO;
import java.util.List;

public interface RecommendationService {
    List<MatchResponseDTO> getRecommendations(Long userId);
}