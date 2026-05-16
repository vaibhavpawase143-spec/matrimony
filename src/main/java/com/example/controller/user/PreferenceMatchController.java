package com.example.controller.user;

import com.example.dto.response.MatchResponseDTO;
import com.example.service.PreferenceMatchService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match/filter")
@RequiredArgsConstructor
public class PreferenceMatchController {

    private final PreferenceMatchService matchService;

    @GetMapping("/{userId}")
    public List<MatchResponseDTO> getMatches(@PathVariable Long userId) {
        return matchService.findMatches(userId);
    }
}