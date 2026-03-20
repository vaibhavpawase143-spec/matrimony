package com.example.controller.master; // master folder

import com.example.model.Qualification;
import com.example.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/qualifications")
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;

    // Create new Qualification
    @PostMapping
    public ResponseEntity<Qualification> create(@RequestBody Qualification qualification) {
        return ResponseEntity.ok(qualificationService.create(qualification));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Qualification>> getAll() {
        return ResponseEntity.ok(qualificationService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Qualification>> getAllActive() {
        return ResponseEntity.ok(qualificationService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Qualification>> getAllInactive() {
        return ResponseEntity.ok(qualificationService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Qualification> getById(@PathVariable Long id) {
        Optional<Qualification> q = qualificationService.getById(id);
        return q.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Qualification> update(@PathVariable Long id, @RequestBody Qualification updated) {
        return ResponseEntity.ok(qualificationService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        qualificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<Qualification>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(qualificationService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Qualification>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(qualificationService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Qualification>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(qualificationService.getActiveByAdmin(adminId));
    }
}