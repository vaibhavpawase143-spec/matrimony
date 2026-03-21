package com.example.controller.master; // Change package based on service type

import com.example.model.Employed;
import com.example.service.EmployedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employed")
public class EmployedController {

    @Autowired
    private EmployedService employedService;

    @GetMapping
    public ResponseEntity<List<Employed>> getAll() {
        return ResponseEntity.ok(employedService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Employed>> getActive() {
        return ResponseEntity.ok(employedService.getActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employed> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employedService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Employed> create(@RequestBody Employed emp) {
        return ResponseEntity.ok(employedService.create(emp));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employed> update(@PathVariable Long id, @RequestBody Employed updated) {
        return ResponseEntity.ok(employedService.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employedService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Employed>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(employedService.getByAdmin(adminId));
    }

    @GetMapping("/admin/{adminId}/active")
    public ResponseEntity<List<Employed>> getActiveByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(employedService.getActiveByAdmin(adminId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employed>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(employedService.search(keyword));
    }
}