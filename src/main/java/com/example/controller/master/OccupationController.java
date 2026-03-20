package com.example.controller.master; // master folder

import com.example.model.Occupation;
import com.example.service.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/occupations")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;

    // Create new Occupation
    @PostMapping
    public ResponseEntity<Occupation> create(@RequestBody Occupation occupation) {
        return ResponseEntity.ok(occupationService.create(occupation));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Occupation>> getAll() {
        return ResponseEntity.ok(occupationService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Occupation>> getAllActive() {
        return ResponseEntity.ok(occupationService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Occupation>> getAllInactive() {
        return ResponseEntity.ok(occupationService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Occupation> getById(@PathVariable Long id) {
        Optional<Occupation> occ = occupationService.getById(id);
        return occ.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Occupation> update(@PathVariable Long id, @RequestBody Occupation updated) {
        return ResponseEntity.ok(occupationService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        occupationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<Occupation>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(occupationService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Occupation>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(occupationService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Occupation>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(occupationService.getActiveByAdmin(adminId));
    }
}