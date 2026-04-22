package com.cts.Registration_Service.dao;

import com.cts.Registration_Service.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

    // Find logs by the email stored in 'performedBy'
    List<AuditLog> findByPerformedBy(String email);

    // If you want to find logs by action type
    List<AuditLog> findByAction(String action);
}
