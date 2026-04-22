package com.cts.Registration_Service.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cts.Registration_Service.dao.FarmerRepo;
import com.cts.Registration_Service.dao.AuditLogRepo;
import com.cts.Registration_Service.entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AuditInterceptor implements HandlerInterceptor {

    @Autowired
    private AuditLogRepo auditLogRepo;

    @Autowired
    private FarmerRepo farmerRepo;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser"))
                ? auth.getName() : "GUEST";

        String role = (auth != null) ? auth.getAuthorities().toString() : "N/A";

        String details = "Farmer System Access";
        if (uri.contains("/api/farmers")) details = "Farmer Profile: " + method;
        else if (uri.contains("/api/documents")) details = "Document Management: " + method;
        else if (uri.contains("/api/crops")) details = "Crop Listing: " + method;

        // Using standard setters in case @Builder is not configured
        AuditLog log = new AuditLog();
        log.setAction(method + " " + uri);
        log.setPerformedBy(email);
        log.setRole(role);
        log.setDetails(details + " | Status: " + response.getStatus());
        log.setTimestamp(LocalDateTime.now());

        auditLogRepo.save(log);
    }
}