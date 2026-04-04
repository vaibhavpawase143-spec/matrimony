package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    // 🔥 ADD THIS FIELD (IMPORTANT)
    @Column(nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime expiryDate;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    // 🔥 NEW METHODS
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}