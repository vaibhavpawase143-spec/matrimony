package com.example.controller.admin;

import com.example.dto.response.AdminAuditLogResponseDTO;
import com.example.dto.response.ApiResponse;
import com.example.service.AdminAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAuditLogController {

    private final AdminAuditLogService adminAuditLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<AdminAuditLogResponseDTO>> getAuditLogs(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction
    ) {

        return new ApiResponse<>(
                true,
                "Audit logs retrieved successfully",
                adminAuditLogService.getAuditLogs(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}