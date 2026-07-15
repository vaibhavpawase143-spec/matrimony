package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "phone_verification_otps")
public class PhoneVerificationOTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String otp;

    private Boolean verified = false;

    private LocalDateTime expiryDate;

    private LocalDateTime createdAt;

    private Integer attemptCount = 0;

    // ================= CONSTRUCTORS =================
    public PhoneVerificationOTP() {
        this.createdAt = LocalDateTime.now();
    }

    public PhoneVerificationOTP(String phone, String otp) {
        this.phone = phone;
        this.otp = otp;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
        this.attemptCount = 0;
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    // ================= CUSTOM METHODS =================
    public boolean isExpired() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isMaxAttemptsReached() {
        return attemptCount >= 5;
    }

    public void incrementAttempt() {
        this.attemptCount++;
    }
}

