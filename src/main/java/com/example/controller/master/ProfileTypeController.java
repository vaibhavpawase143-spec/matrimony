package com.example.controller.master; // master folder

import com.example.model.ProfileType;
import com.example.service.ProfileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile-types")
public class ProfileTypeController {

    @Autowired
    private ProfileTypeService profileTypeService;

    // Create new ProfileType
    @PostMapping
    public ResponseEntity<ProfileType> create(@RequestBody ProfileType pt) {
        return ResponseEntity.ok(profileTypeService.create(pt));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ProfileType>> getAll() {
        return ResponseEntity.ok(profileTypeService.getAll());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfileType> getById(@PathVariable Long id) {
        Optional<ProfileType> pt = profileTypeService.getById(id);
        return pt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ProfileType> update(@PathVariable Long id, @RequestBody ProfileType updated) {
        return ResponseEntity.ok(profileTypeService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<ProfileType>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(profileTypeService.getByAdmin(adminId));
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<ProfileType>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(profileTypeService.search(keyword));
    }
}