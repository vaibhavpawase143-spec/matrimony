package com.example.controller.user;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.SwipeType;
import com.example.service.MatchService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    // 🔍 PAGINATED MATCHES
    @GetMapping("/{userId}")
    public PageResponse<MatchResponseDTO> getMatches(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return matchService.getMatches(userId, page, size);
    }

    // 💖 TOP RECOMMENDATIONS (NEW 🔥)
    @GetMapping("/recommend/{userId}")
    public List<MatchResponseDTO> getTopMatches(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return matchService.getTopMatches(userId, limit);
    }

    @GetMapping("/{userId}/explanation/{profileId}")
    public MatchExplanationResponseDTO getMatchExplanation(
            @PathVariable Long userId,
            @PathVariable Long profileId
    ) {
        return matchService.getMatchExplanation(userId, profileId);
    }
    @PostMapping("/swipe")
    public ResponseEntity<String> swipe(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam SwipeType type
    ) {
        matchService.swipe(fromUserId, toUserId, type);
        return ResponseEntity.ok("Swiped successfully");
    }
}