package com.example.controller.master; // master folder

import com.example.model.MaritalStatus;
import com.example.service.MaritalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/marital-status")
public class MaritalStatusController {

    @Autowired
    private MaritalStatusService maritalStatusService;

    // Create new MaritalStatus
    @PostMapping
    public ResponseEntity<MaritalStatus> save(@RequestBody MaritalStatus status) {
        return ResponseEntity.ok(maritalStatusService.save(status));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<MaritalStatus> getById(@PathVariable Long id) {
        Optional<MaritalStatus> ms = maritalStatusService.getById(id);
        return ms.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<MaritalStatus> getByName(@PathVariable String name) {
        Optional<MaritalStatus> ms = maritalStatusService.getByName(name);
        return ms.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<MaritalStatus>> getAll() {
        return ResponseEntity.ok(maritalStatusService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<MaritalStatus>> getAllActive() {
        return ResponseEntity.ok(maritalStatusService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<MaritalStatus>> getAllInactive() {
        return ResponseEntity.ok(maritalStatusService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<MaritalStatus>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(maritalStatusService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<MaritalStatus>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(maritalStatusService.getActiveByAdmin(adminId));
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<List<MaritalStatus>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(maritalStatusService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<MaritalStatus> update(@PathVariable Long id, @RequestBody MaritalStatus updated) {
        return ResponseEntity.ok(maritalStatusService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maritalStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}