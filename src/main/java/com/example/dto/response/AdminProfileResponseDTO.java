package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileResponseDTO {

    private Long id;

    private String name;

    private String username;

    private String email;

    private String phone;

    private String role;

    private String profilePhoto;

    private Boolean isActive;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;
}