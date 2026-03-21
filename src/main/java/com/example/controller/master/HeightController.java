package com.example.controller.master; // master folder

import com.example.model.Height;
import com.example.service.HeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/heights")
public class HeightController {

    @Autowired
    private HeightService heightService;

    // Create new Height
    @PostMapping
    public ResponseEntity<Height> save(@RequestBody Height height) {
        return ResponseEntity.ok(heightService.save(height));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Height> getById(@PathVariable Long id) {
        Optional<Height> h = heightService.getById(id);
        return h.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by height value
    @GetMapping("/value/{heightValue}")
    public ResponseEntity<Height> getByHeight(@PathVariable("heightValue") String heightValue) {
        Optional<Height> h = heightService.getByHeight(heightValue);
        return h.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Height>> getAll() {
        return ResponseEntity.ok(heightService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Height>> getAllActive() {
        return ResponseEntity.ok(heightService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Height>> getAllInactive() {
        return ResponseEntity.ok(heightService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Height>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(heightService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Height>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(heightService.getActiveByAdmin(adminId));
    }

    // Search by height keyword
    @GetMapping("/search")
    public ResponseEntity<List<Height>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(heightService.searchByHeight(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Height> update(@PathVariable Long id, @RequestBody Height updated) {
        return ResponseEntity.ok(heightService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        heightService.delete(id);
        return ResponseEntity.noContent().build();
    }
}