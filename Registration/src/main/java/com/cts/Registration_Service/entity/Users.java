package com.cts.Registration_Service.entity;

import com.cts.Registration_Service.enums.UserRole;
import com.cts.Registration_Service.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore // Security: Never send password hashes in JSON
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 10)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}