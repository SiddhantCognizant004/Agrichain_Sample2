package com.cts.Registration_Service.service;

import com.cts.Registration_Service.dao.UserRepo;
import com.cts.Registration_Service.dao.FarmerRepo;
import com.cts.Registration_Service.dto.request.LoginRequestDTO;
import com.cts.Registration_Service.dto.request.UserRequestDTO;
import com.cts.Registration_Service.dto.response.UserResponseDTO;
import com.cts.Registration_Service.entity.Users;
import com.cts.Registration_Service.entity.Farmer;
import com.cts.Registration_Service.enums.UserRole;
import com.cts.Registration_Service.enums.UserStatus;
import com.cts.Registration_Service.enums.FarmerStatus;
import com.cts.Registration_Service.enums.FarmerGender;
import com.cts.Registration_Service.exception.UserConflictException;
import com.cts.Registration_Service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FarmerRepo farmerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Transactional
    public String registerUser(UserRequestDTO dto) {
        userRepo.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new UserConflictException("Registration Failed: Email " + dto.getEmail() + " already exists.");
        });

        userRepo.findByPhone(dto.getPhone()).ifPresent(u -> {
            throw new UserConflictException("Registration Failed: Phone " + dto.getPhone() + " already exists.");
        });

        UserRole assignedRole = (dto.getRole() != null) ? dto.getRole() : UserRole.FARMER;

        Users user = Users.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(assignedRole)
                .status(UserStatus.ACTIVE)
                .build();

        Users savedUser = userRepo.save(user);
        String message = "Success: User account created as " + assignedRole;

        if (assignedRole == UserRole.FARMER) {
            Farmer farmer = new Farmer();
            farmer.setUsers(savedUser);
            farmer.setName(savedUser.getName());
            farmer.setEmail(savedUser.getEmail());
            farmer.setContactInfo(savedUser.getPhone());
            farmer.setDob(dto.getDob());

            if (dto.getGender() != null) {
                try {
                    farmer.setGender(FarmerGender.valueOf(dto.getGender().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new UserConflictException("Input Error: '" + dto.getGender() + "' is not a recognized gender.");
                }
            }

            farmer.setAddress(dto.getAddress());
            farmer.setLandDetails(dto.getLandDetails());
            farmer.setStatus(FarmerStatus.ACTIVE);
            farmerRepo.save(farmer);
            message += " with a synchronized Farmer profile.";
        }
        return message;
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        Users user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found.", HttpStatus.NOT_FOUND));

        // Update core user details
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        // If it's a farmer, update the linked farmer profile too
        if (user.getRole() == UserRole.FARMER) {
            farmerRepo.findByEmail(user.getEmail()).ifPresent(farmer -> {
                farmer.setName(dto.getName());
                farmer.setContactInfo(dto.getPhone());
                farmer.setAddress(dto.getAddress());
                farmer.setLandDetails(dto.getLandDetails());
                farmerRepo.save(farmer);
            });
        }

        Users updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    public String validateLogin(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (Exception e) {
            throw new UserConflictException("Authentication Failed: Incorrect email or password.");
        }

        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User account not found.", HttpStatus.NOT_FOUND));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserConflictException("Access Restricted: This account is currently inactive.");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    public UserResponseDTO fetchUserById(Long id) {
        Users user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found.", HttpStatus.NOT_FOUND));
        return mapToResponse(user);
    }

    @Transactional
    public String deactivateUser(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found.", HttpStatus.NOT_FOUND));
        user.setStatus(UserStatus.INACTIVE);
        userRepo.save(user);
        return "Success: User ID " + userId + " deactivated.";
    }

    public UserResponseDTO mapToResponse(Users user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll().stream().map(this::mapToResponse).toList();
    }
}