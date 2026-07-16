package com.example.controller.master;

import com.example.dto.request.RoleRequestDTO;
import com.example.dto.response.RoleResponseDTO;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    // =========================
    // CREATE
    // =========================
    @PostMapping
    public RoleResponseDTO create(@Valid @RequestBody RoleRequestDTO dto) {

        Role entity = mapToEntity(dto);

        Role saved = service.save(entity);

        return mapToResponse(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public RoleResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto) {

        Role existing = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        updateEntity(existing, dto);

        Role updated = service.save(existing);

        return mapToResponse(updated);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public RoleResponseDTO getById(@PathVariable Long id) {

        Role role = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return mapToResponse(role);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<RoleResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // ADMIN APIs
    // =========================

    @GetMapping("/admin/{adminId}")
    public List<RoleResponseDTO> getByAdmin(@PathVariable Long adminId) {

        return service.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/active")
    public List<RoleResponseDTO> getActive(@PathVariable Long adminId) {

        return service.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}/inactive")
    public List<RoleResponseDTO> getInactive(@PathVariable Long adminId) {

        return service.getInactiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/admin/{adminId}/search")
    public List<RoleResponseDTO> search(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return service.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Role deleted successfully";
    }

    // =========================
    // 🔥 MAPPING
    // =========================

    private Role mapToEntity(RoleRequestDTO dto) {

        Role role = new Role();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        role.setAdmin(admin);

        role.setName(dto.getName());

        if (dto.getIsActive() != null) {
            role.setIsActive(dto.getIsActive());
        }

        return role;
    }

    private void updateEntity(Role role, RoleRequestDTO dto) {

        // 🚫 DO NOT update admin

        if (dto.getName() != null) {
            role.setName(dto.getName());
        }

        if (dto.getIsActive() != null) {
            role.setIsActive(dto.getIsActive());
        }
    }

    private RoleResponseDTO mapToResponse(Role role) {

        return RoleResponseDTO.builder()
                .id(role.getId())
                .adminId(role.getAdmin() != null ? role.getAdmin().getId() : null)
                .adminName(role.getAdmin() != null ? role.getAdmin().getName() : null)
                .name(role.getName())
                .isActive(role.getIsActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}