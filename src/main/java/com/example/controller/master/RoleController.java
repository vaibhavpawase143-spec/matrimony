package com.example.controller.master;

import com.example.dto.request.RoleRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.RoleResponseDTO;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RoleResponseDTO> create(
            @Valid @RequestBody RoleRequestDTO requestDto) {

        return ApiResponse.success(
                "Role created successfully.",
                roleService.create(requestDto)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ApiResponse<RoleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO requestDto) {

        return ApiResponse.success(
                "Role updated successfully.",
                roleService.update(id, requestDto)
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDelete(@PathVariable Long id) {

        roleService.softDelete(id);

        return ApiResponse.success(
                "Role deleted successfully.",
                null
        );
    }

    // =====================================================
    // RESTORE
    // =====================================================

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restore(@PathVariable Long id) {

        roleService.restore(id);

        return ApiResponse.success(
                "Role restored successfully.",
                null
        );
    }

    // =====================================================
    // HARD DELETE
    // =====================================================

    @DeleteMapping("/{id}/hard")
    public ApiResponse<Void> hardDelete(@PathVariable Long id) {

        roleService.hardDelete(id);

        return ApiResponse.success(
                "Role permanently deleted successfully.",
                null
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ApiResponse<RoleResponseDTO> getById(@PathVariable Long id) {

        return ApiResponse.success(
                "Role fetched successfully.",
                roleService.getById(id)
        );
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ApiResponse<List<RoleResponseDTO>> getAll() {

        return ApiResponse.success(
                "Role list fetched successfully.",
                roleService.getAll()
        );
    }

    // =====================================================
    // GET DELETED
    // =====================================================

    @GetMapping("/deleted")
    public ApiResponse<List<RoleResponseDTO>> getDeleted() {

        return ApiResponse.success(
                "Deleted role list fetched successfully.",
                roleService.getDeleted()
        );
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    @GetMapping("/active")
    public ApiResponse<List<RoleResponseDTO>> getActive() {

        return ApiResponse.success(
                "Active role list fetched successfully.",
                roleService.getActive()
        );
    }

    // =====================================================
    // INACTIVE
    // =====================================================

    @GetMapping("/inactive")
    public ApiResponse<List<RoleResponseDTO>> getInactive() {

        return ApiResponse.success(
                "Inactive role list fetched successfully.",
                roleService.getInactive()
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @GetMapping("/search")
    public ApiResponse<List<RoleResponseDTO>> search(
            @RequestParam String keyword) {

        return ApiResponse.success(
                "Search completed successfully.",
                roleService.search(keyword)
        );
    }
}