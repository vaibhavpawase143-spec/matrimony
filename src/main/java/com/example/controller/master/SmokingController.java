package com.example.controller.master; // master folder

import com.example.model.Smoking;
import com.example.service.SmokingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/smoking")
public class SmokingController {

    @Autowired
    private SmokingService smokingService;

    // Create new Smoking
    @PostMapping
    public ResponseEntity<Smoking> create(@RequestBody Smoking smoking) {
        return ResponseEntity.ok(smokingService.create(smoking));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Smoking>> getAll() {
        return ResponseEntity.ok(smokingService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Smoking>> getAllActive() {
        return ResponseEntity.ok(smokingService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Smoking>> getAllInactive() {
        return ResponseEntity.ok(smokingService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Smoking> getById(@PathVariable Long id) {
        Optional<Smoking> s = smokingService.getById(id);
        return s.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Smoking> update(@PathVariable Long id, @RequestBody Smoking updated) {
        return ResponseEntity.ok(smokingService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        smokingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<Smoking>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(smokingService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Smoking>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(smokingService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Smoking>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(smokingService.getActiveByAdmin(adminId));
    }
}