package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminAuditLogResponseDTO {

    private Long id;

    // Admin Information
    private Long adminId;
    private String adminName;

    // Audit Information
    private String module;
    private String action;

    // Target Information
    private String entityType;
    private Long entityId;

    // Details
    private String description;
    private String oldValue;
    private String newValue;

    // Request Information
    private String ipAddress;
    private String userAgent;

    // Audit Time
    private LocalDateTime createdAt;
}