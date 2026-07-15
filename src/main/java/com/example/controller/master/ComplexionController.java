package com.example.controller.master;

import com.example.dto.request.ComplexionRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ComplexionResponseDTO;
import com.example.model.Admin;
import com.example.model.Complexion;
import com.example.service.ComplexionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complexions")
@RequiredArgsConstructor
public class ComplexionController {

    private final ComplexionService complexionService;

    // ================= CREATE =================
    @PostMapping
    public ApiResponse<ComplexionResponseDTO> create(
            @Valid @RequestBody ComplexionRequestDTO dto) {

        Complexion saved = complexionService.create(mapToEntity(dto));

        return ApiResponse.<ComplexionResponseDTO>builder()
                .success(true)
                .message("Complexion created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<ComplexionResponseDTO> getById(
            @PathVariable Long id) {

        Complexion complexion = complexionService.getById(id);

        return ApiResponse.<ComplexionResponseDTO>builder()
                .success(true)
                .message("Complexion retrieved successfully.")
                .data(mapToResponse(complexion))
                .build();
    }
    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<ComplexionResponseDTO>> getAll() {

        List<ComplexionResponseDTO> complexions = complexionService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<ComplexionResponseDTO>>builder()
                .success(true)
                .message("Complexions retrieved successfully.")
                .data(complexions)
                .build();
    }
    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<ComplexionResponseDTO>> getActive() {

        List<ComplexionResponseDTO> active = complexionService.getActive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<ComplexionResponseDTO>>builder()
                .success(true)
                .message("Active complexions retrieved successfully.")
                .data(active)
                .build();
    }

    // ================= GET BY ADMIN =================
    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<ComplexionResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        List<ComplexionResponseDTO> complexions = complexionService
                .getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<ComplexionResponseDTO>>builder()
                .success(true)
                .message("Complexions retrieved successfully by admin.")
                .data(complexions)
                .build();
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ApiResponse<List<ComplexionResponseDTO>> search(
            @RequestParam String keyword) {

        List<ComplexionResponseDTO> complexions = complexionService
                .search(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<ComplexionResponseDTO>>builder()
                .success(true)
                .message("Complexions searched successfully.")
                .data(complexions)
                .build();
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<ComplexionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ComplexionRequestDTO dto) {

        Complexion updated = complexionService.update(
                id,
                mapToEntity(dto)
        );

        return ApiResponse.<ComplexionResponseDTO>builder()
                .success(true)
                .message("Complexion updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        complexionService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Complexion deleted successfully.")
                .build();
    }
    @GetMapping("/deleted")
    public ApiResponse<List<ComplexionResponseDTO>> getDeleted() {

        List<ComplexionResponseDTO> deleted = complexionService.getDeleted()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<ComplexionResponseDTO>>builder()
                .success(true)
                .message("Deleted complexions retrieved successfully.")
                .data(deleted)
                .build();
    }
    @PutMapping("/restore/{id}")
    public ApiResponse<ComplexionResponseDTO> restore(
            @PathVariable Long id) {

        Complexion restored = complexionService.restore(id);

        return ApiResponse.<ComplexionResponseDTO>builder()
                .success(true)
                .message("Complexion restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        complexionService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Complexion permanently deleted successfully.")
                .build();
    }
    // ================= MAPPERS =================

    private Complexion mapToEntity(ComplexionRequestDTO dto) {

        Complexion entity = new Complexion();

        entity.setValue(dto.getValue());

        // Admin mapping
        Admin admin = new Admin();
        admin.setId(dto.getAdminId());
        entity.setAdmin(admin);

        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return entity;
    }

    private ComplexionResponseDTO mapToResponse(Complexion entity) {

        ComplexionResponseDTO dto = new ComplexionResponseDTO();

        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
}