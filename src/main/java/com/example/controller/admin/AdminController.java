package com.example.controller.admin;

import com.example.dto.other.AdminLoginDTO;
import com.example.dto.request.AdminRequestDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;
import com.example.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ================= CREATE =================
    @PostMapping
    public AdminResponseDTO create(@Valid @RequestBody AdminRequestDTO dto) {

        Admin admin = mapToEntity(dto);
        Admin saved = adminService.create(admin);

        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public AdminResponseDTO getById(@PathVariable Long id) {
        return mapToResponse(adminService.getById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    public List<AdminResponseDTO> getAll() {
        return adminService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public AdminResponseDTO update(@PathVariable Long id,
                                   @Valid @RequestBody AdminRequestDTO dto) {

        Admin updated = adminService.update(id, mapToEntity(dto));
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return "Admin deleted successfully";
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public AdminResponseDTO login(@Valid @RequestBody AdminLoginDTO dto) {

        Admin admin = adminService.login(dto.getUsername(), dto.getPassword());

        return mapToResponse(admin);
    }

    // ================= MAPPERS =================

    private Admin mapToEntity(AdminRequestDTO dto) {
        Admin admin = new Admin();

        admin.setName(dto.getName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setPhone(dto.getPhone());

        return admin;
    }

    private AdminResponseDTO mapToResponse(Admin admin) {
        AdminResponseDTO dto = new AdminResponseDTO();

        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setIsActive(admin.getIsActive());
        dto.setLastLogin(admin.getLastLogin());
        dto.setCreatedAt(admin.getCreatedAt());

        return dto;
    }
}