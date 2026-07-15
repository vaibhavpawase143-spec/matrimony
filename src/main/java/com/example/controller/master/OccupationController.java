package com.example.controller.master;

import com.example.dto.request.OccupationRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.OccupationResponseDTO;
import com.example.service.OccupationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/occupations")
@RequiredArgsConstructor
public class OccupationController {

    private final OccupationService occupationService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OccupationResponseDTO> create(
            @Valid @RequestBody OccupationRequestDTO requestDto) {

        return ApiResponse.success(
                "Occupation created successfully.",
                occupationService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<OccupationResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody OccupationRequestDTO requestDto) {

        return ApiResponse.success(
                "Occupation updated successfully.",
                occupationService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        occupationService.softDelete(id);

        return ApiResponse.success(
                "Occupation deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        occupationService.restore(id);

        return ApiResponse.success(
                "Occupation restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        occupationService.hardDelete(id);

        return ApiResponse.success(
                "Occupation permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<OccupationResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Occupation fetched successfully.",
                occupationService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<OccupationResponseDTO>> getAll() {

        return ApiResponse.success(
                "Occupation list fetched successfully.",
                occupationService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<OccupationResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Occupation list fetched successfully.",
                occupationService.getDeleted()
        );
    }

    // =========================
    // ACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<OccupationResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Occupation list fetched successfully.",
                occupationService.getActive()
        );
    }

    // =========================
    // INACTIVE
    // =========================

    @GetMapping("/inactive")
    public ApiResponse<List<OccupationResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Occupation list fetched successfully.",
                occupationService.getInactive()
        );
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<OccupationResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Occupation list fetched successfully.",
                occupationService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<OccupationResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Occupation list fetched successfully.",
                occupationService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<OccupationResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Occupation list fetched successfully.",
                occupationService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<OccupationResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                occupationService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<OccupationResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                occupationService.searchByAdmin(adminId, keyword)
        );
    }
}