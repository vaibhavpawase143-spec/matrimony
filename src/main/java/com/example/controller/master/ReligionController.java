package com.example.controller.master;

import com.example.dto.request.ReligionRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.ReligionResponseDTO;
import com.example.security.SecurityUtils;
import com.example.service.AdminService;
import com.example.service.ReligionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Religion Master Data Management
 * Provides endpoints for CRUD operations, search, and filtering.
 * 
 * Endpoints:
 * - GET /api/religions - Get all active religions (public)
 * - GET /api/religions/{id} - Get religion by ID (public)
 * - POST /api/religions - Create religion (admin only)
 * - PUT /api/religions/{id} - Update religion (admin only)
 * - DELETE /api/religions/{id} - Soft delete religion (admin only)
 */
@RestController
@RequestMapping("/api/religions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ReligionController {

    private final ReligionService religionService;
    private final AdminService adminService;

    // ================= PUBLIC ENDPOINTS =================

    /**
     * Get all active religions
     * Public endpoint - no authentication required
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getAll() {
        log.debug("Fetching all active religions");
        
        List<ReligionResponseDTO> religions = religionService.getAll();
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * Get religion by ID
     * Public endpoint - returns only if active and not deleted
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReligionResponseDTO>> getById(@PathVariable Long id) {
        log.debug("Fetching religion by ID: {}", id);
        
        ReligionResponseDTO religion = religionService.getById(id);
        
        return ResponseEntity.ok(
                ApiResponse.<ReligionResponseDTO>builder()
                        .success(true)
                        .message("Religion fetched successfully")
                        .data(religion)
                        .build()
        );
    }

    // ================= ADMIN ENDPOINTS =================

    /**
     * Create new religion
     * Admin-only endpoint
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReligionResponseDTO>> create(@Valid @RequestBody ReligionRequestDTO dto) {
        log.info("Creating new religion: {}", dto.getName());
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        ReligionResponseDTO created = religionService.create(dto, currentAdminId);
        
        log.info("Religion created successfully with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ReligionResponseDTO>builder()
                                .success(true)
                                .message("Religion created successfully")
                                .data(created)
                                .build()
                );
    }

    /**
     * Update existing religion
     * Admin-only endpoint
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReligionResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ReligionRequestDTO dto) {
        
        log.info("Updating religion ID: {}", id);
        
        ReligionResponseDTO updated = religionService.update(id, dto);
        
        log.info("Religion updated successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<ReligionResponseDTO>builder()
                        .success(true)
                        .message("Religion updated successfully")
                        .data(updated)
                        .build()
        );
    }

    /**
     * Soft delete religion
     * Admin-only endpoint
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        log.info("Deleting religion ID: {}", id);
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        religionService.delete(id, currentAdminId);
        
        log.info("Religion soft deleted successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Religion deleted successfully")
                        .data("Religion ID: " + id)
                        .build()
        );
    }

    /**
     * Restore soft-deleted religion
     * Admin-only endpoint
     */
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ReligionResponseDTO>> restore(@PathVariable Long id) {
        log.info("Restoring religion ID: {}", id);
        
        ReligionResponseDTO restored = religionService.restore(id);
        
        log.info("Religion restored successfully with ID: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<ReligionResponseDTO>builder()
                        .success(true)
                        .message("Religion restored successfully")
                        .data(restored)
                        .build()
        );
    }

    // ================= ADMIN SEARCH & FILTER ENDPOINTS =================

    /**
     * Get all religions for authenticated admin
     * Admin-only endpoint
     */
    @GetMapping("/admin/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getForAdmin() {
        log.debug("Fetching religions for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<ReligionResponseDTO> religions = religionService.getByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * Get active religions for authenticated admin
     * Admin-only endpoint
     */
    @GetMapping("/admin/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getActiveForAdmin() {
        log.debug("Fetching active religions for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<ReligionResponseDTO> religions = religionService.getActiveByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Active religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * Get inactive religions for authenticated admin
     * Admin-only endpoint
     */
    @GetMapping("/admin/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getInactiveForAdmin() {
        log.debug("Fetching inactive religions for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<ReligionResponseDTO> religions = religionService.getInactiveByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Inactive religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * Get deleted religions for authenticated admin
     * Admin-only endpoint
     */
    @GetMapping("/admin/deleted")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getDeletedForAdmin() {
        log.debug("Fetching deleted religions for authenticated admin");
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<ReligionResponseDTO> religions = religionService.getDeletedByAdmin(currentAdminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Deleted religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * Search religions for authenticated admin
     * Admin-only endpoint
     * 
     * @param keyword Search keyword
     */
    @GetMapping("/admin/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> search(@RequestParam(required = false) String keyword) {
        log.debug("Searching religions with keyword: {}", keyword);
        
        String currentEmail = SecurityUtils.getCurrentUsername();
        Long currentAdminId = adminService.findByEmail(currentEmail).getId();
        
        List<ReligionResponseDTO> religions = religionService.searchByAdmin(currentAdminId, keyword);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Search completed successfully")
                        .data(religions)
                        .build()
        );
    }

    // ================= LEGACY ENDPOINTS (BACKWARD COMPATIBILITY) =================
    // These endpoints are kept for backward compatibility but use the admin-specific versions now

    /**
     * @deprecated Use /admin/list instead
     */
    @Deprecated
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getByAdminId(@PathVariable Long adminId) {
        log.debug("Fetching religions for admin (deprecated endpoint): {}", adminId);
        List<ReligionResponseDTO> religions = religionService.getByAdmin(adminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * @deprecated Use /admin/active instead
     */
    @Deprecated
    @GetMapping("/admin/{adminId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getActiveByAdminId(@PathVariable Long adminId) {
        log.debug("Fetching active religions for admin (deprecated endpoint): {}", adminId);
        List<ReligionResponseDTO> religions = religionService.getActiveByAdmin(adminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Active religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * @deprecated Use /admin/inactive instead
     */
    @Deprecated
    @GetMapping("/admin/{adminId}/inactive")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> getInactiveByAdminId(@PathVariable Long adminId) {
        log.debug("Fetching inactive religions for admin (deprecated endpoint): {}", adminId);
        List<ReligionResponseDTO> religions = religionService.getInactiveByAdmin(adminId);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Inactive religions fetched successfully")
                        .data(religions)
                        .build()
        );
    }

    /**
     * @deprecated Use /admin/search instead
     */
    @Deprecated
    @GetMapping("/admin/{adminId}/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ReligionResponseDTO>>> searchByAdminId(
            @PathVariable Long adminId,
            @RequestParam String keyword) {
        
        log.debug("Searching religions for admin (deprecated endpoint): {} with keyword: {}", adminId, keyword);
        List<ReligionResponseDTO> religions = religionService.searchByAdmin(adminId, keyword);
        
        return ResponseEntity.ok(
                ApiResponse.<List<ReligionResponseDTO>>builder()
                        .success(true)
                        .message("Search completed successfully")
                        .data(religions)
                        .build()
        );
    }
}