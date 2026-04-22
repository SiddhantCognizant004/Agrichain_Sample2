package com.cts.Registration_Service.dto.request;

import com.cts.Registration_Service.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Validation Error: Name cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Input Error: Name must contain only alphabets and spaces.")
    private String name;

    @NotBlank(message = "Contact Error: Email cannot be blank.")
    @Email(message = "Format Error: Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Contact Error: Phone number cannot be blank.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Format Error: Phone number must be exactly 10 digits.")
    private String phone;

    @NotBlank(message = "Security Error: Password cannot be blank.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$",
            message = "Security Policy: Password must be 6+ characters with a mix of uppercase, lowercase, numbers, and symbols."
    )
    private String password;

    // Removed @NotNull to allow the Service to handle the default 'FARMER' role
    private UserRole role;

    // Optional fields for Farmer registration
    private LocalDate dob;
    private String gender;
    private String address;
    private String landDetails;
}