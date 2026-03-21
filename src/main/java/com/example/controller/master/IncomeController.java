package com.example.controller.master; // master folder

import com.example.model.Income;
import com.example.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // Create new Income
    @PostMapping
    public ResponseEntity<Income> save(@RequestBody Income income) {
        return ResponseEntity.ok(incomeService.save(income));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Income> getById(@PathVariable Long id) {
        Optional<Income> inc = incomeService.getById(id);
        return inc.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get by range
    @GetMapping("/range/{range}")
    public ResponseEntity<Income> getByRange(@PathVariable String range) {
        Optional<Income> inc = incomeService.getByRange(range);
        return inc.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<Income>> getAll() {
        return ResponseEntity.ok(incomeService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<Income>> getAllActive() {
        return ResponseEntity.ok(incomeService.getAllActive());
    }

    // Get all inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<Income>> getAllInactive() {
        return ResponseEntity.ok(incomeService.getAllInactive());
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Income>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(incomeService.getByAdmin(adminId));
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Income>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(incomeService.getActiveByAdmin(adminId));
    }

    // Search by range keyword
    @GetMapping("/search")
    public ResponseEntity<List<Income>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(incomeService.searchByRange(keyword));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Income> update(@PathVariable Long id, @RequestBody Income updated) {
        return ResponseEntity.ok(incomeService.update(id, updated));
    }

    // Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}