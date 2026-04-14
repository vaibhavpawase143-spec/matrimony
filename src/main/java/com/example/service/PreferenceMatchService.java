package com.example.service;

import com.example.dto.response.MatchResponseDTO;
import java.util.List;

public interface PreferenceMatchService {

    List<MatchResponseDTO> findMatches(Long userId);
}