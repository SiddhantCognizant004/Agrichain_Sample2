package com.cts.Registration_Service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String username; // Added this
    private String email;
    private String password;
}