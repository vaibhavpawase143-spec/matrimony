package com.example.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDetailsResponse {

    // ===== BASIC USER =====
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private Boolean isActive;

    // ===== ROLE =====
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
    private String about;

    // ===== FAMILY =====
    private String familyType;
    private String family;
    private String brotherType;
    private String sisterType;
    private String fatherOccupation;
    private String motherOccupation;

    // ===== PREFERENCES =====
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // ===== PAYMENT =====
    private String paymentStatus;
    private String transactionId;
    private Double amount;

    // ===== AUDIT =====
    private LocalDateTime createdAt;

    // ===== CONSTRUCTOR =====
    public UserDetailsResponse(
            Long userId,
            String fullName,
            String email,
            String phone,
            Boolean isActive,
            Set<String> roles,
            String religion,
            String caste,
            String education,
            String occupation,
            String height,
            String weight,
            String city,
            String state,
            String country,
            String about,
            String familyType,
            String family,
            String brotherType,
            String sisterType,
            String fatherOccupation,
            String motherOccupation,
            Integer minAge,
            Integer maxAge,
            Double minHeight,
            Double maxHeight,
            String preferredReligion,
            String preferredCaste,
            String preferredCity,
            String planName,
            String subscriptionStatus,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentStatus,
            String transactionId,
            Double amount,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
        this.roles = roles;
        this.religion = religion;
        this.caste = caste;
        this.education = education;
        this.occupation = occupation;
        this.height = height;
        this.weight = weight;
        this.city = city;
        this.state = state;
        this.country = country;
        this.about = about;
        this.familyType = familyType;
        this.family = family;
        this.brotherType = brotherType;
        this.sisterType = sisterType;
        this.fatherOccupation = fatherOccupation;
        this.motherOccupation = motherOccupation;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.preferredReligion = preferredReligion;
        this.preferredCaste = preferredCaste;
        this.preferredCity = preferredCity;
        this.planName = planName;
        this.subscriptionStatus = subscriptionStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}