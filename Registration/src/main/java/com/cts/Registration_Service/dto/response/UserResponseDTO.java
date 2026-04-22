package com.cts.Registration_Service.dto.response;

import com.cts.Registration_Service.enums.UserRole;
import com.cts.Registration_Service.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;
}