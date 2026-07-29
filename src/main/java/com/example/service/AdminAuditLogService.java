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
    default void log(
            Long adminId,
            String module,
            String action,
            String entityType,
            Long entityId,
            String description,
            String oldValue,
            String newValue
    ) {

        log(
                adminId,
                module,
                action,
                entityType,
                entityId,
                description,
                oldValue,
                newValue,
                null,
                null
        );
    }
    /**
     * Get Audit Logs
     */
    Page<AdminAuditLogResponseDTO> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String direction,
            String search,
            String module,
            String action,
            Long adminId,
            java.time.LocalDate fromDate,
            java.time.LocalDate toDate
    );
}