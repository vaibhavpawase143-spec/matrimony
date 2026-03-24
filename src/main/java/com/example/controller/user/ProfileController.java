package com.example.controller.user; // user folder

import com.example.model.Profile;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Create profile
    @PostMapping
    public ResponseEntity<Profile> create(@RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.saveProfile(profile));
    }

    // Update profile
    @PutMapping("/{userId}")
    public ResponseEntity<Profile> update(@PathVariable Long userId, @RequestBody Profile updated) {
        return ResponseEntity.ok(profileService.saveProfile(updated));
    }

    // Get by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<Profile> getByUserId(@PathVariable Long userId) {
        Optional<Profile> profile = profileService.getByUserId(userId);
        return profile.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Delete by userId
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        profileService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // Get all profiles
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(profileService.getAll());
    }

    // Filter by religion
    @GetMapping("/religion/{religionId}")
    public ResponseEntity<List<Profile>> getByReligion(@PathVariable Long religionId) {
        return ResponseEntity.ok(profileService.getByReligion(religionId));
    }

    // Filter by caste
    @GetMapping("/caste/{casteId}")
    public ResponseEntity<List<Profile>> getByCaste(@PathVariable Long casteId) {
        return ResponseEntity.ok(profileService.getByCaste(casteId));
    }

    // Filter by city
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<Profile>> getByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(profileService.getByCity(cityId));
    }

    // Filter by education
    @GetMapping("/education/{educationLevelId}")
    public ResponseEntity<List<Profile>> getByEducation(@PathVariable Long educationLevelId) {
        return ResponseEntity.ok(profileService.getByEducation(educationLevelId));
    }

    // Filter by occupation
    @GetMapping("/occupation/{occupationId}")
    public ResponseEntity<List<Profile>> getByOccupation(@PathVariable Long occupationId) {
        return ResponseEntity.ok(profileService.getByOccupation(occupationId));
    }
}