package com.example.serviceimpl;

import com.example.dto.response.AdminAuditLogResponseDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Admin;
import com.example.model.AdminAuditLog;
import com.example.repository.AdminAuditLogRepository;
import com.example.repository.AdminRepository;
import com.example.service.AdminAuditLogService;
import com.example.specification.AdminAuditLogSpecification;
import com.example.util.LogSanitizer;
import com.example.security.ratelimit.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuditLogServiceImpl implements AdminAuditLogService {

    private final AdminAuditLogRepository auditLogRepository;
    private final AdminRepository adminRepository;
    private final HttpServletRequest request;
    private final ClientIpResolver clientIpResolver;

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
        auditLog.setModule(LogSanitizer.sanitizeAndTruncate(module, 100));
        auditLog.setAction(LogSanitizer.sanitizeAndTruncate(action, 100));
        auditLog.setEntityType(LogSanitizer.sanitizeAndTruncate(entityType, 100));
        auditLog.setEntityId(entityId);
        auditLog.setDescription(LogSanitizer.sanitize(description));
        auditLog.setOldValue(LogSanitizer.sanitize(oldValue));
        auditLog.setNewValue(LogSanitizer.sanitize(newValue));

        // Capture actual request details
        String resolvedIp = (ipAddress != null && !ipAddress.isBlank()) ? ipAddress : getClientIp();
        String resolvedUserAgent = (userAgent != null && !userAgent.isBlank()) ? userAgent : getUserAgent();

        auditLog.setIpAddress(LogSanitizer.sanitizeAndTruncate(resolvedIp, 45));
        auditLog.setUserAgent(LogSanitizer.sanitizeAndTruncate(resolvedUserAgent, 1000));

        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminAuditLogResponseDTO> getAuditLogs(
            int page,
            int size,
            String sortBy,
            String direction,
            String search,
            String module,
            String action,
            Long adminId,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<AdminAuditLog> specification = Specification.<AdminAuditLog>where(null);

        specification = specification.and(AdminAuditLogSpecification.hasSearch(search));
        specification = specification.and(AdminAuditLogSpecification.hasModule(module));
        specification = specification.and(AdminAuditLogSpecification.hasAction(action));
        specification = specification.and(AdminAuditLogSpecification.hasAdmin(adminId));
        specification = specification.and(AdminAuditLogSpecification.createdBetween(fromDate, toDate));

        return auditLogRepository
                .findAll(specification, pageable)
                .map(this::convertToDTO);
    }

    private String getClientIp() {
        return clientIpResolver != null ? clientIpResolver.resolveClientIp(request) : request.getRemoteAddr();
    }

    private String getUserAgent() {
        String userAgent = request.getHeader("User-Agent");
        return (userAgent == null || userAgent.isBlank()) ? "UNKNOWN" : userAgent;
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