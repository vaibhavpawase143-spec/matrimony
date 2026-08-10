package com.example.controller.user;

import com.example.dto.response.MatchDetailsResponseDTO;
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

    // =========================================================
    // PAGINATED MATCHES
    // =========================================================

    @GetMapping("/{userId}")
    public PageResponse<MatchResponseDTO> getMatches(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return matchService.getMatches(userId, page, size);
    }


    // =========================================================
    // PAGINATED TOP RECOMMENDATIONS
    // =========================================================
    @GetMapping("/recommend/{userId}")
    public List<MatchResponseDTO> getTopMatches(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        // Production safety
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page cannot be negative"
            );
        }

        // Maximum 20 profiles per request
        if (size < 1 || size > 20) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 20"
            );
        }

        return matchService.getTopMatches(
                userId,
                page,
                size
        );
    }

    // =========================================================
    // MATCH EXPLANATION
    // =========================================================

    @GetMapping("/{userId}/explanation/{profileId}")
    public MatchExplanationResponseDTO getMatchExplanation(
            @PathVariable Long userId,
            @PathVariable Long profileId
    ) {
        return matchService.getMatchExplanation(userId, profileId);
    }


    // =========================================================
    // SWIPE
    // =========================================================

    @PostMapping("/swipe")
    public ResponseEntity<String> swipe(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam SwipeType type
    ) {
        matchService.swipe(fromUserId, toUserId, type);

        return ResponseEntity.ok("Swiped successfully");
    }


    // =========================================================
    // MATCH DETAILS
    // =========================================================

    @GetMapping("/{userId}/details/{partnerId}")
    public ResponseEntity<MatchDetailsResponseDTO> getMatchDetails(
            @PathVariable Long userId,
            @PathVariable Long partnerId
    ) {

        return ResponseEntity.ok(
                matchService.getMatchDetails(userId, partnerId)
        );
    }
}