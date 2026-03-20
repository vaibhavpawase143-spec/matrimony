package com.example.controller.master; // master folder

import com.example.model.Religion;
import com.example.service.ReligionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/religions")
public class ReligionController {

    @Autowired
    private ReligionService religionService;

    // Create new Religion
    @PostMapping
    public ResponseEntity<Religion> create(@RequestBody Religion religion) {
        return ResponseEntity.ok(religionService.create(religion));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Religion>> getAll() {
        return ResponseEntity.ok(religionService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Religion>> getAllActive() {
        return ResponseEntity.ok(religionService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Religion>> getAllInactive() {
        return ResponseEntity.ok(religionService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Religion> getById(@PathVariable Long id) {
        Optional<Religion> r = religionService.getById(id);
        return r.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Religion> update(@PathVariable Long id, @RequestBody Religion updated) {
        return ResponseEntity.ok(religionService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        religionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<Religion>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(religionService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Religion>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(religionService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Religion>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(religionService.getActiveByAdmin(adminId));
    }
}