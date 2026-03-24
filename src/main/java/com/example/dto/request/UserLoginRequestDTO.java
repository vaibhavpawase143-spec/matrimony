package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}