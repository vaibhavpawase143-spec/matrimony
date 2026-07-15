package com.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

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
    @Size(max = 15)
    private String phone;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}