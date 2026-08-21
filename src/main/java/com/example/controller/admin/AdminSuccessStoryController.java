package com.example.controller.admin;

import com.example.dto.request.SuccessStoryCreateRequestDTO;
import com.example.dto.request.SuccessStoryUpdateRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.dto.response.SuccessStoryResponseDTO;
import com.example.model.Admin;
import com.example.service.AdminService;
import com.example.service.SuccessStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/success-stories")
@RequiredArgsConstructor
public class AdminSuccessStoryController {

    private final SuccessStoryService successStoryService;
    private final AdminService adminService;

    // ================= GET ALL (PAGINATED & SEARCHABLE) =================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<Page<SuccessStoryResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean published,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        int maxPageSize = Math.min(size, 100);
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, maxPageSize, sort);

        Page<SuccessStoryResponseDTO> stories = successStoryService.getAdminSuccessStories(search, published, pageable);
        return new ApiResponse<>(true, "Success stories retrieved successfully", stories);
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<SuccessStoryResponseDTO> getById(@PathVariable Long id) {
        SuccessStoryResponseDTO story = successStoryService.getSuccessStoryById(id);
        return new ApiResponse<>(true, "Success story retrieved successfully", story);
    }

    // ================= CREATE =================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<SuccessStoryResponseDTO> create(@Valid @RequestBody SuccessStoryCreateRequestDTO dto) {
        Long adminId = getCurrentAdminId();
        SuccessStoryResponseDTO created = successStoryService.createSuccessStory(dto, adminId);
        return new ApiResponse<>(true, "Success story created successfully", created);
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<SuccessStoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SuccessStoryUpdateRequestDTO dto
    ) {
        Long adminId = getCurrentAdminId();
        SuccessStoryResponseDTO updated = successStoryService.updateSuccessStory(id, dto, adminId);
        return new ApiResponse<>(true, "Success story updated successfully", updated);
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<String> delete(@PathVariable Long id) {
        Long adminId = getCurrentAdminId();
        successStoryService.deleteSuccessStory(id, adminId);
        return new ApiResponse<>(true, "Success story deleted successfully", null);
    }

    // ================= PUBLISH =================

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<SuccessStoryResponseDTO> publish(@PathVariable Long id) {
        Long adminId = getCurrentAdminId();
        SuccessStoryResponseDTO story = successStoryService.publishSuccessStory(id, adminId);
        return new ApiResponse<>(true, "Success story published successfully", story);
    }

    // ================= UNPUBLISH =================

    @PatchMapping("/{id}/unpublish")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or hasAuthority('SUCCESS_STORY_MANAGE')")
    public ApiResponse<SuccessStoryResponseDTO> unpublish(@PathVariable Long id) {
        Long adminId = getCurrentAdminId();
        SuccessStoryResponseDTO story = successStoryService.unpublishSuccessStory(id, adminId);
        return new ApiResponse<>(true, "Success story unpublished successfully", story);
    }

    // ================= HELPER =================

    private Long getCurrentAdminId() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            if (email != null && !email.isBlank() && !"anonymousUser".equals(email)) {
                Admin admin = adminService.findByEmail(email);
                if (admin != null) {
                    return admin.getId();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
