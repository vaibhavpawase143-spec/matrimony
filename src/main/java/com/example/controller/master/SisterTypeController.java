package com.example.controller.master; // master folder

import com.example.model.SisterType;
import com.example.service.SisterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sister-types")
public class SisterTypeController {

    @Autowired
    private SisterTypeService sisterTypeService;

    // Create new SisterType
    @PostMapping
    public ResponseEntity<SisterType> create(@RequestBody SisterType st) {
        return ResponseEntity.ok(sisterTypeService.create(st));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<SisterType>> getAll() {
        return ResponseEntity.ok(sisterTypeService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<SisterType>> getAllActive() {
        return ResponseEntity.ok(sisterTypeService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<SisterType>> getAllInactive() {
        return ResponseEntity.ok(sisterTypeService.getAllInactive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<SisterType> getById(@PathVariable Long id) {
        Optional<SisterType> st = sisterTypeService.getById(id);
        return st.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<SisterType> update(@PathVariable Long id, @RequestBody SisterType updated) {
        return ResponseEntity.ok(sisterTypeService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sisterTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<SisterType>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(sisterTypeService.search(keyword));
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<SisterType>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(sisterTypeService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<SisterType>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(sisterTypeService.getActiveByAdmin(adminId));
    }
}