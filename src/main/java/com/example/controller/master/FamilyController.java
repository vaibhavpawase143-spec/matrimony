package com.example.controller.master;

import com.example.dto.request.FamilyRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FamilyResponseDto;
import com.example.service.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FamilyResponseDto> create(
            @Valid @RequestBody FamilyRequestDto requestDto) {

        return ApiResponse.success(
                "Family created successfully.",
                familyService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FamilyResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FamilyRequestDto requestDto) {

        return ApiResponse.success(
                "Family updated successfully.",
                familyService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        familyService.softDelete(id);

        return ApiResponse.success(
                "Family deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        familyService.restore(id);

        return ApiResponse.success(
                "Family restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        familyService.hardDelete(id);

        return ApiResponse.success(
                "Family permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FamilyResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Family fetched successfully.",
                familyService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FamilyResponseDto>> getAll() {

        return ApiResponse.success(
                "Families fetched successfully.",
                familyService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FamilyResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted families fetched successfully.",
                familyService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<FamilyResponseDto>> getActive() {

        return ApiResponse.success(
                "Active families fetched successfully.",
                familyService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<FamilyResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive families fetched successfully.",
                familyService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<FamilyResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Families fetched successfully.",
                familyService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<FamilyResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active families fetched successfully.",
                familyService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<FamilyResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive families fetched successfully.",
                familyService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<FamilyResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<FamilyResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyService.searchByAdmin(adminId, keyword)
        );
    }
}