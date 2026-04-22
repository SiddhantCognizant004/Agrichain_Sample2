package com.cts.Registration_Service.controller;

import jakarta.validation.Valid;
import com.cts.Registration_Service.dto.request.AuditLogRequestDTO;
import com.cts.Registration_Service.entity.AuditLog;
import com.cts.Registration_Service.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    /**
     * ✅ GET ALL LOGS (Paginated)
     * Usage: /api/logs/GetAllLogs?page=0&size=10&sortBy=timestamp
     */
    @GetMapping("/GetAllLogs")
    public Page<AuditLog> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy) {

        // Use PageRequest to create the pagination and sorting rules
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return auditLogService.getAllLogs(pageable);
    }

    /**
     * ✅ CREATE LOG
     * This allows manual logging if needed from the frontend
     */
    @PostMapping("/CreateLog")
    public void createLog(@Valid @RequestBody AuditLogRequestDTO requestDTO) {
        // We call the service method that handles the DTO conversion
        auditLogService.logAction(requestDTO);
    }
}