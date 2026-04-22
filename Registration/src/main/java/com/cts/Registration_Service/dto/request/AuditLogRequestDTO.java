package com.cts.Registration_Service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Performed by (Email/Username) cannot be blank")
    private String performedBy;

    @NotBlank(message = "Action cannot be blank")
    private String action;

    private String role;

    private String details;

    private LocalDateTime timestamp;

    // Standard getters and setters (Lombok @Data handles these, 
    // but you can keep explicit ones if your team guidelines require it)
    
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}