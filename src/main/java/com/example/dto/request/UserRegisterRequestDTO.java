package com.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterRequestDTO {

    // =====================================================
    // BASIC DETAILS
    // =====================================================

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 10, message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(
            min = 6,
            message = "Password must be at least 6 characters"
    )
    private String password;

    // =====================================================
    // BASIC PROFILE
    // =====================================================

    // Gender Master Table ID
    private Long genderId;

    // DOB
    private LocalDate dateOfBirth;


}