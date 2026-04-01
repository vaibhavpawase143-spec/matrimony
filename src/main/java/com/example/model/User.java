package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        },
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // 🔥 EMAIL VERIFICATION (NEW)
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    private LocalDateTime emailVerifiedAt;
    private LocalDateTime phoneVerifiedAt;

    // 🔐 ROLES
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    // ================= LIFECYCLE =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.isActive == null) {
            this.isActive = true;
        }

        if (this.emailVerified == null) {
            this.emailVerified = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================= HELPERS =================

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " +
                (lastName != null ? lastName : "");
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void setRole(Role role) {
        this.roles.clear();
        this.roles.add(role);
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Boolean getIsActive() { return isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getEmailVerified() { return emailVerified; }

    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getEmailVerifiedAt() { return emailVerifiedAt; }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) { this.emailVerifiedAt = emailVerifiedAt; }

    public LocalDateTime getPhoneVerifiedAt() { return phoneVerifiedAt; }

    public void setPhoneVerifiedAt(LocalDateTime phoneVerifiedAt) { this.phoneVerifiedAt = phoneVerifiedAt; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}