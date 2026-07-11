package com.example.controller.master;

import com.example.dto.request.FamilyRequestDto;
import com.example.dto.response.FamilyResponseDto;
import com.example.model.Admin;
import com.example.model.Family;
import com.example.service.FamilyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/family")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    public FamilyResponseDto create(@Valid @RequestBody FamilyRequestDto dto) {
        return mapToResponse(familyService.create(mapToEntity(dto)));
    }

    @PutMapping("/{id}")
    public FamilyResponseDto update(@PathVariable Long id,
                                    @Valid @RequestBody FamilyRequestDto dto) {
        return mapToResponse(familyService.update(id, mapToEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        familyService.delete(id);
        return "Family deleted successfully";
    }

    @GetMapping("/{id}")
    public FamilyResponseDto getById(@PathVariable Long id) {
        return familyService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Family not found"));
    }

    @GetMapping
    public List<FamilyResponseDto> getAll() {
        return familyService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/active")
    public List<FamilyResponseDto> getActive() {
        return familyService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}")
    public List<FamilyResponseDto> getByAdmin(@PathVariable Long adminId) {
        return familyService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Mapping

    private Family mapToEntity(FamilyRequestDto dto) {
        Family family = new Family();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        family.setAdmin(admin);
        family.setName(dto.getName());
        family.setIsActive(dto.getIsActive());

        return family;
    }

    private FamilyResponseDto mapToResponse(Family entity) {
        FamilyResponseDto dto = new FamilyResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}