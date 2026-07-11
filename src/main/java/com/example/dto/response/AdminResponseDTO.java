package com.example.dto.response;

import java.time.LocalDateTime;

public class AdminResponseDTO {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private Boolean isActive;

    // 🔥 NEW FIELD
    private String role;

    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    // ================= GETTERS =================

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public Boolean getIsActive() { return isActive; }

    public String getRole() { return role; } // 🔥 NEW

    public LocalDateTime getLastLogin() { return lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // ================= SETTERS =================

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setUsername(String username) { this.username = username; }

    public void setEmail(String email) { this.email = email; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public void setRole(String role) { this.role = role; } // 🔥 NEW

    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}