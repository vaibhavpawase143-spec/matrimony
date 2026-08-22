package com.example.controller.user;

import com.example.dto.response.ShortlistResponseDTO;
import com.example.dto.request.ShortlistRequestDTO;
import com.example.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import jakarta.validation.Valid;
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

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AccessDeniedException("User not found: " + email));
    }

    private void validateUserOwnership(Long targetUserId, String actionDescription) {
        User currentUser = getAuthenticatedUser();
        boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));
        if (!isAdmin && (currentUser.getId() == null || !currentUser.getId().equals(targetUserId))) {
            throw new AccessDeniedException("Access denied: " + actionDescription);
        }
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

        validateUserOwnership(userId, "You cannot shortlist profiles on behalf of another user");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (profile.getUser() != null && profile.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot shortlist yourself");
        }

        Shortlist shortlist = new Shortlist();
        shortlist.setUser(user);
        shortlist.setProfile(profile);

        Shortlist saved = shortlistService.addToShortlist(shortlist);

        return ResponseEntity.ok(mapToDTO(saved));
    }

    // ==========================
    // New APIs that use authenticated user (JWT)
    // POST /api/shortlists  { profileId OR userId }
    @PostMapping
    public ResponseEntity<ShortlistResponseDTO> addToShortlistAuth(@Valid @RequestBody ShortlistRequestDTO dto) {

        String email = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Determine profile to shortlist
        Profile profile = null;
        if (dto.getProfileId() != null) {
            profile = profileRepository.findById(dto.getProfileId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));
        } else if (dto.getUserId() != null) {
            profile = profileRepository.findByUserId(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Profile not found for user"));
        } else {
            throw new IllegalArgumentException("profileId or userId is required");
        }

        // Rule: cannot shortlist self
        if (profile.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You cannot shortlist yourself");
        }

        Shortlist s = new Shortlist();
        s.setUser(user);
        s.setProfile(profile);

        Shortlist saved = shortlistService.addToShortlist(s);

        return ResponseEntity.ok(mapToDTO(saved));
    }

    // DELETE /api/shortlists/{shortlistedUserId}
    // DELETE /api/shortlists/{profileId}

    @DeleteMapping("/{profileId}")

    public ResponseEntity<Void>
    removeFromShortlistAuth(

            @PathVariable Long profileId

    ){

        String email =
                SecurityUtils.getCurrentUsername();

        User user =
                userRepository
                        .findByEmailWithRoles(email)
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "User not found"
                                ));

        shortlistService.removeFromShortlist(

                user.getId(),

                profileId

        );

        return ResponseEntity
                .noContent()
                .build();

    }
    // GET /api/shortlists/me?page=0&size=20
    @GetMapping("/me")
    public ResponseEntity<?> getMyShortlist(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        String email = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
        Page<Shortlist> p = shortlistService.getByUser(user.getId(), pageable);

        var content = p.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(
                java.util.Map.of(
                        "content", content,
                        "page", p.getNumber(),
                        "size", p.getSize(),
                        "totalElements", p.getTotalElements(),
                        "totalPages", p.getTotalPages()
                )
        );
    }

    // GET /api/shortlists/check/{shortlistedUserId}
    @GetMapping("/check/{shortlistedUserId}")
    public ResponseEntity<Boolean> checkIsShortlisted(@PathVariable Long shortlistedUserId) {
        String email = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(shortlistedUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user"));

        boolean exists = shortlistService.getByUserAndProfile(user.getId(), profile.getId()).isPresent();
        return ResponseEntity.ok(exists);
    }

    // ✅ Remove from shortlist
    @DeleteMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<Void> removeFromShortlist(
            @PathVariable Long userId,
            @PathVariable Long profileId) {

        validateUserOwnership(userId, "You cannot remove shortlist items for another user");
        shortlistService.removeFromShortlist(userId, profileId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get user's shortlist
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShortlistResponseDTO>> getUserShortlist(
            @PathVariable Long userId) {

        validateUserOwnership(userId, "You cannot view another user's shortlist");
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

        validateUserOwnership(userId, "You cannot check shortlist status for another user");
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