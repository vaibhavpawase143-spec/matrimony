package com.example.controller.master; // master folder

import com.example.model.MotherTongue;
import com.example.service.MotherTongueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mother-tongues")
public class MotherTongueController {

    @Autowired
    private MotherTongueService motherTongueService;

    // Create new MotherTongue
    @PostMapping
    public ResponseEntity<MotherTongue> create(@RequestBody MotherTongue mt) {
        return ResponseEntity.ok(motherTongueService.create(mt));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<MotherTongue>> getAll() {
        return ResponseEntity.ok(motherTongueService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<MotherTongue>> getAllActive() {
        return ResponseEntity.ok(motherTongueService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<MotherTongue>> getAllInactive() {
        return ResponseEntity.ok(motherTongueService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<MotherTongue> getById(@PathVariable Long id) {
        Optional<MotherTongue> mt = motherTongueService.getById(id);
        return mt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<MotherTongue> update(@PathVariable Long id, @RequestBody MotherTongue updated) {
        return ResponseEntity.ok(motherTongueService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        motherTongueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<MotherTongue>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(motherTongueService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<MotherTongue>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(motherTongueService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<MotherTongue>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(motherTongueService.getActiveByAdmin(adminId));
    }
}