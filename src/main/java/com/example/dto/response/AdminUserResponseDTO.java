package com.example.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserResponseDTO {

    // ================= BASIC =================

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    // ================= PROFILE =================

    private String imageUrl;

    private String gender;

    private String religion;

    private String caste;

    private String city;

    // ================= PREMIUM =================

    private Boolean premium;

    private String premiumPlan;

    // ================= STATUS =================

    private Boolean active;

    private Boolean blocked;

    private Boolean deleted;

    private Boolean profileCompleted;

    // ================= VERIFICATION =================

    private Boolean emailVerified;

    private Boolean phoneVerified;

    // ================= USER ACTIVITY =================

    private Boolean online;

    private LocalDateTime lastSeen;

    private LocalDateTime lastLogin;

    // ================= AUDIT =================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}