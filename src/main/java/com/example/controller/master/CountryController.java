package com.example.controller.master;

import com.example.model.Country;
import com.example.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/countries")
public class CountryController {

    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    // Get all countries
    @GetMapping
    public List<Country> getAll() {
        return service.getAll();
    }

    // Get active countries
    @GetMapping("/active")
    public List<Country> getActive() {
        return service.getActive();
    }

    // Get country by ID
    @GetMapping("/{id}")
    public Country getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create country
    @PostMapping
    public Country create(@RequestBody Country country) {
        return service.create(country);
    }

    // Update country
    @PutMapping("/{id}")
    public Country update(@PathVariable Long id, @RequestBody Country country) {
        return service.update(id, country);
    }

    // Delete country
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Get by admin
    @GetMapping("/admin/{adminId}")
    public List<Country> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<Country> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }

    // Search country
    @GetMapping("/search")
    public List<Country> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}