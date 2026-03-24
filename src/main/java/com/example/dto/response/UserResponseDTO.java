package com.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phone;

    private Boolean isActive;

    private Boolean emailVerified;
    private Boolean phoneVerified;

    private Set<String> roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}