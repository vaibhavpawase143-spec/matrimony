package com.example.controller.master;

import com.example.dto.request.ManglikStatusRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ManglikStatusResponseDTO;
import com.example.service.ManglikStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/manglik-status")
@RequiredArgsConstructor
public class ManglikStatusController {

    private final ManglikStatusService manglikStatusService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ManglikStatusResponseDTO> create(
            @Valid @RequestBody ManglikStatusRequestDTO requestDto) {

        return ApiResponse.success(
                "Manglik Status created successfully.",
                manglikStatusService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<ManglikStatusResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ManglikStatusRequestDTO requestDto) {

        return ApiResponse.success(
                "Manglik Status updated successfully.",
                manglikStatusService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        manglikStatusService.softDelete(id);

        return ApiResponse.success(
                "Manglik Status deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        manglikStatusService.restore(id);

        return ApiResponse.success(
                "Manglik Status restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        manglikStatusService.hardDelete(id);

        return ApiResponse.success(
                "Manglik Status permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<ManglikStatusResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Manglik Status fetched successfully.",
                manglikStatusService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<ManglikStatusResponseDTO>> getAll() {

        return ApiResponse.success(
                "Manglik Status list fetched successfully.",
                manglikStatusService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<ManglikStatusResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Manglik Status fetched successfully.",
                manglikStatusService.getDeleted()
        );
    }

    // =========================
    // ACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<ManglikStatusResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Manglik Status fetched successfully.",
                manglikStatusService.getActive()
        );
    }

    // =========================
    // INACTIVE
    // =========================

    @GetMapping("/inactive")
    public ApiResponse<List<ManglikStatusResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Manglik Status fetched successfully.",
                manglikStatusService.getInactive()
        );
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<ManglikStatusResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Manglik Status fetched successfully.",
                manglikStatusService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<ManglikStatusResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Manglik Status fetched successfully.",
                manglikStatusService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<ManglikStatusResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Manglik Status fetched successfully.",
                manglikStatusService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<ManglikStatusResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                manglikStatusService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<ManglikStatusResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                manglikStatusService.searchByAdmin(adminId, keyword)
        );
    }
}