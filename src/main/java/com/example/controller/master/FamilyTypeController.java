package com.example.controller.master; // master folder

import com.example.model.FamilyType;
import com.example.service.FamilyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/family-type")
public class FamilyTypeController {

    @Autowired
    private FamilyTypeService familyTypeService;

    // Create new FamilyType
    @PostMapping
    public ResponseEntity<FamilyType> save(@RequestBody FamilyType familyType) {
        return ResponseEntity.ok(familyTypeService.save(familyType));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyType> getById(@PathVariable Long id) {
        Optional<FamilyType> ft = familyTypeService.getById(id);
        return ft.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<FamilyType> getByName(@PathVariable String name) {
        Optional<FamilyType> ft = familyTypeService.getByName(name);
        return ft.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<FamilyType>> getAll() {
        return ResponseEntity.ok(familyTypeService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<FamilyType>> getAllActive() {
        return ResponseEntity.ok(familyTypeService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<FamilyType>> getAllInactive() {
        return ResponseEntity.ok(familyTypeService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<FamilyType>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyTypeService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<FamilyType>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(familyTypeService.getActiveByAdmin(adminId));
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<List<FamilyType>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(familyTypeService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<FamilyType> update(@PathVariable Long id, @RequestBody FamilyType updated) {
        return ResponseEntity.ok(familyTypeService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        familyTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}