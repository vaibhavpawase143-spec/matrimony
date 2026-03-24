package com.example.controller.master;

import com.example.dto.request.DrinkingRequestDto;
import com.example.dto.response.DrinkingResponseDto;
import com.example.model.Admin;
import com.example.model.Drinking;
import com.example.service.DrinkingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/master/drinking")
public class DrinkingController {

    private final DrinkingService drinkingService;

    public DrinkingController(DrinkingService drinkingService) {
        this.drinkingService = drinkingService;
    }

    // ✅ Create
    @PostMapping
    public DrinkingResponseDto create(@Valid @RequestBody DrinkingRequestDto dto) {
        Drinking entity = mapToEntity(dto);
        return mapToResponse(drinkingService.create(entity));
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public DrinkingResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody DrinkingRequestDto dto) {
        Drinking entity = mapToEntity(dto);
        return mapToResponse(drinkingService.update(id, entity));
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        drinkingService.delete(id);
        return "Drinking deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public DrinkingResponseDto getById(@PathVariable Long id) {
        return drinkingService.getById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Drinking not found"));
    }

    // 🔍 Get all
    @GetMapping
    public List<DrinkingResponseDto> getAll() {
        return drinkingService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active
    @GetMapping("/active")
    public List<DrinkingResponseDto> getActive() {
        return drinkingService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Inactive
    @GetMapping("/inactive")
    public List<DrinkingResponseDto> getInactive() {
        return drinkingService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 By Admin
    @GetMapping("/admin/{adminId}")
    public List<DrinkingResponseDto> getByAdmin(@PathVariable Long adminId) {
        return drinkingService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Active by Admin
    @GetMapping("/admin/{adminId}/active")
    public List<DrinkingResponseDto> getActiveByAdmin(@PathVariable Long adminId) {
        return drinkingService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<DrinkingResponseDto> search(@RequestParam String keyword) {
        return drinkingService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search by Admin
    @GetMapping("/admin/{adminId}/search")
    public List<DrinkingResponseDto> searchByAdmin(@PathVariable Long adminId,
                                                   @RequestParam String keyword) {
        return drinkingService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    private Drinking mapToEntity(DrinkingRequestDto dto) {

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        return Drinking.builder()
                .name(dto.getName())
                .value(dto.getValue())
                .isActive(dto.getIsActive())
                .admin(admin)
                .build();
    }

    private DrinkingResponseDto mapToResponse(Drinking entity) {
        DrinkingResponseDto dto = new DrinkingResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}