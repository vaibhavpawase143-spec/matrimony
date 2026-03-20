package com.example.controller.user; // user folder

import com.example.model.PartnerPreference;
import com.example.service.PartnerPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partner-preferences")
public class PartnerPreferenceController {

    @Autowired
    private PartnerPreferenceService preferenceService;

    // Create new partner preference
    @PostMapping
    public ResponseEntity<PartnerPreference> create(@RequestBody PartnerPreference preference) {
        return ResponseEntity.ok(preferenceService.create(preference));
    }

    // Update partner preference
    @PutMapping("/{userId}")
    public ResponseEntity<PartnerPreference> update(@PathVariable Long userId, @RequestBody PartnerPreference updated) {
        return ResponseEntity.ok(preferenceService.update(userId, updated));
    }

    // Get by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<PartnerPreference> getByUserId(@PathVariable Long userId) {
        Optional<PartnerPreference> pp = preferenceService.getByUserId(userId);
        return pp.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Delete by userId
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        preferenceService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // Get preferences by religion
    @GetMapping("/religion/{religionId}")
    public ResponseEntity<List<PartnerPreference>> getByReligion(@PathVariable Long religionId) {
        return ResponseEntity.ok(preferenceService.getByReligion(religionId));
    }

    // Get preferences by caste
    @GetMapping("/caste/{casteId}")
    public ResponseEntity<List<PartnerPreference>> getByCaste(@PathVariable Long casteId) {
        return ResponseEntity.ok(preferenceService.getByCaste(casteId));
    }

    // Get preferences by city
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<PartnerPreference>> getByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(preferenceService.getByCity(cityId));
    }
}