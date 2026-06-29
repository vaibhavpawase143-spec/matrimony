package com.example.service;

import com.example.dto.response.AdminAuditLogResponseDTO;
import org.springframework.data.domain.Page;

public interface AdminAuditLogService {

    /**
     * Save Audit Log
     */
    void log(
            Long adminId,
            String module,
            String action,
            String entityType,
            Long entityId,
            String description,
            String oldValue,
            String newValue,
            String ipAddress,
            String userAgent
    );

    /**
     * Get Audit Logs
     */
    Page<AdminAuditLogResponseDTO> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String direction
    );

}