package com.example.controller.master;

import com.example.dto.request.EmployedRequestDto;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.EmployedResponseDto;
import com.example.service.EmployedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/employed")
@RequiredArgsConstructor
public class EmployedController {

    private final EmployedService employedService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployedResponseDto> create(
            @Valid @RequestBody EmployedRequestDto requestDto) {

        return ApiResponse.success(
                "Employed created successfully.",
                employedService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<EmployedResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployedRequestDto requestDto) {

        return ApiResponse.success(
                "Employed updated successfully.",
                employedService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        employedService.softDelete(id);

        return ApiResponse.success(
                "Employed deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        employedService.restore(id);

        return ApiResponse.success(
                "Employed restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        employedService.hardDelete(id);

        return ApiResponse.success(
                "Employed permanently deleted.",
                null
        );
    }
    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<EmployedResponseDto> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Employed fetched successfully.",
                employedService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<EmployedResponseDto>> getAll() {

        return ApiResponse.success(
                "Employed records fetched successfully.",
                employedService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<EmployedResponseDto>> getDeleted() {

        return ApiResponse.success(
                "Deleted employed records fetched successfully.",
                employedService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<EmployedResponseDto>> getActive() {

        return ApiResponse.success(
                "Active employed records fetched successfully.",
                employedService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<EmployedResponseDto>> getInactive() {

        return ApiResponse.success(
                "Inactive employed records fetched successfully.",
                employedService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<EmployedResponseDto>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Employed records fetched successfully.",
                employedService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<EmployedResponseDto>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active employed records fetched successfully.",
                employedService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<EmployedResponseDto>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive employed records fetched successfully.",
                employedService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<EmployedResponseDto>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                employedService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<EmployedResponseDto>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                employedService.searchByAdmin(adminId, keyword)
        );
    }
}