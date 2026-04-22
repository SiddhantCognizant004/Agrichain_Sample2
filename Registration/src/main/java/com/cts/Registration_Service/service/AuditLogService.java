package com.cts.Registration_Service.service;

import com.cts.Registration_Service.dao.AuditLogRepo;
import com.cts.Registration_Service.dto.request.AuditLogRequestDTO;
import com.cts.Registration_Service.entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepo auditLogRepo;

    /**
     * Maps DTO to Entity and saves to DB.
     * Used by UserController and AuditLogController.
     */
    @Transactional
    public AuditLog logAction(AuditLogRequestDTO dto) {
        AuditLog entity = new AuditLog();
        entity.setPerformedBy(dto.getPerformedBy());
        entity.setAction(dto.getAction());
        entity.setRole(dto.getRole());
        entity.setDetails(dto.getDetails());

        // Handle timestamp logic
        entity.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());

        return auditLogRepo.save(entity);
    }

    /**
     * Overloaded method to support direct Entity logging if needed
     * (e.g., from the AuditInterceptor).
     */
    @Transactional
    public void logAction(AuditLog entity) {
        if (entity.getTimestamp() == null) {
            entity.setTimestamp(LocalDateTime.now());
        }
        auditLogRepo.save(entity);
    }

    /**
     * Fetches paginated logs for the AuditLogController.
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepo.findAll(pageable);
    }
}