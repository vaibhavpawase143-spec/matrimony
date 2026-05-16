package com.example.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}