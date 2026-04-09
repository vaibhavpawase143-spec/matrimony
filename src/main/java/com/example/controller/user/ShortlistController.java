package com.example.controller.user;

import com.example.dto.response.ShortlistResponseDTO;
import com.example.model.Shortlist;
import com.example.model.User;
import com.example.model.Profile;
import com.example.repository.UserRepository;
import com.example.repository.ProfileRepository;
import com.example.service.ShortlistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shortlists")
public class ShortlistController {

    private final ShortlistService shortlistService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ShortlistController(ShortlistService shortlistService,
                               UserRepository userRepository,
                               ProfileRepository profileRepository) {
        this.shortlistService = shortlistService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    // 🔥 COMMON MAPPER METHOD
    private ShortlistResponseDTO mapToDTO(Shortlist s) {
        return ShortlistResponseDTO.builder()
                .id(s.getId())
                .userId(s.getUser().getId())
                .userName(s.getUser().getEmail()) // adjust if field name differs
                .profileId(s.getProfile().getId())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    // ✅ Add to shortlist
    @PostMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<ShortlistResponseDTO> addToShortlist(
            @PathVariable Long userId,
            @PathVariable Long profileId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Shortlist shortlist = new Shortlist();
        shortlist.setUser(user);
        shortlist.setProfile(profile);

        Shortlist saved = shortlistService.addToShortlist(shortlist);

        return ResponseEntity.ok(mapToDTO(saved));
    }

    // ✅ Remove from shortlist
    @DeleteMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<Void> removeFromShortlist(
            @PathVariable Long userId,
            @PathVariable Long profileId) {

        shortlistService.removeFromShortlist(userId, profileId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get user's shortlist
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShortlistResponseDTO>> getUserShortlist(
            @PathVariable Long userId) {

        List<ShortlistResponseDTO> list = shortlistService.getByUser(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // ✅ Check if shortlisted
    @GetMapping("/user/{userId}/profile/{profileId}/check")
    public ResponseEntity<Boolean> isShortlisted(
            @PathVariable Long userId,
            @PathVariable Long profileId) {

        boolean exists = shortlistService
                .getByUserAndProfile(userId, profileId)
                .isPresent();

        return ResponseEntity.ok(exists);
    }

    // ✅ Get who shortlisted a profile
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ShortlistResponseDTO>> getByProfile(
            @PathVariable Long profileId) {

        List<ShortlistResponseDTO> list = shortlistService.getByProfile(profileId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // ✅ Get all
    @GetMapping
    public ResponseEntity<List<ShortlistResponseDTO>> getAll() {

        List<ShortlistResponseDTO> list = shortlistService.getAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
}