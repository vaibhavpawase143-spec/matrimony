package com.example.controller.master;

import com.example.dto.request.BodyTypeRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.BodyTypeResponseDTO;
import com.example.model.BodyType;
import com.example.service.BodyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BodyTypeController {

    private final BodyTypeService bodyTypeService;

    // =========================================
    // PUBLIC API FOR FRONTEND DROPDOWN
    // =========================================

    @GetMapping("/api/body-types")
    public List<BodyTypeResponseDTO> getAllPublic() {

        Long adminId = 1L;

        return bodyTypeService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================
    // ADMIN APIs
    // =========================================

    @PostMapping("/api/admins/{adminId}/body-types")
    public ApiResponse<BodyTypeResponseDTO> create(
            @PathVariable Long adminId,
            @Valid @RequestBody BodyTypeRequestDTO dto
    ) {

        BodyType saved = bodyTypeService.create(
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<BodyTypeResponseDTO>builder()
                .success(true)
                .message("Body type created successfully.")
                .data(mapToResponse(saved))
                .build();
    }
    @GetMapping("/api/admins/{adminId}/body-types/{id}")
    public ApiResponse<BodyTypeResponseDTO> getById(
            @PathVariable Long adminId,
            @PathVariable Long id
    ) {

        BodyType bodyType = bodyTypeService.getById(id, adminId);

        return ApiResponse.<BodyTypeResponseDTO>builder()
                .success(true)
                .message("Body type retrieved successfully.")
                .data(mapToResponse(bodyType))
                .build();
    }
    @GetMapping("/api/admins/{adminId}/body-types")
    public ApiResponse<List<BodyTypeResponseDTO>> getAll(
            @PathVariable Long adminId
    ) {

        List<BodyTypeResponseDTO> bodyTypes = bodyTypeService.getAll(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BodyTypeResponseDTO>>builder()
                .success(true)
                .message("Body types retrieved successfully.")
                .data(bodyTypes)
                .build();
    }
    @GetMapping("/api/admins/{adminId}/body-types/active")
    public ApiResponse<List<BodyTypeResponseDTO>> getActive(
            @PathVariable Long adminId
    ) {

        List<BodyTypeResponseDTO> activeBodyTypes = bodyTypeService.getActive(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BodyTypeResponseDTO>>builder()
                .success(true)
                .message("Active body types retrieved successfully.")
                .data(activeBodyTypes)
                .build();
    }
    @PutMapping("/api/admins/{adminId}/body-types/{id}")
    public ApiResponse<BodyTypeResponseDTO> update(
            @PathVariable Long adminId,
            @PathVariable Long id,
            @Valid @RequestBody BodyTypeRequestDTO dto
    ) {

        BodyType updated = bodyTypeService.update(
                id,
                mapToEntity(dto),
                adminId
        );

        return ApiResponse.<BodyTypeResponseDTO>builder()
                .success(true)
                .message("Body type updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }
    @DeleteMapping("/api/admins/{adminId}/body-types/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long adminId,
            @PathVariable Long id
    ) {

        bodyTypeService.delete(id, adminId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Body type deleted successfully.")
                .build();
    }
    // =========================================
    // MAPPERS
    // =========================================

    private BodyType mapToEntity(
            BodyTypeRequestDTO dto
    ) {

        BodyType bt = new BodyType();

        bt.setValue(dto.getValue());

        bt.setIsActive(
                dto.getIsActive() != null
                        ? dto.getIsActive()
                        : true
        );

        return bt;
    }

    private BodyTypeResponseDTO mapToResponse(
            BodyType bt
    ) {

        BodyTypeResponseDTO dto =
                new BodyTypeResponseDTO();

        dto.setId(bt.getId());
        dto.setValue(bt.getValue());
        dto.setIsActive(bt.getIsActive());
        dto.setCreatedAt(bt.getCreatedAt());

        if (bt.getAdmin() != null) {
            dto.setAdminId(
                    bt.getAdmin().getId()
            );
        }

        return dto;
    }
    @GetMapping("/api/admins/{adminId}/body-types/deleted")
    public ApiResponse<List<BodyTypeResponseDTO>> getDeleted(
            @PathVariable Long adminId
    ) {

        List<BodyTypeResponseDTO> deleted = bodyTypeService
                .getDeleted(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<BodyTypeResponseDTO>>builder()
                .success(true)
                .message("Deleted body types retrieved successfully.")
                .data(deleted)
                .build();
    }

    @PutMapping("/api/admins/{adminId}/body-types/restore/{id}")
    public ApiResponse<BodyTypeResponseDTO> restore(
            @PathVariable Long adminId,
            @PathVariable Long id
    ) {

        BodyType restored = bodyTypeService.restore(id);

        return ApiResponse.<BodyTypeResponseDTO>builder()
                .success(true)
                .message("Body type restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }

    @DeleteMapping("/api/admins/{adminId}/body-types/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long adminId,
            @PathVariable Long id
    ) {

        bodyTypeService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Body type permanently deleted successfully.")
                .build();
    }

}