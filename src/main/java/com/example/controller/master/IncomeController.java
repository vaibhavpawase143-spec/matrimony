package com.example.controller.master;

import com.example.dto.request.IncomeRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.IncomeResponseDTO;
import com.example.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    // =========================
    // CREATE
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<IncomeResponseDTO> create(
            @Valid @RequestBody IncomeRequestDTO requestDto) {

        return ApiResponse.success(
                "Income created successfully.",
                incomeService.create(requestDto)
        );
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ApiResponse<IncomeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequestDTO requestDto) {

        return ApiResponse.success(
                "Income updated successfully.",
                incomeService.update(id, requestDto)
        );
    }

    // =========================
    // SOFT DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        incomeService.softDelete(id);

        return ApiResponse.success(
                "Income deleted successfully.",
                null
        );
    }

    // =========================
    // RESTORE
    // =========================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        incomeService.restore(id);

        return ApiResponse.success(
                "Income restored successfully.",
                null
        );
    }

    // =========================
    // HARD DELETE
    // =========================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        incomeService.hardDelete(id);

        return ApiResponse.success(
                "Income permanently deleted.",
                null
        );
    }

    // =========================
    // GET BY ID
    // =========================

    @GetMapping("/{id}")
    public ApiResponse<IncomeResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Income fetched successfully.",
                incomeService.getById(id)
        );
    }

    // =========================
    // GET ALL
    // =========================

    @GetMapping
    public ApiResponse<List<IncomeResponseDTO>> getAll() {

        return ApiResponse.success(
                "Incomes fetched successfully.",
                incomeService.getAll()
        );
    }

    // =========================
    // GET DELETED
    // =========================

    @GetMapping("/deleted")
    public ApiResponse<List<IncomeResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted Incomes fetched successfully.",
                incomeService.getDeleted()
        );
    }

    // =========================
    // ACTIVE / INACTIVE
    // =========================

    @GetMapping("/active")
    public ApiResponse<List<IncomeResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active Incomes fetched successfully.",
                incomeService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ApiResponse<List<IncomeResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive Incomes fetched successfully.",
                incomeService.getInactive()
        );
    }

    // =========================
    // ADMIN WISE
    // =========================

    @GetMapping("/admin/{adminId}")
    public ApiResponse<List<IncomeResponseDTO>> getByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Incomes fetched successfully.",
                incomeService.getByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/active")
    public ApiResponse<List<IncomeResponseDTO>> getActiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Active Incomes fetched successfully.",
                incomeService.getActiveByAdmin(adminId)
        );
    }

    @GetMapping("/admin/{adminId}/inactive")
    public ApiResponse<List<IncomeResponseDTO>> getInactiveByAdmin(
            @PathVariable Long adminId) {

        return ApiResponse.success(
                "Inactive Incomes fetched successfully.",
                incomeService.getInactiveByAdmin(adminId)
        );
    }

    // =========================
    // SEARCH
    // =========================

    @GetMapping("/search")
    public ApiResponse<List<IncomeResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                incomeService.search(keyword)
        );
    }

    @GetMapping("/admin/{adminId}/search")
    public ApiResponse<List<IncomeResponseDTO>> searchByAdmin(
            @PathVariable Long adminId,
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                incomeService.searchByAdmin(adminId, keyword)
        );
    }
}