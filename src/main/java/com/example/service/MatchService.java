package com.example.service;

import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;

import java.util.List;

public interface MatchService {
    PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size);
    List<MatchResponseDTO> getTopMatches(Long userId, int limit);
}