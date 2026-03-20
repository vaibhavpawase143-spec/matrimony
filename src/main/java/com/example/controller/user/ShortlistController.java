package com.example.controller.user; // user folder

import com.example.model.Shortlist;
import com.example.service.ShortlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shortlists")
public class ShortlistController {

    @Autowired
    private ShortlistService shortlistService;

    // Add to shortlist
    @PostMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<Shortlist> addToShortlist(@PathVariable Long userId, @PathVariable Long profileId) {
        return ResponseEntity.ok(shortlistService.addToShortlist(userId, profileId));
    }

    // Remove from shortlist
    @DeleteMapping("/user/{userId}/profile/{profileId}")
    public ResponseEntity<Void> removeFromShortlist(@PathVariable Long userId, @PathVariable Long profileId) {
        shortlistService.removeFromShortlist(userId, profileId);
        return ResponseEntity.noContent().build();
    }

    // Get user's shortlist
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Shortlist>> getUserShortlist(@PathVariable Long userId) {
        return ResponseEntity.ok(shortlistService.getUserShortlist(userId));
    }

    // Check if a profile is shortlisted
    @GetMapping("/user/{userId}/profile/{profileId}/check")
    public ResponseEntity<Boolean> isShortlisted(@PathVariable Long userId, @PathVariable Long profileId) {
        return ResponseEntity.ok(shortlistService.isShortlisted(userId, profileId));
    }
}