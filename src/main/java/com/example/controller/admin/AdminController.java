package com.example.controller.admin;

import com.example.dto.other.AdminLoginDTO;
import com.example.dto.request.AdminRequestDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.model.Admin;
import com.example.service.AdminService;
import com.example.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    // ================= CREATE (FIRST ADMIN OPEN) =================
    @PostMapping
    public AdminResponseDTO create(@Valid @RequestBody AdminRequestDTO dto) {

        Admin admin = mapToEntity(dto);

        // ❌ REMOVE PASSWORD ENCODING FROM CONTROLLER
        // admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ✅ PASS RAW PASSWORD → SERVICE WILL ENCODE
        admin.setPassword(dto.getPassword());

        Admin saved = adminService.create(admin);

        return mapToResponse(saved);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminResponseDTO getById(@PathVariable Long id) {
        return mapToResponse(adminService.getById(id));
    }

    // ================= GET ALL =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminResponseDTO> getAll() {
        return adminService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminResponseDTO update(@PathVariable Long id,
                                   @Valid @RequestBody AdminRequestDTO dto) {

        Admin admin = mapToEntity(dto);

        // ✅ PASS RAW PASSWORD → SERVICE WILL ENCODE
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            admin.setPassword(dto.getPassword());
        }

        Admin updated = adminService.update(id, admin);
        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return "Admin deleted successfully";
    }

    // ================= LOGIN (PUBLIC) =================
    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody AdminLoginDTO dto) {

        Admin admin = adminService.login(dto.getUsername(), dto.getPassword());

        String token = jwtUtil.generateToken(
                admin.getEmail(),
                List.of("ROLE_ADMIN")
        );

        return Map.of(
                "token", token,
                "admin", mapToResponse(admin)
        );
    }

    // ================= MAPPERS =================

    private Admin mapToEntity(AdminRequestDTO dto) {
        Admin admin = new Admin();

        admin.setName(dto.getName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setIsActive(true);

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