package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "family_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_id"})
        }
)
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Profile (One-to-One)
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    // 🔗 Family Type
    @ManyToOne
    @JoinColumn(name = "family_type_id")
    private FamilyType familyType;

    // 🔗 Family (FIXED)
    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    // 🔗 Brother Type
    @ManyToOne
    @JoinColumn(name = "brother_type_id")
    private BrotherType brotherType;

    // 🔗 Sister Type
    @ManyToOne
    @JoinColumn(name = "sister_type_id")
    private SisterType sisterType;

    private String fatherOccupation;
    private String motherOccupation;

    public FamilyDetails() {}

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public FamilyType getFamilyType() {
        return familyType;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public Family getFamily() {   // ✅ FIXED
        return family;
    }

    public void setFamily(Family family) {   // ✅ FIXED
        this.family = family;
    }

    public BrotherType getBrotherType() {
        return brotherType;
    }

    public void setBrotherType(BrotherType brotherType) {
        this.brotherType = brotherType;
    }

    public SisterType getSisterType() {
        return sisterType;
    }

    public void setSisterType(SisterType sisterType) {
        this.sisterType = sisterType;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }
}