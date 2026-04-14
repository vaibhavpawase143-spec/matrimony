package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
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

    private Boolean isActive = true;
    private Boolean emailVerified = false;

    private LocalDateTime emailVerifiedAt;
    private LocalDateTime phoneVerifiedAt;

    // ================= AUDIT =================
    private Boolean isDeleted = false;
    private Long deletedBy;
    private LocalDateTime deletedAt;
    private Long updatedBy;

    private Boolean isOnline = false;
    private LocalDateTime lastSeen;
    private LocalDateTime lastLogin;

    // ================= ROLE =================
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ================= RELATIONS =================
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Profile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private PartnerPreference partnerPreference;

    // ================= TIMESTAMP =================
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isBlocked = false;
    private Integer reportCount = 0;

    public User() {}

    // ================= LIFECYCLE =================
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ================= CUSTOM METHODS =================
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " +
                (lastName != null ? lastName : "");
    }

    public void setId(@NotNull(message = "User ID is required") Long userId) {
        this.id = userId;
    }
}