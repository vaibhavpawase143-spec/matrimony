package com.example.controller.master;

import com.example.model.DisabilityStatus;
import com.example.service.DisabilityStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/disability-status")
public class DisabilityStatusController {

    private final DisabilityStatusService service;

    public DisabilityStatusController(DisabilityStatusService service) {
        this.service = service;
    }

    // Get all
    @GetMapping
    public List<DisabilityStatus> getAll() {
        return service.getAll();
    }

    // Get active
    @GetMapping("/active")
    public List<DisabilityStatus> getActive() {
        return service.getActive();
    }

    // Get by ID
    @GetMapping("/{id}")
    public DisabilityStatus getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create
    @PostMapping
    public DisabilityStatus create(@RequestBody DisabilityStatus ds) {
        return service.create(ds);
    }

    // Update
    @PutMapping("/{id}")
    public DisabilityStatus update(@PathVariable Long id, @RequestBody DisabilityStatus ds) {
        return service.update(id, ds);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<DisabilityStatus> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<DisabilityStatus> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search
    @GetMapping("/search")
    public List<DisabilityStatus> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}