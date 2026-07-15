package com.example.controller.master;

import com.example.dto.request.FamilyStatusRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.FamilyStatusResponseDto;
import com.example.service.FamilyStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/family-status")
@RequiredArgsConstructor
public class FamilyStatusController {

    private final FamilyStatusService familyStatusService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FamilyStatusResponseDto> create(
            @Valid @RequestBody FamilyStatusRequestDto requestDto) {

        return ApiResponse.success(
                "Family Status created successfully.",
                familyStatusService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<FamilyStatusResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FamilyStatusRequestDto requestDto) {

        return ApiResponse.success(
                "Family Status updated successfully.",
                familyStatusService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        familyStatusService.softDelete(id);

        return ApiResponse.success(
                "Family Status deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        familyStatusService.restore(id);

        return ApiResponse.success(
                "Family Status restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        familyStatusService.hardDelete(id);

        return ApiResponse.success(
                "Family Status permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<FamilyStatusResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Family Status fetched successfully.",
                familyStatusService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<FamilyStatusResponseDto>> getAll() {

        return ApiResponse.success(
                "Family Statuses fetched successfully.",
                familyStatusService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<FamilyStatusResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted Family Statuses fetched successfully.",
                familyStatusService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<FamilyStatusResponseDto>> getActive() {

        return ApiResponse.success(
                "Active Family Statuses fetched successfully.",
                familyStatusService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<FamilyStatusResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive Family Statuses fetched successfully.",
                familyStatusService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<FamilyStatusResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Family Statuses fetched successfully.",
                familyStatusService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<FamilyStatusResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Family Statuses fetched successfully.",
                familyStatusService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<FamilyStatusResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Family Statuses fetched successfully.",
                familyStatusService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<FamilyStatusResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyStatusService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<FamilyStatusResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                familyStatusService.searchByAdmin(adminId, keyword)
        );
    }
}