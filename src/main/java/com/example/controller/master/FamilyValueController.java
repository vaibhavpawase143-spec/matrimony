package com.example.controller.master;

import com.example.dto.request.FamilyValueRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FamilyValueResponseDto;
import com.example.service.FamilyValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/family-values")
@RequiredArgsConstructor
public class FamilyValueController {

    private final FamilyValueService familyValueService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FamilyValueResponseDto> create(
            @Valid @RequestBody FamilyValueRequestDto requestDto) {

        return ApiResponse.success(
                "Family Value created successfully.",
                familyValueService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FamilyValueResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FamilyValueRequestDto requestDto) {

        return ApiResponse.success(
                "Family Value updated successfully.",
                familyValueService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        familyValueService.softDelete(id);

        return ApiResponse.success(
                "Family Value deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        familyValueService.restore(id);

        return ApiResponse.success(
                "Family Value restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        familyValueService.hardDelete(id);

        return ApiResponse.success(
                "Family Value permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FamilyValueResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Family Value fetched successfully.",
                familyValueService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FamilyValueResponseDto>> getAll() {

        return ApiResponse.success(
                "Family Values fetched successfully.",
                familyValueService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FamilyValueResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted Family Values fetched successfully.",
                familyValueService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<FamilyValueResponseDto>> getActive() {

        return ApiResponse.success(
                "Active Family Values fetched successfully.",
                familyValueService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<FamilyValueResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive Family Values fetched successfully.",
                familyValueService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<FamilyValueResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Family Values fetched successfully.",
                familyValueService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<FamilyValueResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Family Values fetched successfully.",
                familyValueService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<FamilyValueResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Family Values fetched successfully.",
                familyValueService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<FamilyValueResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyValueService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<FamilyValueResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyValueService.searchByAdmin(adminId, keyword)
        );
    }
}