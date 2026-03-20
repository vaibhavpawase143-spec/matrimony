package com.example.controller.master; // master folder

import com.example.model.ManglikStatus;
import com.example.service.ManglikStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manglik-status")
public class ManglikStatusController {

    @Autowired
    private ManglikStatusService manglikStatusService;

    // Create new ManglikStatus
    @PostMapping
    public ResponseEntity<ManglikStatus> save(@RequestBody ManglikStatus status) {
        return ResponseEntity.ok(manglikStatusService.save(status));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ManglikStatus> getById(@PathVariable Long id) {
        Optional<ManglikStatus> ms = manglikStatusService.getById(id);
        return ms.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ManglikStatus> getByName(@PathVariable String name) {
        Optional<ManglikStatus> ms = manglikStatusService.getByName(name);
        return ms.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ManglikStatus>> getAll() {
        return ResponseEntity.ok(manglikStatusService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<ManglikStatus>> getAllActive() {
        return ResponseEntity.ok(manglikStatusService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<ManglikStatus>> getAllInactive() {
        return ResponseEntity.ok(manglikStatusService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<ManglikStatus>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(manglikStatusService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<ManglikStatus>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(manglikStatusService.getActiveByAdmin(adminId));
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<List<ManglikStatus>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(manglikStatusService.searchByName(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ManglikStatus> update(@PathVariable Long id, @RequestBody ManglikStatus updated) {
        return ResponseEntity.ok(manglikStatusService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        manglikStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}