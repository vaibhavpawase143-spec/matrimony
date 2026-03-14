package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "admins",
        indexes = {
                @Index(name = "idx_admin_email", columnList = "email"),
                @Index(name = "idx_admin_username", columnList = "username")
        }
)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of admin
    @Column(nullable = false)
    private String name;

    // Username for login
    @Column(nullable = false, unique = true)
    private String username;

    // Email for login and communication
    @Column(nullable = false, unique = true)
    private String email;

    // Encrypted password
    @Column(nullable = false)
    private String password;

    // Role (Super Admin, Moderator, etc.)
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Contact number
    private String phone;

    // Active / Inactive
    private Boolean status = true;

    // Last login tracking
    private LocalDateTime lastLogin;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}