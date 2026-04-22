package com.cts.Registration_Service.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogResponseDTO {
    private Long id;
    private String performedBy;
    private String action;
    private String resource;
    private LocalDateTime timestamp;
}