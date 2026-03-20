package com.example.controller.admin;

import com.example.model.Admin;
import com.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping
    public List<Admin> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Admin getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Admin create(@RequestBody Admin admin) {
        return service.create(admin);
    }

    @PutMapping("/{id}")
    public Admin update(@PathVariable Long id, @RequestBody Admin admin) {
        return service.update(id, admin);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}