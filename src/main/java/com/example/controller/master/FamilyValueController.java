package com.example.controller.master; // master folder

import com.example.model.FamilyValue;
import com.example.service.FamilyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/family-value")
public class FamilyValueController {

    @Autowired
    private FamilyValueService familyValueService;

    // Create new FamilyValue
    @PostMapping
    public ResponseEntity<FamilyValue> save(@RequestBody FamilyValue familyValue) {
        return ResponseEntity.ok(familyValueService.save(familyValue));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyValue> getById(@PathVariable Long id) {
        Optional<FamilyValue> fv = familyValueService.getById(id);
        return fv.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<FamilyValue> getByName(@PathVariable String name) {
        Optional<FamilyValue> fv = familyValueService.getByName(name);
        return fv.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<FamilyValue>> getAll() {
        return ResponseEntity.ok(familyValueService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<FamilyValue>> getAllActive() {
        return ResponseEntity.ok(familyValueService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<FamilyValue>> getAllInactive() {
        return ResponseEntity.ok(familyValueService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<FamilyValue>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyValueService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<FamilyValue>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyValueService.getActiveByAdmin(adminId));
    }

    // Search by name keyword
    @GetMapping("/search")
    public ResponseEntity<List<FamilyValue>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(familyValueService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<FamilyValue> update(@PathVariable Long id, @RequestBody FamilyValue updated) {
        return ResponseEntity.ok(familyValueService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        familyValueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}