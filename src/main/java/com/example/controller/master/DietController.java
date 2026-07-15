package com.example.controller.master;

import com.example.dto.request.DietRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.DietResponseDto;
import com.example.model.Admin;
import com.example.model.Diet;
import com.example.service.DietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<DietResponseDto> create(
            @Valid @RequestBody DietRequestDto dto) {

        Diet saved = dietService.create(mapToEntity(dto));

        return ApiResponse.<DietResponseDto>builder()
                .success(true)
                .message("Diet created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<DietResponseDto> getById(
            @PathVariable Long id) {

        Diet diet = dietService.getById(id);

        return ApiResponse.<DietResponseDto>builder()
                .success(true)
                .message("Diet retrieved successfully.")
                .data(mapToResponse(diet))
                .build();
    }

    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<DietResponseDto>> getAll() {

        List<DietResponseDto> diets = dietService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Diets retrieved successfully.")
                .data(diets)
                .build();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<DietResponseDto>> getActive() {

        List<DietResponseDto> diets = dietService.getActive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Active diets retrieved successfully.")
                .data(diets)
                .build();
    }
    // ================= GET INACTIVE =================
    @GetMapping("/inactive")
    public ApiResponse<List<DietResponseDto>> getInactive() {

        List<DietResponseDto> diets = dietService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Inactive diets retrieved successfully.")
                .data(diets)
                .build();
    }

    // ================= GET BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<DietResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        List<DietResponseDto> diets = dietService.getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Diets retrieved successfully by admin.")
                .data(diets)
                .build();
    }

    // ================= GET ACTIVE BY ADMIN =================
    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<DietResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        List<DietResponseDto> diets = dietService.getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Active diets retrieved successfully by admin.")
                .data(diets)
                .build();
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ApiResponse<List<DietResponseDto>> search(
            @RequestParam String keyword) {

        List<DietResponseDto> diets = dietService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Diets searched successfully.")
                .data(diets)
                .build();
    }

    // ================= SEARCH BY ADMIN =================
    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<DietResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        List<DietResponseDto> diets = dietService.searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Diets searched successfully by admin.")
                .data(diets)
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<DietResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DietRequestDto dto) {

        Diet updated = dietService.update(id, mapToEntity(dto));

        return ApiResponse.<DietResponseDto>builder()
                .success(true)
                .message("Diet updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        dietService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Diet deleted successfully.")
                .build();
    }

    // ================= GET DELETED =================
    @GetMapping("/deleted")
    public ApiResponse<List<DietResponseDto>> getDeleted() {

        List<DietResponseDto> deleted = dietService.getDeleted()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DietResponseDto>>builder()
                .success(true)
                .message("Deleted diets retrieved successfully.")
                .data(deleted)
                .build();
    }

    // ================= RESTORE =================
    @PutMapping("/restore/{id}")
    public ApiResponse<DietResponseDto> restore(
            @PathVariable Long id) {

        Diet restored = dietService.restore(id);

        return ApiResponse.<DietResponseDto>builder()
                .success(true)
                .message("Diet restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }

    // ================= HARD DELETE =================
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        dietService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Diet permanently deleted successfully.")
                .build();
    }    // =========================
    // 🔁 MAPPING METHODS
    // =========================

    // ================= MAPPERS =================

    private Diet mapToEntity(DietRequestDto dto) {

        Diet diet = new Diet();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        diet.setAdmin(admin);
        diet.setName(dto.getName());

        diet.setIsActive(
                dto.getIsActive() != null
                        ? dto.getIsActive()
                        : true
        );

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
