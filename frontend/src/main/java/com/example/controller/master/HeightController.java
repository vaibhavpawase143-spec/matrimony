package com.example.controller.master;

import com.example.dto.request.HeightRequestDTO;
import com.example.dto.responce.HeightResponseDTO;
import com.example.model.Admin;
import com.example.model.Height;
import com.example.service.HeightService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/heights")
@RequiredArgsConstructor
public class HeightController {

    private final HeightService heightService;

    // ✅ Create
    @PostMapping
    public HeightResponseDTO create(@RequestBody HeightRequestDTO dto) {

        Height entity = mapToEntity(dto);
        Height saved = heightService.create(entity);

        return mapToResponse(saved);
    }

    // 🔄 Update
    @PutMapping("/{id}")
    public HeightResponseDTO update(
            @PathVariable Long id,
            @RequestBody HeightRequestDTO dto
    ) {
        Height entity = mapToEntity(dto);
        Height updated = heightService.update(id, entity);

        return mapToResponse(updated);
    }

    // ❌ Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        heightService.delete(id);
        return "Height deleted successfully";
    }

    // 🔍 Get by ID
    @GetMapping("/{id}")
    public HeightResponseDTO getById(@PathVariable Long id) {
        Height height = heightService.getById(id)
                .orElseThrow(() -> new RuntimeException("Height not found"));

        return mapToResponse(height);
    }

    // 🔍 Get all
    @GetMapping
    public List<HeightResponseDTO> getAll() {
        return heightService.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Get active
    @GetMapping("/active")
    public List<HeightResponseDTO> getActive() {
        return heightService.getActive()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Get by admin
    @GetMapping("/admin/{adminId}")
    public List<HeightResponseDTO> getByAdmin(@PathVariable Long adminId) {
        return heightService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔍 Search
    @GetMapping("/search")
    public List<HeightResponseDTO> search(@RequestParam String keyword) {
        return heightService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // 🔁 MAPPING METHODS
    // ===============================

    private Height mapToEntity(HeightRequestDTO dto) {

        Height entity = new Height();

        entity.setHeight(dto.getHeight());
        entity.setIsActive(dto.getIsActive());

        if (dto.getAdminId() != null) {
            Admin admin = new Admin();
            admin.setId(dto.getAdminId());
            entity.setAdmin(admin);
        }

        return entity;
    }

    private HeightResponseDTO mapToResponse(Height entity) {

        HeightResponseDTO dto = new HeightResponseDTO();

        dto.setId(entity.getId());
        dto.setHeight(entity.getHeight());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}