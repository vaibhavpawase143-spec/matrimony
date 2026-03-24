package com.example.controller.user;

import com.example.model.Shortlist;
import com.example.model.User;
import com.example.model.Profile;
import com.example.repository.UserRepository;
import com.example.repository.ProfileRepository;
import com.example.service.ShortlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ✅ Add to shortlist
    @PostMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<Shortlist> addToShortlist(
            @PathVariable Long userId,
            @PathVariable Long profileId) {

        // 🔥 Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Fetch Profile
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // 🔥 Create Shortlist object
        Shortlist shortlist = new Shortlist();
        shortlist.setUser(user);
        shortlist.setProfile(profile);

        Shortlist saved = shortlistService.addToShortlist(shortlist);

        return ResponseEntity.ok(saved);
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
    public ResponseEntity<List<Shortlist>> getUserShortlist(
            @PathVariable Long userId) {

        return ResponseEntity.ok(shortlistService.getByUser(userId));
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

    // ✅ Get who shortlisted a profile (optional but useful)
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<Shortlist>> getByProfile(
            @PathVariable Long profileId) {

        return ResponseEntity.ok(shortlistService.getByProfile(profileId));
    }

    // ✅ Get all (admin/debug)
    @GetMapping
    public ResponseEntity<List<Shortlist>> getAll() {
        return ResponseEntity.ok(shortlistService.getAll());
    }
}