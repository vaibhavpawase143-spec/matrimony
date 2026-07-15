package com.example.controller.master;

import com.example.dto.request.MaritalStatusRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.MaritalStatusResponseDTO;
import com.example.service.MaritalStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/marital-status")
@RequiredArgsConstructor
public class MaritalStatusController {

    private final MaritalStatusService maritalStatusService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MaritalStatusResponseDTO> create(
            @Valid @RequestBody MaritalStatusRequestDTO requestDto) {

        return ApiResponse.success(
                "Marital Status created successfully.",
                maritalStatusService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<MaritalStatusResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MaritalStatusRequestDTO requestDto) {

        return ApiResponse.success(
                "Marital Status updated successfully.",
                maritalStatusService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        maritalStatusService.softDelete(id);

        return ApiResponse.success(
                "Marital Status deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        maritalStatusService.restore(id);

        return ApiResponse.success(
                "Marital Status restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        maritalStatusService.hardDelete(id);

        return ApiResponse.success(
                "Marital Status permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<MaritalStatusResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Marital Status fetched successfully.",
                maritalStatusService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<MaritalStatusResponseDTO>> getAll() {

        return ApiResponse.success(
                "Marital Status list fetched successfully.",
                maritalStatusService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<MaritalStatusResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Marital Status fetched successfully.",
                maritalStatusService.getDeleted()
        );
    }

    // =========================
    // ACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<MaritalStatusResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Marital Status fetched successfully.",
                maritalStatusService.getActive()
        );
    }

    // =========================
    // INACTIVE
    // =========================

    @GetMapping("/inactive")
    public ApiResponse<List<MaritalStatusResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Marital Status fetched successfully.",
                maritalStatusService.getInactive()
        );
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<MaritalStatusResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Marital Status fetched successfully.",
                maritalStatusService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<MaritalStatusResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Marital Status fetched successfully.",
                maritalStatusService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<MaritalStatusResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Marital Status fetched successfully.",
                maritalStatusService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<MaritalStatusResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                maritalStatusService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<MaritalStatusResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                maritalStatusService.searchByAdmin(adminId, keyword)
        );
    }
}