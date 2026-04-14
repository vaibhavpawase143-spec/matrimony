package com.example.controller.user;

import com.example.dto.response.MatchResponseDTO;
import com.example.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping("/recommendations")
    public List<MatchResponseDTO> getRecommendations(@RequestParam Long userId) {
        return service.getRecommendations(userId);
    }
}