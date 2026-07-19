package com.example.model;

import com.example.model.base.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "partner_preferences",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id"})
        },
        indexes = {
                @Index(name = "idx_pref_user", columnList = "user_id"),
                @Index(name = "idx_pref_religion", columnList = "religion_id"),
                @Index(name = "idx_pref_caste", columnList = "caste_id"),
                @Index(name = "idx_pref_city", columnList = "city_id")
        }
)
public class PartnerPreference extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // USER
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // =====================================================
    // AGE
    // =====================================================

    private Integer minAge;

    private Integer maxAge;

    // =====================================================
    // EDUCATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EducationLevel educationLevel;

    @Column(length = 1000)
    private String otherExpectations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Occupation occupation;

    // =====================================================
    // MARITAL
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marital_status_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MaritalStatus maritalStatus;

    // =====================================================
    // LIFESTYLE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smoking_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Smoking smoking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drinking_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Drinking drinking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Diet diet;

    // =====================================================
    // HEIGHT & WEIGHT
    // =====================================================

    private Long minHeight;

    private Long maxHeight;

    private Long minWeight;

    private Long maxWeight;

    // =====================================================
    // RELIGION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Religion religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Caste caste;

    // =====================================================
    // LOCATION
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private City city;

    // =====================================================
    // STATUS
    // =====================================================

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}