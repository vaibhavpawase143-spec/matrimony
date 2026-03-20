package com.example.controller.master; // master folder

import com.example.model.SubCaste;
import com.example.service.SubCasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sub-castes")
public class SubCasteController {

    @Autowired
    private SubCasteService subCasteService;

    // Create new SubCaste
    @PostMapping
    public ResponseEntity<SubCaste> create(@RequestBody SubCaste subCaste) {
        return ResponseEntity.ok(subCasteService.create(subCaste));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<SubCaste>> getAll() {
        return ResponseEntity.ok(subCasteService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<SubCaste>> getAllActive() {
        return ResponseEntity.ok(subCasteService.getAllActive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubCaste> getById(@PathVariable Long id) {
        Optional<SubCaste> sc = subCasteService.getById(id);
        return sc.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<SubCaste> update(@PathVariable Long id, @RequestBody SubCaste updated) {
        return ResponseEntity.ok(subCasteService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subCasteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<SubCaste>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(subCasteService.search(keyword));
    }

    // Get by Caste
    @GetMapping("/caste/{casteId}")
    public ResponseEntity<List<SubCaste>> getByCaste(@PathVariable Long casteId) {
        return ResponseEntity.ok(subCasteService.getByCaste(casteId));
    }

    // Get active by Caste
    @GetMapping("/caste/{casteId}/active")
    public ResponseEntity<List<SubCaste>> getActiveByCaste(@PathVariable Long casteId) {
        return ResponseEntity.ok(subCasteService.getActiveByCaste(casteId));
    }

    // Get by Admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<SubCaste>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(subCasteService.getByAdmin(adminId));
    }
}