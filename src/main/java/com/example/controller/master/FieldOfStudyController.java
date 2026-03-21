package com.example.controller.master; // master folder

import com.example.model.FieldOfStudy;
import com.example.service.FieldOfStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/field-of-study")
public class FieldOfStudyController {

    @Autowired
    private FieldOfStudyService fieldOfStudyService;

    // Create new FieldOfStudy
    @PostMapping
    public ResponseEntity<FieldOfStudy> save(@RequestBody FieldOfStudy fieldOfStudy) {
        return ResponseEntity.ok(fieldOfStudyService.save(fieldOfStudy));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FieldOfStudy> getById(@PathVariable Long id) {
        Optional<FieldOfStudy> fs = fieldOfStudyService.getById(id);
        return fs.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<FieldOfStudy> getByName(@PathVariable String name) {
        Optional<FieldOfStudy> fs = fieldOfStudyService.getByName(name);
        return fs.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<FieldOfStudy>> getAll() {
        return ResponseEntity.ok(fieldOfStudyService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<FieldOfStudy>> getAllActive() {
        return ResponseEntity.ok(fieldOfStudyService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<FieldOfStudy>> getAllInactive() {
        return ResponseEntity.ok(fieldOfStudyService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<FieldOfStudy>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(fieldOfStudyService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<FieldOfStudy>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(fieldOfStudyService.getActiveByAdmin(adminId));
    }

    // Search by name keyword
    @GetMapping("/search")
    public ResponseEntity<List<FieldOfStudy>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(fieldOfStudyService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<FieldOfStudy> update(@PathVariable Long id, @RequestBody FieldOfStudy updated) {
        return ResponseEntity.ok(fieldOfStudyService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fieldOfStudyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}