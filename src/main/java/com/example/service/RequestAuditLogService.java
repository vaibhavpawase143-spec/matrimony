package com.example.service;

import com.example.model.RequestAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestAuditLogService {
    void record(RequestAuditLog auditLog);
    Page<RequestAuditLog> findAll(Pageable pageable);
}
