package com.example.controller.master;

import com.example.dto.request.DietRequestDto;
import com.example.dto.response.DietResponseDto;
import com.example.model.Admin;
import com.example.model.Diet;
import com.example.service.DietService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diets")
public class DietController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    // ✅ Create
    @PostMapping
    public DietResponseDto create(@Valid @RequestBody DietRequestDto dto) {

        Diet diet = mapToEntity(dto);
        Diet saved = dietService.create(diet);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public DietResponseDto update(@PathVariable Long id,
                                  @Valid @RequestBody DietRequestDto dto) {

        Diet diet = mapToEntity(dto);
        Diet updated = dietService.update(id, diet);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        dietService.delete(id);
        return "Diet deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public DietResponseDto getById(@PathVariable Long id) {
        Diet diet = dietService.getById(id)
                .orElseThrow(() -> new RuntimeException("Diet not found"));
        return mapToResponse(diet);
    }

    // 🔍 Get all
    @GetMapping
    public List<DietResponseDto> getAll() {
        return dietService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<DietResponseDto> getActive() {
        return dietService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/inactive")
    public List<DietResponseDto> getInactive() {
        return dietService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<DietResponseDto> getByAdmin(@PathVariable Long adminId) {
        return dietService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<DietResponseDto> getActiveByAdmin(@PathVariable Long adminId) {
        return dietService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<DietResponseDto> search(@RequestParam String keyword) {
        return dietService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search by Admin
    @GetMapping("/admin/{adminId}/search")
    public List<DietResponseDto> searchByAdmin(@PathVariable Long adminId,
                                               @RequestParam String keyword) {
        return dietService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    private Diet mapToEntity(DietRequestDto dto) {
        Diet diet = new Diet();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        diet.setAdmin(admin);
        diet.setName(dto.getName());
        diet.setIsActive(dto.getIsActive());

        return diet;
    }

    private DietResponseDto mapToResponse(Diet diet) {
        DietResponseDto dto = new DietResponseDto();

        dto.setId(diet.getId());
        dto.setName(diet.getName());
        dto.setIsActive(diet.getIsActive());
        dto.setCreatedAt(diet.getCreatedAt());
        dto.setUpdatedAt(diet.getUpdatedAt());

        if (diet.getAdmin() != null) {
            dto.setAdminId(diet.getAdmin().getId());
        }

        return dto;
    }
}