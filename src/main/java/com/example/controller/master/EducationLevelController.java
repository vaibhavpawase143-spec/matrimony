package com.example.controller.master;

import com.example.model.EducationLevel;
import com.example.service.EducationLevelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/education-levels")
public class EducationLevelController {

    private final EducationLevelService service;

    public EducationLevelController(EducationLevelService service) {
        this.service = service;
    }

    // Get all
    @GetMapping
    public List<EducationLevel> getAll() {
        return service.getAll();
    }

    // Get active
    @GetMapping("/active")
    public List<EducationLevel> getActive() {
        return service.getActive();
    }

    // Get by ID
    @GetMapping("/{id}")
    public EducationLevel getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create
    @PostMapping
    public EducationLevel create(@RequestBody EducationLevel edu) {
        return service.create(edu);
    }

    // Update
    @PutMapping("/{id}")
    public EducationLevel update(@PathVariable Long id, @RequestBody EducationLevel edu) {
        return service.update(id, edu);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<EducationLevel> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<EducationLevel> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search
    @GetMapping("/search")
    public List<EducationLevel> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}