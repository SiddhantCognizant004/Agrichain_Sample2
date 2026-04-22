package com.cts.Registration_Service.controller;

import com.cts.Registration_Service.dto.request.LoginRequestDTO;
import com.cts.Registration_Service.dto.request.UserRequestDTO;
import com.cts.Registration_Service.dto.request.AuditLogRequestDTO;
import com.cts.Registration_Service.dto.response.UserResponseDTO;
import com.cts.Registration_Service.service.UserService;
import com.cts.Registration_Service.service.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto) {
        String token = userService.validateLogin(dto);
        String userHandle = Optional.ofNullable(dto.getEmail()).orElse("Unknown");

        // Local Audit Logging
        try {
            AuditLogRequestDTO auditDTO = AuditLogRequestDTO.builder()
                    .performedBy(userHandle)
                    .action("LOGIN")
                    .role("USER")
                    .details("User logged in successfully via Authentication endpoint")
                    .timestamp(LocalDateTime.now())
                    .build();
            auditLogService.logAction(auditDTO);
        } catch (Exception e) {
            System.err.println("Login Audit failed: " + e.getMessage());
        }

        return ResponseEntity.ok(token);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or " +
            "#id.toString() == authentication.name or " +
            "@userService.fetchUserById(#id).email == authentication.name")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updatedUser = userService.updateUser(id, dto);

        // Audit Logging for Update
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            AuditLogRequestDTO auditDTO = AuditLogRequestDTO.builder()
                    .performedBy(currentUser)
                    .action("UPDATE_USER")
                    .role("SYSTEM")
                    .details("Updated details for User ID: " + id)
                    .timestamp(LocalDateTime.now())
                    .build();
            auditLogService.logAction(auditDTO);
        } catch (Exception e) {
            System.err.println("Update Audit failed: " + e.getMessage());
        }

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'OFFICER', 'ADMIN', 'AUDITOR') or " +
            "#id.toString() == authentication.name or " +
            "@userService.fetchUserById(#id).email == authentication.name")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.fetchUserById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'OFFICER', 'ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }
}