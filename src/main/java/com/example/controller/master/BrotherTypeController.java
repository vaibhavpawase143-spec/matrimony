package com.example.controller.master;

import com.example.dto.request.BrotherTypeRequestDTO;
import com.example.dto.responce.BrotherTypeResponseDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.BrotherType;
import com.example.service.BrotherTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins/{adminId}/brother-types")
@RequiredArgsConstructor
public class BrotherTypeController {

    private final BrotherTypeService brotherTypeService;

    @PostMapping
    public ApiResponse<BrotherTypeResponseDTO> create(
            @PathVariable Long adminId,
            @Valid @RequestBody BrotherTypeRequestDTO dto) {

        BrotherType saved = brotherTypeService.create(
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<BrotherTypeResponseDTO>builder()
                .success(true)
                .message("Brother type created successfully.")
                .data(mapToResponse(saved))
                .build();
    }
    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ApiResponse<BrotherTypeResponseDTO> getById(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        BrotherType brotherType = brotherTypeService.getById(id, adminId);

        return ApiResponse.<BrotherTypeResponseDTO>builder()
                .success(true)
                .message("Brother type retrieved successfully.")
                .data(mapToResponse(brotherType))
                .build();
    }
    // ================= GET ALL =================
    @GetMapping
    public ApiResponse<List<BrotherTypeResponseDTO>> getAll(
            @PathVariable Long adminId) {

        List<BrotherTypeResponseDTO> brotherTypes = brotherTypeService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BrotherTypeResponseDTO>>builder()
                .success(true)
                .message("Brother types retrieved successfully.")
                .data(brotherTypes)
                .build();
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ApiResponse<List<BrotherTypeResponseDTO>> getActive(
            @PathVariable Long adminId) {

        List<BrotherTypeResponseDTO> activeBrotherTypes = brotherTypeService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BrotherTypeResponseDTO>>builder()
                .success(true)
                .message("Active brother types retrieved successfully.")
                .data(activeBrotherTypes)
                .build();
    }
    // ================= SEARCH =================


    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ApiResponse<BrotherTypeResponseDTO> update(
            @PathVariable Long adminId,
            @PathVariable Long id,
            @Valid @RequestBody BrotherTypeRequestDTO dto) {

        BrotherType updated = brotherTypeService.update(
                id,
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<BrotherTypeResponseDTO>builder()
                .success(true)
                .message("Brother type updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        brotherTypeService.delete(id, adminId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Brother type deleted successfully.")
                .build();
    }

    // ================= MAPPERS =================

    private BrotherType mapToEntity(BrotherTypeRequestDTO dto) {

        BrotherType entity = new BrotherType();

        entity.setValue(dto.getValue());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return entity;
    }

    private BrotherTypeResponseDTO mapToResponse(BrotherType entity) {

        BrotherTypeResponseDTO dto = new BrotherTypeResponseDTO();

        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
        }

        return dto;
    }
    @GetMapping("/deleted")
    public ApiResponse<List<BrotherTypeResponseDTO>> getDeleted(
            @PathVariable Long adminId) {

        List<BrotherTypeResponseDTO> deleted = brotherTypeService
                .getDeleted(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BrotherTypeResponseDTO>>builder()
                .success(true)
                .message("Deleted brother types retrieved successfully.")
                .data(deleted)
                .build();
    }
    @PutMapping("/restore/{id}")
    public ApiResponse<BrotherTypeResponseDTO> restore(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        BrotherType restored = brotherTypeService.restore(id);

        return ApiResponse.<BrotherTypeResponseDTO>builder()
                .success(true)
                .message("Brother type restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }
    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long adminId,
            @PathVariable Long id) {

        brotherTypeService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Brother type permanently deleted successfully.")
                .build();
    }

}