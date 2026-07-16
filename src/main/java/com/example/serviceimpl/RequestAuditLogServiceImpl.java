package com.example.serviceimpl;

import com.example.model.RequestAuditLog;
import com.example.repository.RequestAuditLogRepository;
import com.example.service.RequestAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestAuditLogServiceImpl implements RequestAuditLogService {

    private final RequestAuditLogRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(RequestAuditLog auditLog) {
        repository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequestAuditLog> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
