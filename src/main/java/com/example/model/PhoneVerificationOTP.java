package com.example.model;

import jakarta.persistence.*;
import java.time.Duration;
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

    @Column(name = "target_type")
    private String targetType = "PHONE";

    @Column(name = "purpose")
    private String purpose = "VERIFICATION";

    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;

    // ================= CONSTRUCTORS =================
    public PhoneVerificationOTP() {
        this.createdAt = LocalDateTime.now();
        this.lastSentAt = LocalDateTime.now();
    }

    public PhoneVerificationOTP(String phone, String otp) {
        this.phone = phone;
        this.otp = otp;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
        this.lastSentAt = LocalDateTime.now();
        this.attemptCount = 0;
        this.targetType = "PHONE";
        this.purpose = "VERIFICATION";
    }

    public PhoneVerificationOTP(String phone, String otp, String targetType, String purpose) {
        this.phone = phone;
        this.otp = otp;
        this.targetType = targetType != null ? targetType : "PHONE";
        this.purpose = purpose != null ? purpose : "VERIFICATION";
        this.verified = false;
        this.createdAt = LocalDateTime.now();
        this.lastSentAt = LocalDateTime.now();
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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getLastSentAt() {
        return lastSentAt;
    }

    public void setLastSentAt(LocalDateTime lastSentAt) {
        this.lastSentAt = lastSentAt;
    }

    // ================= CUSTOM METHODS =================
    public boolean isExpired() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isMaxAttemptsReached() {
        return attemptCount != null && attemptCount >= 3;
    }

    public void incrementAttempt() {
        if (this.attemptCount == null) {
            this.attemptCount = 0;
        }
        this.attemptCount++;
    }

    public boolean isCooldownActive(int cooldownSeconds) {
        if (lastSentAt == null) return false;
        long elapsedSeconds = Duration.between(lastSentAt, LocalDateTime.now()).getSeconds();
        return elapsedSeconds < cooldownSeconds;
    }

    public long getRemainingCooldownSeconds(int cooldownSeconds) {
        if (lastSentAt == null) return 0;
        long elapsedSeconds = Duration.between(lastSentAt, LocalDateTime.now()).getSeconds();
        long remaining = cooldownSeconds - elapsedSeconds;
        return Math.max(0, remaining);
    }

    public long getRemainingExpirySeconds() {
        if (expiryDate == null) return 0;
        long remaining = Duration.between(LocalDateTime.now(), expiryDate).getSeconds();
        return Math.max(0, remaining);
    }
}
