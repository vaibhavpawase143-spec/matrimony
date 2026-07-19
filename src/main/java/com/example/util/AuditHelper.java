package com.example.util;

import com.example.model.Admin;
import com.example.service.AdminAuditLogService;
import com.example.service.CurrentAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditHelper {

    private final CurrentAdminService currentAdminService;
    private final AdminAuditLogService adminAuditLogService;
    private final RequestInfoUtil requestInfoUtil;
    public void logCreate(
            String module,
            String entityType,
            Long entityId,
            String entityName,
            String newValue
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                module,
                entityType + "_CREATED",
                entityType,
                entityId,
                "Created " + entityType.toLowerCase() + ": " + entityName,
                null,
                newValue,
                requestInfoUtil.getIpAddress(),
                requestInfoUtil.getUserAgent()
        );
    }
    public void logUpdate(
            String module,
            String entityType,
            Long entityId,
            String entityName,
            String oldValue,
            String newValue,
            boolean wasActive,
            boolean isActive
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        String action = entityType + "_UPDATED";
        String description = "Updated " + entityType.toLowerCase() + ": " + entityName;

        if (wasActive && !isActive) {
            action = entityType + "_DEACTIVATED";
            description = "Deactivated " + entityType.toLowerCase() + ": " + entityName;
        } else if (!wasActive && isActive) {
            action = entityType + "_ACTIVATED";
            description = "Activated " + entityType.toLowerCase() + ": " + entityName;
        }

        adminAuditLogService.log(
                currentAdmin.getId(),
                module,
                action,
                entityType,
                entityId,
                description,
                oldValue,
                newValue
        );
    }

    public void logDelete(
            String module,
            String entityType,
            Long entityId,
            String entityName,
            String oldValue
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                module,
                entityType + "_DELETED",
                entityType,
                entityId,
                "Deleted " + entityType.toLowerCase() + ": " + entityName,
                oldValue,
                null,
                requestInfoUtil.getIpAddress(),
                requestInfoUtil.getUserAgent()
        );
    }

    public void logRestore(
            String module,
            String entityType,
            Long entityId,
            String entityName,
            String newValue
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                module,
                entityType + "_RESTORED",
                entityType,
                entityId,
                "Restored " + entityType.toLowerCase() + ": " + entityName,
                null,
                newValue,
                requestInfoUtil.getIpAddress(),
                requestInfoUtil.getUserAgent()
        );
    }
    public void logHardDelete(
            String module,
            String entityType,
            Long entityId,
            String entityName,
            String oldValue
    ) {

        Admin currentAdmin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                currentAdmin.getId(),
                module,
                entityType + "_HARD_DELETED",
                entityType,
                entityId,
                "Permanently deleted " + entityType.toLowerCase() + ": " + entityName,
                oldValue,
                null,
                requestInfoUtil.getIpAddress(),
                requestInfoUtil.getUserAgent()
        );
    }

}