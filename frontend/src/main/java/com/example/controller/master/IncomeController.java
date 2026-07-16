package com.example.controller.master;

import com.example.dto.request.IncomeRequestDTO;
import com.example.dto.responce.IncomeResponseDTO;
import com.example.model.Admin;
import com.example.model.Income;
import com.example.service.IncomeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    // ✅ Create
    @PostMapping
    public IncomeResponseDTO create(@Valid @RequestBody IncomeRequestDTO dto) {

        Income entity = mapToEntity(dto);
        Income saved = incomeService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public IncomeResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequestDTO dto
    ) {
        Income entity = mapToEntity(dto);
        Income updated = incomeService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        incomeService.delete(id);
        return "Income deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public IncomeResponseDTO getById(@PathVariable Long id) {
        Income income = incomeService.getById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        return mapToResponse(income);
    }

    // 🔍 Get all
    @GetMapping
    public List<IncomeResponseDTO> getAll() {
        return incomeService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<IncomeResponseDTO> getActive() {
        return incomeService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By admin
    @GetMapping("/admin/{adminId}")
    public List<IncomeResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return incomeService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<IncomeResponseDTO> search(@RequestParam String keyword) {
        return incomeService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 MAPPING METHODS
    // ===============================

    private Income mapToEntity(IncomeRequestDTO dto) {

        Income entity = new Income();

        entity.setRange(dto.getRange());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private IncomeResponseDTO mapToResponse(Income entity) {

        IncomeResponseDTO dto = new IncomeResponseDTO();

        dto.setId(entity.getId());
        dto.setRange(entity.getRange());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}