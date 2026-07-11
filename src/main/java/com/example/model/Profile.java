package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
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
public class Profile {

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
    @JoinColumn(name="profile_type_id")
    private ProfileType profileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="manglik_status_id")
    private ManglikStatus manglikStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_type_id")
    private FamilyType familyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_status_id")
    private FamilyStatus familyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="family_value_id")
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
    @JoinColumn(name="qualification_id")
    private Qualification qualification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_of_study_id")
    private FieldOfStudy fieldOfStudy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employed_id")
    private Employed employed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="disability_status_id")
    private DisabilityStatus disabilityStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blood_group_id")
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
    // PARTNER PREFERENCE
    // =====================================================


    // =====================================================
    // SYSTEM
    // =====================================================

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "current_step")
    private Integer currentStep = 1;

    @Column(name = "profile_completed")
    private Boolean profileCompleted = false;

    @Column(name = "is_premium")
    private Boolean isPremium = false;
    @Enumerated(EnumType.STRING)
    @Column(name = "premium_plan")
    private PremiumPlan premiumPlan = PremiumPlan.FREE;

    @Column(name = "premium_start_date")
    private LocalDateTime premiumStartDate;

    @Column(name = "premium_end_date")
    private LocalDateTime premiumEndDate;
    @Column(name = "boost_score")
    private Integer boostScore = 0;

    // =====================================================
    // AUDIT
    // =====================================================

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Profile() {
    }

    // =====================================================
    // LIFECYCLE
    // =====================================================

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

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
        this.updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Caste getCaste() {
        return caste;
    }

    public void setCaste(Caste caste) {
        this.caste = caste;
    }

    public SubCaste getSubCaste() {
        return subCaste;
    }

    public void setSubCaste(SubCaste subCaste) {
        this.subCaste = subCaste;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public MotherTongue getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(MotherTongue motherTongue) {
        this.motherTongue = motherTongue;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public EducationLevel getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Complexion getComplexion() {
        return complexion;
    }

    public void setComplexion(Complexion complexion) {
        this.complexion = complexion;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }





    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Smoking getSmoking() {
        return smoking;
    }

    public void setSmoking(Smoking smoking) {
        this.smoking = smoking;
    }

    public Drinking getDrinking() {
        return drinking;
    }

    public void setDrinking(Drinking drinking) {
        this.drinking = drinking;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public Integer getSiblingsCount() {
        return siblingsCount;
    }

    public void setSiblingsCount(Integer siblingsCount) {
        this.siblingsCount = siblingsCount;
    }


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Boolean getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(Boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean premium) {
        isPremium = premium;
    }

    public Integer getBoostScore() {
        return boostScore;
    }

    public void setBoostScore(Integer boostScore) {
        this.boostScore = boostScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public PremiumPlan getPremiumPlan() {
        return premiumPlan;
    }

    public void setPremiumPlan(PremiumPlan premiumPlan) {
        this.premiumPlan = premiumPlan;
    }

    public LocalDateTime getPremiumStartDate() {
        return premiumStartDate;
    }

    public void setPremiumStartDate(LocalDateTime premiumStartDate) {
        this.premiumStartDate = premiumStartDate;
    }

    public LocalDateTime getPremiumEndDate() {
        return premiumEndDate;
    }

    public void setPremiumEndDate(LocalDateTime premiumEndDate) {
        this.premiumEndDate = premiumEndDate;
    }
}