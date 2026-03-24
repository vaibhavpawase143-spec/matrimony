package com.example.controller.master;

import com.example.dto.request.EmployedRequestDto;
import com.example.dto.response.EmployedResponseDto;
import com.example.model.Admin;
import com.example.model.Employed;
import com.example.service.EmployedService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/employed")
public class EmployedController {

    private final EmployedService employedService;

    public EmployedController(EmployedService employedService) {
        this.employedService = employedService;
    }

    // ✅ Create
    @PostMapping
    public EmployedResponseDto create(@Valid @RequestBody EmployedRequestDto dto) {
        Employed entity = mapToEntity(dto);
        return mapToResponse(employedService.create(entity));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public EmployedResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody EmployedRequestDto dto) {
        Employed entity = mapToEntity(dto);
        return mapToResponse(employedService.update(id, entity));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        employedService.delete(id);
        return "Employed deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public EmployedResponseDto getById(@PathVariable Long id) {
        return employedService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Employed not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<EmployedResponseDto> getAll() {
        return employedService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<EmployedResponseDto> getActive() {
        return employedService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/inactive")
    public List<EmployedResponseDto> getInactive() {
        return employedService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<EmployedResponseDto> getByAdmin(@PathVariable Long adminId) {
        return employedService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<EmployedResponseDto> getActiveByAdmin(@PathVariable Long adminId) {
        return employedService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<EmployedResponseDto> search(@RequestParam String keyword) {
        return employedService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search by Admin
    @GetMapping("/admin/{adminId}/search")
    public List<EmployedResponseDto> searchByAdmin(@PathVariable Long adminId,
                                                   @RequestParam String keyword) {
        return employedService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    private Employed mapToEntity(EmployedRequestDto dto) {
        Employed entity = new Employed();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setName(dto.getName());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    private EmployedResponseDto mapToResponse(Employed entity) {
        EmployedResponseDto dto = new EmployedResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // 🔥 Use direct adminId (better performance)
        dto.setAdminId(entity.getAdminId());

        return dto;
    }
}