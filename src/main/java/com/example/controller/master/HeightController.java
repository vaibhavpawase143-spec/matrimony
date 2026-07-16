package com.example.controller.master;

import com.example.dto.request.HeightRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.HeightResponseDTO;
import com.example.service.HeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/heights")
@RequiredArgsConstructor
public class HeightController {

    private final HeightService heightService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<HeightResponseDTO> create(
            @Valid @RequestBody HeightRequestDTO requestDto) {

        return ApiResponse.success(
                "Height created successfully.",
                heightService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<HeightResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody HeightRequestDTO requestDto) {

        return ApiResponse.success(
                "Height updated successfully.",
                heightService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        heightService.softDelete(id);

        return ApiResponse.success(
                "Height deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        heightService.restore(id);

        return ApiResponse.success(
                "Height restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        heightService.hardDelete(id);

        return ApiResponse.success(
                "Height permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<HeightResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Height fetched successfully.",
                heightService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<HeightResponseDTO>> getAll() {

        return ApiResponse.success(
                "Heights fetched successfully.",
                heightService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<HeightResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Heights fetched successfully.",
                heightService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<HeightResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Heights fetched successfully.",
                heightService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<HeightResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Heights fetched successfully.",
                heightService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<HeightResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Heights fetched successfully.",
                heightService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<HeightResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Heights fetched successfully.",
                heightService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<HeightResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Heights fetched successfully.",
                heightService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<HeightResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                heightService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<HeightResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                heightService.searchByAdmin(adminId, keyword)
        );
    }
}