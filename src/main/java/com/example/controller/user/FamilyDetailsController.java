package com.example.controller.user; // User-specific data

import com.example.model.FamilyDetails;
import com.example.service.FamilyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/family-details")
public class FamilyDetailsController {

    @Autowired
    private FamilyDetailsService familyDetailsService;

    // Save family details
    @PostMapping
    public ResponseEntity<FamilyDetails> save(@RequestBody FamilyDetails familyDetails) {
        return ResponseEntity.ok(familyDetailsService.create(familyDetails));
    }

    // Get family details by profile ID
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<FamilyDetails> getByProfileId(@PathVariable Long profileId) {
        Optional<FamilyDetails> details = familyDetailsService.getByProfile(profileId);
        return details.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Check if family details exist for a profile
    @GetMapping("/profile/{profileId}/exists")
    public ResponseEntity<Boolean> existsByProfileId(@PathVariable Long profileId) {
        return ResponseEntity.ok(familyDetailsService.existsByProfile(profileId));
    }

    // Get family details by family type
    @GetMapping("/family-type/{familyTypeId}")
    public ResponseEntity<List<FamilyDetails>> getByFamilyType(@PathVariable Long familyTypeId) {
        return ResponseEntity.ok(familyDetailsService.getByFamilyType(familyTypeId));
    }

    // Get family details by family status
    @GetMapping("/family-status/{familyStatusId}")
    public ResponseEntity<List<FamilyDetails>> getByFamilyStatus(@PathVariable Long familyStatusId) {
        return ResponseEntity.ok(familyDetailsService.getActiveByFamily(familyStatusId));
    }

    // Get family details by brother type
    @GetMapping("/brother-type/{brotherTypeId}")
    public ResponseEntity<List<FamilyDetails>> getByBrotherType(@PathVariable Long brotherTypeId) {
        return ResponseEntity.ok(familyDetailsService.getByBrotherType(brotherTypeId));
    }

    // Get family details by sister type
    @GetMapping("/sister-type/{sisterTypeId}")
    public ResponseEntity<List<FamilyDetails>> getBySisterType(@PathVariable Long sisterTypeId) {
        return ResponseEntity.ok(familyDetailsService.getBySisterType(sisterTypeId));
    }

    // Update family details
    @PutMapping("/profile/{profileId}")
    public ResponseEntity<FamilyDetails> update(@PathVariable Long profileId, @RequestBody FamilyDetails updated) {
        return ResponseEntity.ok(familyDetailsService.update(profileId, updated));
    }

    // Delete family details by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        familyDetailsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}