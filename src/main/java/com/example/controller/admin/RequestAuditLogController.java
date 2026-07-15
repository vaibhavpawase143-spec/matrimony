package com.example.controller.admin;

import com.example.dto.response.ApiResponse;
import com.example.model.RequestAuditLog;
import com.example.service.RequestAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/request-audit-logs")
@RequiredArgsConstructor
public class RequestAuditLogController {

    private final RequestAuditLogService requestAuditLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Page<RequestAuditLog>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 200);
        Page<RequestAuditLog> logs = requestAuditLogService.findAll(
                PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "occurredAt")));
        return new ApiResponse<>(true, "Request audit logs retrieved successfully", logs);
    }
}
