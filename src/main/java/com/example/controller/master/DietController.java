package com.example.controller.master;

import com.example.model.Diet;
import com.example.service.DietService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/diets")
public class DietController {

    private final DietService service;

    public DietController(DietService service) {
        this.service = service;
    }

    // Get all
    @GetMapping
    public List<Diet> getAll() {
        return service.getAll();
    }

    // Get active
    @GetMapping("/active")
    public List<Diet> getActive() {
        return service.getActive();
    }

    // Get by ID
    @GetMapping("/{id}")
    public Diet getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create
    @PostMapping
    public Diet create(@RequestBody Diet diet) {
        return service.create(diet);
    }

    // Update
    @PutMapping("/{id}")
    public Diet update(@PathVariable Long id, @RequestBody Diet diet) {
        return service.update(id, diet);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<Diet> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<Diet> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search
    @GetMapping("/search")
    public List<Diet> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}