package com.example.controller.master;

import com.example.model.City;
import com.example.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/cities")
public class CityController {

    @Autowired
    private CityService service;

    // ✅ Get all cities
    @GetMapping
    public List<City> getAll() {
        return service.getAll();
    }

    // ✅ Get active cities
    @GetMapping("/active")
    public List<City> getActive() {
        return service.getActive();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public City getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ Create city
    @PostMapping
    public City create(@RequestBody City city) {
        return service.create(city);
    }

    // ✅ Update city
    @PutMapping("/{id}")
    public City update(@PathVariable Long id, @RequestBody City city) {
        return service.update(id, city);
    }

    // ✅ Delete city
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ Get cities by state
    @GetMapping("/state/{stateId}")
    public List<City> getByState(@PathVariable Long stateId) {
        return service.getByState(stateId);
    }

    // ✅ Get active cities by state
    @GetMapping("/state/{stateId}/active")
    public List<City> getActiveByState(@PathVariable Long stateId) {
        return service.getActiveByState(stateId);
    }

    // ✅ Get by admin
    @GetMapping("/admin/{adminId}")
    public List<City> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // ✅ Search city
    @GetMapping("/search")
    public List<City> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    // ✅ Search city by state
    @GetMapping("/state/{stateId}/search")
    public List<City> searchByState(@PathVariable Long stateId,
                                    @RequestParam String keyword) {
        return service.searchByState(stateId, keyword);
    }
}