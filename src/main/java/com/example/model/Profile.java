package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "religion_id")
    private Religion religion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "caste_id")
    private Caste caste;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_caste_id")
    private SubCaste subCaste;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mother_tongue_id")
    private MotherTongue motherTongue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marital_status_id")
    private MaritalStatus maritalStatus;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "education_level_id")
    private EducationLevel educationLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "height_id")
    private Height height;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "weight_id")
    private Weight weight;

    @Column(length = 1000)
    private String about;

    @Column(name = "image_url")
    private String imageUrl;

    // Physical details
    @Column(length = 50)
    private String complexion;

    @Column(length = 50)
    private String bodyType;

    // Education & Career
    @Column(length = 100)
    private String annualIncome;

    @Column(length = 200)
    private String companyName;

    // Location
    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String state;

    @Column(length = 500)
    private String address;

    // Lifestyle
    @Column(length = 50)
    private String diet;

    @Column(length = 50)
    private String smoking;

    @Column(length = 50)
    private String drinking;

    // Family details
    @Column(length = 100)
    private String fatherName;

    @Column(length = 200)
    private String fatherOccupation;

    @Column(length = 100)
    private String motherName;

    @Column(length = 200)
    private String motherOccupation;

    @Column(length = 50)
    private String siblingsCount;

    // Partner preferences
    @Column(length = 10)
    private String preferredAgeMin;

    @Column(length = 10)
    private String preferredAgeMax;

    @Column(length = 200)
    private String preferredLocation;

    @Column(length = 200)
    private String preferredEducation;

    @Column(length = 1000)
    private String otherExpectations;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "current_step")
    private Integer currentStep = 1;

    @Column(name = "profile_completed")
    private Boolean profileCompleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Profile() {}
    // 🔥 PREMIUM / BOOST SYSTEM
    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "boost_score")
    private Integer boostScore = 0;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ FIXED GETTER
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Integer getBoostScore() {
        return boostScore;
    }

    public void setBoostScore(Integer boostScore) {
        this.boostScore = boostScore;
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

    // Getters and setters for new fields
    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
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

    public String getSiblingsCount() {
        return siblingsCount;
    }

    public void setSiblingsCount(String siblingsCount) {
        this.siblingsCount = siblingsCount;
    }

    public String getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(String preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public String getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(String preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getPreferredEducation() {
        return preferredEducation;
    }

    public void setPreferredEducation(String preferredEducation) {
        this.preferredEducation = preferredEducation;
    }

    public String getOtherExpectations() {
        return otherExpectations;
    }

    public void setOtherExpectations(String otherExpectations) {
        this.otherExpectations = otherExpectations;
    }
}