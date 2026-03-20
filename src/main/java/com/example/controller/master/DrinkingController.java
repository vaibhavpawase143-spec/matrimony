package com.example.controller.master;

import com.example.model.Drinking;
import com.example.service.DrinkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/drinking")
public class DrinkingController {

    private final DrinkingService service;

    public DrinkingController(DrinkingService service) {
        this.service = service;
    }

    // Get all
    @GetMapping
    public List<Drinking> getAll() {
        return service.getAll();
    }

    // Get active
    @GetMapping("/active")
    public List<Drinking> getActive() {
        return service.getActive();
    }

    // Get by ID
    @GetMapping("/{id}")
    public Drinking getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create
    @PostMapping
    public Drinking create(@RequestBody Drinking drinking) {
        return service.create(drinking);
    }

    // Update
    @PutMapping("/{id}")
    public Drinking update(@PathVariable Long id, @RequestBody Drinking drinking) {
        return service.update(id, drinking);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<Drinking> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<Drinking> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search
    @GetMapping("/search")
    public List<Drinking> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}