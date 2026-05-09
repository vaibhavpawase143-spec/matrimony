package com.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegisterRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 10)
    private String phone;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Basic profile fields from registration
    @Size(max = 10)
    private String gender;

    private LocalDate dateOfBirth;
}