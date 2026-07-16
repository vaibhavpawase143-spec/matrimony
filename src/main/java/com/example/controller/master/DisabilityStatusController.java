package com.example.controller.master;

import com.example.dto.request.DisabilityStatusRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.DisabilityStatusResponseDto;
import com.example.model.Admin;
import com.example.model.DisabilityStatus;
import com.example.service.DisabilityStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disability-statuses")
@RequiredArgsConstructor
public class DisabilityStatusController {

    private final DisabilityStatusService disabilityStatusService;

    // ================= CREATE =================

    @PostMapping
    public ApiResponse<DisabilityStatusResponseDto> create(
            @Valid @RequestBody DisabilityStatusRequestDto dto) {

        DisabilityStatus saved = disabilityStatusService.create(mapToEntity(dto));

        return ApiResponse.<DisabilityStatusResponseDto>builder()
                .success(true)
                .message("Disability status created successfully.")
                .data(mapToResponse(saved))
                .build();
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ApiResponse<DisabilityStatusResponseDto> getById(
            @PathVariable Long id) {

        DisabilityStatus disabilityStatus = disabilityStatusService.getById(id);

        return ApiResponse.<DisabilityStatusResponseDto>builder()
                .success(true)
                .message("Disability status retrieved successfully.")
                .data(mapToResponse(disabilityStatus))
                .build();
    }

    // ================= GET ALL =================

    @GetMapping
    public ApiResponse<List<DisabilityStatusResponseDto>> getAll() {

        List<DisabilityStatusResponseDto> list = disabilityStatusService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= GET ACTIVE =================

    @GetMapping("/active")
    public ApiResponse<List<DisabilityStatusResponseDto>> getActive() {

        List<DisabilityStatusResponseDto> list = disabilityStatusService.getActive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Active disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= GET INACTIVE =================

    @GetMapping("/inactive")
    public ApiResponse<List<DisabilityStatusResponseDto>> getInactive() {

        List<DisabilityStatusResponseDto> list = disabilityStatusService.getInactive()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Inactive disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= GET BY ADMIN =================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<DisabilityStatusResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        List<DisabilityStatusResponseDto> list = disabilityStatusService
                .getByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= GET ACTIVE BY ADMIN =================

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<DisabilityStatusResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        List<DisabilityStatusResponseDto> list = disabilityStatusService
                .getActiveByAdmin(adminId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Active disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= SEARCH =================

    @GetMapping("/search")
    public ApiResponse<List<DisabilityStatusResponseDto>> search(
            @RequestParam String keyword) {

        List<DisabilityStatusResponseDto> list = disabilityStatusService.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Disability statuses searched successfully.")
                .data(list)
                .build();
    }

    // ================= SEARCH BY ADMIN =================

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<DisabilityStatusResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        List<DisabilityStatusResponseDto> list = disabilityStatusService
                .searchByAdmin(adminId, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Disability statuses searched successfully.")
                .data(list)
                .build();
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ApiResponse<DisabilityStatusResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DisabilityStatusRequestDto dto) {

        DisabilityStatus updated = disabilityStatusService.update(
                id,
                mapToEntity(dto)
        );

        return ApiResponse.<DisabilityStatusResponseDto>builder()
                .success(true)
                .message("Disability status updated successfully.")
                .data(mapToResponse(updated))
                .build();
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @PathVariable Long id,
            @RequestParam Long deletedBy) {

        disabilityStatusService.delete(id, deletedBy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Disability status deleted successfully.")
                .build();
    }

    // ================= GET DELETED =================

    @GetMapping("/deleted")
    public ApiResponse<List<DisabilityStatusResponseDto>> getDeleted() {

        List<DisabilityStatusResponseDto> list = disabilityStatusService.getDeleted()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.<List<DisabilityStatusResponseDto>>builder()
                .success(true)
                .message("Deleted disability statuses retrieved successfully.")
                .data(list)
                .build();
    }

    // ================= RESTORE =================

    @PutMapping("/restore/{id}")
    public ApiResponse<DisabilityStatusResponseDto> restore(
            @PathVariable Long id) {

        DisabilityStatus restored = disabilityStatusService.restore(id);

        return ApiResponse.<DisabilityStatusResponseDto>builder()
                .success(true)
                .message("Disability status restored successfully.")
                .data(mapToResponse(restored))
                .build();
    }

    // ================= HARD DELETE =================

    @DeleteMapping("/hard-delete/{id}")
    public ApiResponse<String> hardDelete(
            @PathVariable Long id) {

        disabilityStatusService.hardDelete(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Disability status permanently deleted successfully.")
                .build();
    }
    // ================= MAPPERS =================

    private DisabilityStatus mapToEntity(DisabilityStatusRequestDto dto) {

        DisabilityStatus entity = new DisabilityStatus();

        Admin admin = new Admin();
        admin.setId(dto.getAdminId());

        entity.setAdmin(admin);
        entity.setValue(dto.getValue());

        entity.setIsActive(
                dto.getIsActive() != null
                        ? dto.getIsActive()
                        : true
        );

        return entity;
    }

    private DisabilityStatusResponseDto mapToResponse(
            DisabilityStatus entity) {

        DisabilityStatusResponseDto dto =
                new DisabilityStatusResponseDto();

        dto.setId(entity.getId());
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