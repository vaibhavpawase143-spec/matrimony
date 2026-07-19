package com.example.serviceimpl;

import com.example.dto.response.AdminAuditLogResponseDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.AdminAuditLog;
import com.example.repository.AdminAuditLogRepository;
import com.example.repository.AdminRepository;
import com.example.service.AdminAuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuditLogServiceImpl implements AdminAuditLogService {

    private final AdminAuditLogRepository auditLogRepository;
    private final AdminRepository adminRepository;
    private final HttpServletRequest request;

    @Override
    public void log(
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
    ) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        AdminAuditLog auditLog = new AdminAuditLog();

        auditLog.setAdmin(admin);
        auditLog.setModule(module);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDescription(description);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);

        // Ignore passed values and capture actual request details
        auditLog.setIpAddress(getClientIp());
        auditLog.setUserAgent(getUserAgent());

        auditLogRepository.save(auditLog);
    }

    private String getClientIp() {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private String getUserAgent() {

        String userAgent = request.getHeader("User-Agent");

        return (userAgent == null || userAgent.isBlank())
                ? "UNKNOWN"
                : userAgent;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminAuditLogResponseDTO> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return auditLogRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    private AdminAuditLogResponseDTO convertToDTO(AdminAuditLog log) {

        AdminAuditLogResponseDTO dto = new AdminAuditLogResponseDTO();

        dto.setId(log.getId());
        dto.setAdminId(log.getAdmin().getId());
        dto.setAdminName(log.getAdmin().getName());
        dto.setModule(log.getModule());
        dto.setAction(log.getAction());
        dto.setEntityType(log.getEntityType());
        dto.setEntityId(log.getEntityId());
        dto.setDescription(log.getDescription());
        dto.setOldValue(log.getOldValue());
        dto.setNewValue(log.getNewValue());
        dto.setIpAddress(log.getIpAddress());
        dto.setUserAgent(log.getUserAgent());
        dto.setCreatedAt(log.getCreatedAt());

        return dto;
    }
}