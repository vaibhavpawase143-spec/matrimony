package com.example.controller.master;

import com.example.model.BloodGroup;
import com.example.service.BloodGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/blood-group")
@CrossOrigin("*")
public class BloodGroupController {


    @Autowired
    private BloodGroupService service;

    // ✅ Get all
    @GetMapping
    public List<BloodGroup> getAll() {
        return service.getAll();
    }

    // ✅ Get active
    @GetMapping("/active")
    public List<BloodGroup> getActive() {
        return service.getActive();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public BloodGroup getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ Create
    @PostMapping
    public BloodGroup create(@RequestBody BloodGroup bg) {
        return service.create(bg);
    }

    // ✅ Update
    @PutMapping("/{id}")
    public BloodGroup update(@PathVariable Long id, @RequestBody BloodGroup bg) {
        return service.update(id, bg);
    }

    // ✅ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Blood group deleted successfully";
    }

    // ✅ Get by admin
    @GetMapping("/admin/{adminId}")
    public List<BloodGroup> getByAdmin(@PathVariable Long adminId) {
        return service.getByAdmin(adminId);
    }

    // ✅ Get active by admin
    @GetMapping("/admin/{adminId}/active")
    public List<BloodGroup> getActiveByAdmin(@PathVariable Long adminId) {
        return service.getActiveByAdmin(adminId);
    }


}
