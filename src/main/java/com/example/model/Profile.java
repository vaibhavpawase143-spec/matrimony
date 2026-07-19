package com.example.model;

import com.example.model.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}),
        indexes = {
                @Index(name = "idx_profile_user", columnList = "user_id"),
                @Index(name = "idx_profile_city", columnList = "city_id"),
                @Index(name = "idx_profile_caste", columnList = "caste_id"),
                @Index(name = "idx_profile_religion", columnList = "religion_id"),
                @Index(name = "idx_profile_dob", columnList = "date_of_birth"),
                @Index(name = "idx_profile_active", columnList = "is_active")
        }
)
public class Profile extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // USER
    // =====================================================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // =====================================================
    // MASTER TABLE RELATIONS
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_type_id")
    private ProfileType profileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manglik_status_id")
    private ManglikStatus manglikStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_type_id")
    private FamilyType familyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_status_id")
    private FamilyStatus familyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_value_id")
    private FamilyValue familyValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caste_id")
    private Caste caste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_caste_id")
    private SubCaste subCaste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_tongue_id")
    private MotherTongue motherTongue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_id")
    private Qualification qualification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_of_study_id")
    private FieldOfStudy fieldOfStudy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employed_id")
    private Employed employed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_status_id")
    private DisabilityStatus disabilityStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_group_id")
    private BloodGroup bloodGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marital_status_id")
    private MaritalStatus maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_level_id")
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "height_id")
    private Height height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_id")
    private Weight weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_type_id")
    private BodyType bodyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complexion_id")
    private Complexion complexion;

    // =====================================================
    // BASIC PROFILE
    // =====================================================

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 1000)
    private String about;

    @Column(length = 2000)
    private String aboutMe;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    // =====================================================
    // CAREER
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_id")
    private Income income;

    @Column(length = 200)
    private String companyName;

    // =====================================================
    // ADDRESS
    // =====================================================

    @Column(length = 500)
    private String address;

    // =====================================================
    // LIFESTYLE
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id")
    private Diet diet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smoking_id")
    private Smoking smoking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drinking_id")
    private Drinking drinking;

    // =====================================================
    // FAMILY
    // =====================================================

    @Column(length = 100)
    private String fatherName;

    @Column(length = 200)
    private String fatherOccupation;

    @Column(length = 100)
    private String motherName;

    @Column(length = 200)
    private String motherOccupation;

    @Column(name = "siblings_count")
    private Integer siblingsCount;

    // =====================================================
    // SYSTEM
    // =====================================================

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "current_step")
    @Builder.Default
    private Integer currentStep = 1;

    @Column(name = "profile_completed")
    @Builder.Default
    private Boolean profileCompleted = false;

    @Column(name = "is_premium")
    @Builder.Default
    private Boolean isPremium = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_plan")
    @Builder.Default
    private PremiumPlan premiumPlan = PremiumPlan.FREE;

    @Column(name = "premium_start_date")
    private LocalDateTime premiumStartDate;

    @Column(name = "premium_end_date")
    private LocalDateTime premiumEndDate;

    @Column(name = "boost_score")
    @Builder.Default
    private Integer boostScore = 0;

// =====================================================
// LIFECYCLE
// =====================================================
@PrePersist
protected void onCreate() {
    super.onCreate();

    if (this.isActive == null) {
        this.isActive = true;
    }

    if (this.profileCompleted == null) {
        this.profileCompleted = false;
    }

    if (this.currentStep == null) {
        this.currentStep = 1;
    }

    if (this.isPremium == null) {
        this.isPremium = false;
    }

    if (this.boostScore == null) {
        this.boostScore = 0;
    }
}

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
    }
}