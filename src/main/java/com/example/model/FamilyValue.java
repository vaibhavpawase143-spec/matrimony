package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "family_values",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "admin_id"})
        },
        indexes = {
                @Index(name = "idx_family_value_name", columnList = "name")
        }
)
public class FamilyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // ✅ Required because repo uses it
    @Column(name = "is_active")
    private Boolean isActive = true;

    // ⚠️ Keep Long adminId (as per your schema)
    @Column(name = "admin_id")
    private Long adminId;

    public FamilyValue() {}

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}