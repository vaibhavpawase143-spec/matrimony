package com.example.controller.master; // master folder

import com.example.model.FamilyStatus;
import com.example.service.FamilyStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/family-status")
public class FamilyStatusController {

    @Autowired
    private FamilyStatusService familyStatusService;

    // Create a new FamilyStatus
    @PostMapping
    public ResponseEntity<FamilyStatus> save(@RequestBody FamilyStatus familyStatus) {
        return ResponseEntity.ok(familyStatusService.save(familyStatus));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyStatus> getById(@PathVariable Long id) {
        Optional<FamilyStatus> fs = familyStatusService.getById(id);
        return fs.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<FamilyStatus> getByName(@PathVariable String name) {
        Optional<FamilyStatus> fs = familyStatusService.getByName(name);
        return fs.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<FamilyStatus>> getAll() {
        return ResponseEntity.ok(familyStatusService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<FamilyStatus>> getAllActive() {
        return ResponseEntity.ok(familyStatusService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<FamilyStatus>> getAllInactive() {
        return ResponseEntity.ok(familyStatusService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<FamilyStatus>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyStatusService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<FamilyStatus>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyStatusService.getActiveByAdmin(adminId));
    }

    // Search by name keyword
    @GetMapping("/search")
    public ResponseEntity<List<FamilyStatus>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(familyStatusService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<FamilyStatus> update(@PathVariable Long id, @RequestBody FamilyStatus updated) {
        return ResponseEntity.ok(familyStatusService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        familyStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}