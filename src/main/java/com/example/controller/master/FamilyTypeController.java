package com.example.controller.master;

import com.example.dto.request.FamilyTypeRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FamilyTypeResponseDto;
import com.example.service.FamilyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/family-types")
@RequiredArgsConstructor
public class FamilyTypeController {

    private final FamilyTypeService familyTypeService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FamilyTypeResponseDto> create(
            @Valid @RequestBody FamilyTypeRequestDto requestDto) {

        return ApiResponse.success(
                "Family Type created successfully.",
                familyTypeService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FamilyTypeResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FamilyTypeRequestDto requestDto) {

        return ApiResponse.success(
                "Family Type updated successfully.",
                familyTypeService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        familyTypeService.softDelete(id);

        return ApiResponse.success(
                "Family Type deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        familyTypeService.restore(id);

        return ApiResponse.success(
                "Family Type restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        familyTypeService.hardDelete(id);

        return ApiResponse.success(
                "Family Type permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FamilyTypeResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Family Type fetched successfully.",
                familyTypeService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FamilyTypeResponseDto>> getAll() {

        return ApiResponse.success(
                "Family Types fetched successfully.",
                familyTypeService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FamilyTypeResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted Family Types fetched successfully.",
                familyTypeService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<FamilyTypeResponseDto>> getActive() {

        return ApiResponse.success(
                "Active Family Types fetched successfully.",
                familyTypeService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<FamilyTypeResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive Family Types fetched successfully.",
                familyTypeService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<FamilyTypeResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Family Types fetched successfully.",
                familyTypeService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<FamilyTypeResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Family Types fetched successfully.",
                familyTypeService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<FamilyTypeResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Family Types fetched successfully.",
                familyTypeService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<FamilyTypeResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyTypeService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<FamilyTypeResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyTypeService.searchByAdmin(adminId, keyword)
        );
    }
}