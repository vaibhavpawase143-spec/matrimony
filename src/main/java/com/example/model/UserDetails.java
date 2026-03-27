package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== USER =====
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private Boolean isActive;

    // ===== ROLE =====
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_details_roles", joinColumns = @JoinColumn(name = "user_details_id"))
    @Column(name = "role")
    private Set<String> roles;

    // ===== PROFILE =====
    private String religion;
    private String caste;
    private String education;
    private String occupation;
    private String height;
    private String weight;
    private String city;
    private String state;
    private String country;

    @Column(length = 1000)
    private String about;

    // ===== FAMILY =====
    private String familyType;
    private String family;
    private String brotherType;
    private String sisterType;
    private String fatherOccupation;
    private String motherOccupation;

    // ===== PREFERENCE =====
    private Integer minAge;
    private Integer maxAge;
    private Double minHeight;
    private Double maxHeight;
    private String preferredReligion;
    private String preferredCaste;
    private String preferredCity;

    // ===== SUBSCRIPTION =====
    private String planName;
    private String subscriptionStatus;

    // ===== PAYMENT =====
    private String paymentStatus;

    // ===== AUDIT =====
    private LocalDateTime createdAt;

    // ===== CONSTRUCTOR =====
    public UserDetails() {}

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getCaste() { return caste; }
    public void setCaste(String caste) { this.caste = caste; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public String getFamilyType() { return familyType; }
    public void setFamilyType(String familyType) { this.familyType = familyType; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getBrotherType() { return brotherType; }
    public void setBrotherType(String brotherType) { this.brotherType = brotherType; }

    public String getSisterType() { return sisterType; }
    public void setSisterType(String sisterType) { this.sisterType = sisterType; }

    public String getFatherOccupation() { return fatherOccupation; }
    public void setFatherOccupation(String fatherOccupation) { this.fatherOccupation = fatherOccupation; }

    public String getMotherOccupation() { return motherOccupation; }
    public void setMotherOccupation(String motherOccupation) { this.motherOccupation = motherOccupation; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public Double getMinHeight() { return minHeight; }
    public void setMinHeight(Double minHeight) { this.minHeight = minHeight; }

    public Double getMaxHeight() { return maxHeight; }
    public void setMaxHeight(Double maxHeight) { this.maxHeight = maxHeight; }

    public String getPreferredReligion() { return preferredReligion; }
    public void setPreferredReligion(String preferredReligion) { this.preferredReligion = preferredReligion; }

    public String getPreferredCaste() { return preferredCaste; }
    public void setPreferredCaste(String preferredCaste) { this.preferredCaste = preferredCaste; }

    public String getPreferredCity() { return preferredCity; }
    public void setPreferredCity(String preferredCity) { this.preferredCity = preferredCity; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}