package com.example.controller.master; // master folder

import com.example.model.Role;
import com.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Create new Role
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.create(role));
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id) {
        Optional<Role> r = roleService.getById(id);
        return r.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update role
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role updated) {
        return ResponseEntity.ok(roleService.update(id, updated));
    }

    // Delete role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search roles
    @GetMapping("/search")
    public ResponseEntity<List<Role>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(roleService.search(keyword));
    }

    // Get roles by admin
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Role>> getByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(roleService.getByAdmin(adminId));
    }
}