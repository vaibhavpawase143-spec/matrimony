package com.example.controller.master;

import com.example.model.Complexion;
import com.example.service.ComplexionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/complexions")
public class ComplexionController {

    private final ComplexionService service;

    public ComplexionController(ComplexionService service) {
        this.service = service;
    }

    // Get all
    @GetMapping
    public List<Complexion> getAll() {
        return service.getAll();
    }

    // Get active
    @GetMapping("/active")
    public List<Complexion> getActive() {
        return service.getActive();
    }

    // Get by ID
    @GetMapping("/{id}")
    public Complexion getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create
    @PostMapping
    public Complexion create(@RequestBody Complexion complexion) {
        return service.create(complexion);
    }

    // Update
    @PutMapping("/{id}")
    public Complexion update(@PathVariable Long id, @RequestBody Complexion complexion) {
        return service.update(id, complexion);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<Complexion> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<Complexion> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search
    @GetMapping("/search")
    public List<Complexion> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}