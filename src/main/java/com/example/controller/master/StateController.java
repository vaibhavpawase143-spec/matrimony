package com.example.controller.master; // master folder

import com.example.model.State;
import com.example.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/states")
public class StateController {

    @Autowired
    private StateService stateService;

    // Create new State
    @PostMapping
    public ResponseEntity<State> create(@RequestBody State state) {
        return ResponseEntity.ok(stateService.create(state));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<State>> getAll() {
        return ResponseEntity.ok(stateService.getAll());
    }

    // Get all active
    @GetMapping("/active")
    public ResponseEntity<List<State>> getAllActive() {
        return ResponseEntity.ok(stateService.getAllActive());
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<State> getById(@PathVariable Long id) {
        Optional<State> s = stateService.getById(id);
        return s.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<State> update(@PathVariable Long id, @RequestBody State updated) {
        return ResponseEntity.ok(stateService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<State>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(stateService.search(keyword));
    }

    // Get by Country
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<State>> getByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(stateService.getByCountry(countryId));
    }

    // Get active by Country
    @GetMapping("/country/{countryId}/active")
    public ResponseEntity<List<State>> getActiveByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(stateService.getActiveByCountry(countryId));
    }

    // Get by Admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<State>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(stateService.getByAdmin(adminId));
    }
}