package com.example.serviceimpl;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.PartnerPreference;
import com.example.model.PremiumPlan;
import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.*;
import com.example.service.CacheService;
import com.example.service.MatchAsyncService;
import com.example.service.ProfileService;
import com.example.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    // =====================================================
    // REPOSITORIES
    // =====================================================

    private final ProfileRepository repository;
    private final UserRepository userRepository;

    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final SubCasteRepository subCasteRepository;

    private final EducationLevelRepository educationRepository;
    private final QualificationRepository qualificationRepository;
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final OccupationRepository occupationRepository;
    private final EmployedRepository employedRepository;

    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final BloodGroupRepository bloodGroupRepository;
    private final BodyTypeRepository bodyTypeRepository;
    private final ComplexionRepository complexionRepository;
    private final DisabilityStatusRepository disabilityStatusRepository;

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    private final MotherTongueRepository motherTongueRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final GenderRepository genderRepository;

    private final ProfileTypeRepository profileTypeRepository;
    private final ManglikStatusRepository manglikStatusRepository;
    private final FamilyTypeRepository familyTypeRepository;
    private final FamilyStatusRepository familyStatusRepository;
    private final FamilyValueRepository familyValueRepository;

    private final IncomeRepository incomeRepository;
    private final DietRepository dietRepository;
    private final SmokingRepository smokingRepository;
    private final DrinkingRepository drinkingRepository;

    private final BrotherTypeRepository brotherTypeRepository;
    private final SisterTypeRepository sisterTypeRepository;

    private final UserPhotoRepository userPhotoRepository;

    // =====================================================
    // SERVICES
    // =====================================================

    private final CacheService cacheService;
    private final MatchAsyncService asyncService;
    private final SubscriptionService subscriptionService;

    @Value("${app.base-url}")
    private String baseUrl;

    // =====================================================
    // CURRENT USER
    // =====================================================

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

// =====================================================
// CREATE PROFILE
// =====================================================

    // =====================================================
    // CREATE PROFILE
    // =====================================================

    @Override
    public ProfileResponseDTO createProfile(ProfileRequestDTO dto) {

        User user = getCurrentUser();

        if (repository.existsByUserId(user.getId())) {
            throw new RuntimeException("Profile already exists!");
        }

        Profile profile = new Profile();
        profile.setUser(user);

        // Map DTO -> Entity
        mapDtoToEntity(dto, profile);

        // Calculate profile completion
        updateProfileCompletion(profile);

        userRepository.save(user);

        Profile saved = repository.save(profile);

        // Refresh cache
       // safeRedis(user.getId());

        return mapToDTO(saved);
    }

    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    @Override
    public ProfileResponseDTO updateMyProfile(UpdateProfileRequestDTO dto) {

        User user = getCurrentUser();

        Profile profile = repository
                .findByUserIdWithRelations(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        // Update entity from DTO
        mapUpdateDto(dto, profile);

        // Recalculate completion percentage
        updateProfileCompletion(profile);

        userRepository.save(user);

        Profile saved = repository.save(profile);

        // Clear cached matches
        //safeRedis(user.getId());

        return mapToDTO(saved);
    }

    @Override
    public Optional<Profile> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Profile> getByUserId(Long userId) {
        return Optional.empty();
    }

// =====================================================
// GET MY PROFILE
// =====================================================

    // =====================================================
    // GET MY PROFILE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDTO getMyProfile() {

        Profile profile = repository
                .findByUserIdWithRelations(getCurrentUser().getId())
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        // Premium status is determined while mapping DTO
        return mapToDTO(profile);
    }

    // =====================================================
    // GET PROFILE BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileById(Long id) {

        Profile profile = repository
                .findByProfileIdWithRelations(id)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));
        // ================= SECURITY =================

        User profileUser = profile.getUser();

        if (Boolean.TRUE.equals(profileUser.getIsDeleted())
                || !Boolean.TRUE.equals(profileUser.getIsActive())
                || Boolean.TRUE.equals(profileUser.getIsBlocked())) {

            throw new RuntimeException("Profile not found");
        }

        ProfileResponseDTO dto = mapToDTO(profile);

        User currentUser = getCurrentUser();

        Profile currentProfile = repository
                .findByUserIdWithRelations(currentUser.getId())
                .orElse(null);

        // Hide contact details for non-premium users
        if (currentProfile != null
                && !subscriptionService.hasActiveSubscription(currentUser.getId())) {

            dto.setPhone(null);
            dto.setEmail(null);
        }

        return dto;
    }

    // =====================================================
    // DELETE PROFILE
    // =====================================================

    @Override
    public void delete(Long id) {

        Profile profile = repository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        if (!profile.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access Denied");
        }

        repository.delete(profile);
    }

// =====================================================
// PREMIUM
// =====================================================

    // =====================================================
    // PREMIUM
    // =====================================================

    @Override
    public void activatePremium(
            Long userId,
            PremiumPlan plan
    ) {

        Profile profile = repository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        profile.setIsPremium(true);
        profile.setBoostScore(100);
        profile.setPremiumPlan(plan);
        profile.setPremiumStartDate(java.time.LocalDateTime.now());

        switch (plan) {

            case ONE_MONTH ->
                    profile.setPremiumEndDate(
                            java.time.LocalDateTime.now().plusMonths(1));

            case THREE_MONTHS ->
                    profile.setPremiumEndDate(
                            java.time.LocalDateTime.now().plusMonths(3));

            case SIX_MONTHS ->
                    profile.setPremiumEndDate(
                            java.time.LocalDateTime.now().plusMonths(6));

            case TWELVE_MONTHS ->
                    profile.setPremiumEndDate(
                            java.time.LocalDateTime.now().plusMonths(12));

            default -> {
                profile.setIsPremium(false);
                profile.setBoostScore(0);
                profile.setPremiumEndDate(null);
            }
        }

        repository.save(profile);
    }

    // =====================================================
    // SAVE PROFILE
    // =====================================================

    @Override
    public Profile saveProfile(Profile profile) {

        User user = getCurrentUser();

        Optional<Profile> existing =
                repository.findByUserId(user.getId());

        if (existing.isPresent()) {

            Profile dbProfile = existing.get();

            if (profile.getImageUrl() != null) {
                dbProfile.setImageUrl(profile.getImageUrl());
            }

            if (profile.getAbout() != null) {
                dbProfile.setAbout(profile.getAbout());
            }

            return repository.save(dbProfile);
        }

        profile.setUser(user);

        if (profile.getIsActive() == null) {
            profile.setIsActive(true);
        }

        return repository.save(profile);
    }

// =====================================================
// GET ALL
// =====================================================
    // =====================================================
    // GET ALL PROFILES (PAGINATION)
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getAll(Pageable pageable) {

        User currentUser = getCurrentUser();

        return repository.findDiscoverProfiles(
                currentUser.getId(),
                pageable
        );
    }

    // =====================================================
    // GET PROFILE BY USER ID
    // =====================================================

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileByUserId(Long userId) {

        Profile profile = repository
                .findByUserIdWithRelations(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToDTO(profile);
    }

// =====================================================
// DTO -> ENTITY
// =====================================================

    // =====================================================
    // DTO -> ENTITY
    // PART 1 : BASIC INFORMATION
    // =====================================================

    private void mapDtoToEntity(
            ProfileRequestDTO dto,
            Profile p
    ) {

        // =====================================================
        // BASIC
        // =====================================================

        p.setDateOfBirth(dto.getDateOfBirth());
        p.setAboutMe(dto.getAboutMe());
        p.setImageUrl(dto.getImageUrl());
        p.setAbout(dto.getAbout());

        if (dto.getIncomeId() != null) {
            p.setIncome(
                    incomeRepository
                            .findById(dto.getIncomeId())
                            .orElse(null)
            );
        }

        p.setCompanyName(dto.getCompanyName());
        p.setAddress(dto.getAddress());

        // =====================================================
        // LIFESTYLE
        // =====================================================

        if (dto.getDietId() != null) {
            p.setDiet(
                    dietRepository
                            .findById(dto.getDietId())
                            .orElse(null)
            );
        }

        if (dto.getSmokingId() != null) {
            p.setSmoking(
                    smokingRepository
                            .findById(dto.getSmokingId())
                            .orElse(null)
            );
        }

        if (dto.getDrinkingId() != null) {
            p.setDrinking(
                    drinkingRepository
                            .findById(dto.getDrinkingId())
                            .orElse(null)
            );
        }

        // =====================================================
        // FAMILY
        // =====================================================

        p.setFatherName(dto.getFatherName());
        p.setFatherOccupation(dto.getFatherOccupation());
        p.setMotherName(dto.getMotherName());
        p.setMotherOccupation(dto.getMotherOccupation());
        p.setSiblingsCount(dto.getSiblingsCount());

        // =====================================================
        // UPDATE USER
        // =====================================================

        User user = p.getUser();

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        // =====================================================
        // RELIGION
        // =====================================================

        if (dto.getReligionId() != null) {
            p.setReligion(
                    religionRepository
                            .findById(dto.getReligionId())
                            .orElse(null)
            );
        }

        // =====================================================
        // CASTE
        // =====================================================

        if (dto.getCasteId() != null) {
            p.setCaste(
                    casteRepository
                            .findById(dto.getCasteId())
                            .orElse(null)
            );
        }

        // =====================================================
        // SUB CASTE
        // =====================================================

        if (dto.getSubCasteId() != null) {
            p.setSubCaste(
                    subCasteRepository
                            .findById(dto.getSubCasteId())
                            .orElse(null)
            );
        }

        // =====================================================
        // MOTHER TONGUE
        // =====================================================

        if (dto.getMotherTongueId() != null) {
            p.setMotherTongue(
                    motherTongueRepository
                            .findById(dto.getMotherTongueId())
                            .orElse(null)
            );
        }

        // =====================================================
        // MARITAL STATUS
        // =====================================================

        if (dto.getMaritalStatusId() != null) {
            p.setMaritalStatus(
                    maritalStatusRepository
                            .findById(dto.getMaritalStatusId())
                            .orElse(null)
            );
        }

        // =====================================================
        // PROFILE TYPE
        // =====================================================

        if (dto.getProfileTypeId() != null) {
            p.setProfileType(
                    profileTypeRepository
                            .findById(dto.getProfileTypeId())
                            .orElse(null)
            );
        }

        // =====================================================
        // MANGLIK STATUS
        // =====================================================

        if (dto.getManglikStatusId() != null) {
            p.setManglikStatus(
                    manglikStatusRepository
                            .findById(dto.getManglikStatusId())
                            .orElse(null)
            );
        }

        // =====================================================
        // FAMILY TYPE
        // =====================================================

        if (dto.getFamilyTypeId() != null) {
            p.setFamilyType(
                    familyTypeRepository
                            .findById(dto.getFamilyTypeId())
                            .orElse(null)
            );
        }

        // =====================================================
        // FAMILY STATUS
        // =====================================================

        if (dto.getFamilyStatusId() != null) {
            p.setFamilyStatus(
                    familyStatusRepository
                            .findById(dto.getFamilyStatusId())
                            .orElse(null)
            );
        }

        // =====================================================
        // FAMILY VALUE
        // =====================================================

        if (dto.getFamilyValueId() != null) {
            p.setFamilyValue(
                    familyValueRepository
                            .findById(dto.getFamilyValueId())
                            .orElse(null)
            );
        }

        // =====================================================
        // QUALIFICATION
        // =====================================================

        if (dto.getQualificationId() != null) {
            p.setQualification(
                    qualificationRepository
                            .findById(dto.getQualificationId())
                            .orElse(null)
            );
        }

        // =====================================================
        // FIELD OF STUDY
        // =====================================================

        if (dto.getFieldOfStudyId() != null) {
            p.setFieldOfStudy(
                    fieldOfStudyRepository
                            .findById(dto.getFieldOfStudyId())
                            .orElse(null)
            );
        }

        // =====================================================
        // EMPLOYMENT
        // =====================================================

        if (dto.getEmployedId() != null) {
            p.setEmployed(
                    employedRepository
                            .findById(dto.getEmployedId())
                            .orElse(null)
            );
        }

        // =====================================================
        // DISABILITY
        // =====================================================

        if (dto.getDisabilityStatusId() != null) {
            p.setDisabilityStatus(
                    disabilityStatusRepository
                            .findById(dto.getDisabilityStatusId())
                            .orElse(null)
            );
        }

        // =====================================================
        // BLOOD GROUP
        // =====================================================

        if (dto.getBloodGroupId() != null) {
            p.setBloodGroup(
                    bloodGroupRepository
                            .findById(dto.getBloodGroupId())
                            .orElse(null)
            );
        }

        // ===== CONTINUES IN PART 2C =====

        // =====================================================
        // GENDER
        // =====================================================

        if (dto.getGenderId() != null) {
            p.setGender(
                    genderRepository
                            .findById(dto.getGenderId())
                            .orElse(null)
            );
        }

        // =====================================================
        // EDUCATION
        // =====================================================

        if (dto.getEducationLevelId() != null) {
            p.setEducationLevel(
                    educationRepository
                            .findById(dto.getEducationLevelId())
                            .orElse(null)
            );
        }

        // =====================================================
        // OCCUPATION
        // =====================================================

        if (dto.getOccupationId() != null) {
            p.setOccupation(
                    occupationRepository
                            .findById(dto.getOccupationId())
                            .orElse(null)
            );
        }

        // =====================================================
        // HEIGHT
        // =====================================================

        if (dto.getHeightId() != null) {
            p.setHeight(
                    heightRepository
                            .findById(dto.getHeightId())
                            .orElse(null)
            );
        }

        // =====================================================
        // WEIGHT
        // =====================================================

        if (dto.getWeightId() != null) {
            p.setWeight(
                    weightRepository
                            .findById(dto.getWeightId())
                            .orElse(null)
            );
        }

        // =====================================================
        // BODY TYPE
        // =====================================================

        if (dto.getBodyTypeId() != null) {
            p.setBodyType(
                    bodyTypeRepository
                            .findById(dto.getBodyTypeId())
                            .orElse(null)
            );
        }

        // =====================================================
        // COMPLEXION
        // =====================================================

        if (dto.getComplexionId() != null) {
            p.setComplexion(
                    complexionRepository
                            .findById(dto.getComplexionId())
                            .orElse(null)
            );
        }

        // =====================================================
        // COUNTRY
        // =====================================================

        if (dto.getCountryId() != null) {
            p.setCountry(
                    countryRepository
                            .findById(dto.getCountryId())
                            .orElse(null)
            );
        }

        // =====================================================
        // STATE
        // =====================================================

        if (dto.getStateId() != null) {
            p.setState(
                    stateRepository
                            .findById(dto.getStateId())
                            .orElse(null)
            );
        }

        // =====================================================
        // CITY
        // =====================================================

        if (dto.getCityId() != null) {
            p.setCity(
                    cityRepository
                            .findById(dto.getCityId())
                            .orElse(null)
            );
        }
    }

// =====================================================
// UPDATE DTO -> ENTITY
// =====================================================

    // =====================================================
    // UPDATE DTO -> ENTITY
    // =====================================================

    private void mapUpdateDto(
            UpdateProfileRequestDTO dto,
            Profile profile
    ) {

        mapDtoToEntity(
                convertUpdateDto(dto),
                profile
        );
    }

    // =====================================================
    // UPDATE DTO CONVERTER
    // =====================================================

    private ProfileRequestDTO convertUpdateDto(
            UpdateProfileRequestDTO dto
    ) {

        ProfileRequestDTO request = new ProfileRequestDTO();

        // =====================================================
        // BASIC
        // =====================================================

        request.setFirstName(dto.getFirstName());
        request.setLastName(dto.getLastName());
        request.setEmail(dto.getEmail());
        request.setPhone(dto.getPhone());

        request.setDateOfBirth(dto.getDateOfBirth());

        request.setImageUrl(dto.getImageUrl());

        request.setAbout(dto.getAbout());

        request.setAboutMe(dto.getAboutMe());

        // =====================================================
        // CAREER
        // =====================================================

        request.setIncomeId(dto.getIncomeId());

        request.setCompanyName(dto.getCompanyName());

        // =====================================================
        // LOCATION
        // =====================================================

        request.setAddress(dto.getAddress());

        // =====================================================
        // LIFESTYLE
        // =====================================================

        request.setDietId(dto.getDietId());

        request.setSmokingId(dto.getSmokingId());

        request.setDrinkingId(dto.getDrinkingId());

        // =====================================================
        // FAMILY
        // =====================================================

        request.setFatherName(dto.getFatherName());

        request.setFatherOccupation(dto.getFatherOccupation());

        request.setMotherName(dto.getMotherName());

        request.setMotherOccupation(dto.getMotherOccupation());

        request.setSiblingsCount(dto.getSiblingsCount());

        // =====================================================
        // MASTER IDS
        // =====================================================

        request.setReligionId(dto.getReligionId());

        request.setCasteId(dto.getCasteId());

        request.setSubCasteId(dto.getSubCasteId());

        request.setMotherTongueId(dto.getMotherTongueId());

        request.setMaritalStatusId(dto.getMaritalStatusId());

        request.setProfileTypeId(dto.getProfileTypeId());

        request.setManglikStatusId(dto.getManglikStatusId());

        request.setFamilyTypeId(dto.getFamilyTypeId());

        request.setFamilyStatusId(dto.getFamilyStatusId());

        request.setFamilyValueId(dto.getFamilyValueId());

        request.setGenderId(dto.getGenderId());
        // =====================================================
        // EDUCATION
        // =====================================================

        request.setEducationLevelId(
                dto.getEducationLevelId()
        );

        request.setOccupationId(
                dto.getOccupationId()
        );

        request.setHeightId(
                dto.getHeightId()
        );

        request.setWeightId(
                dto.getWeightId()
        );

        request.setBodyTypeId(
                dto.getBodyTypeId()
        );

        request.setComplexionId(
                dto.getComplexionId()
        );

        // =====================================================
        // LOCATION
        // =====================================================

        request.setCountryId(
                dto.getCountryId()
        );

        request.setStateId(
                dto.getStateId()
        );

        request.setCityId(
                dto.getCityId()
        );

        // =====================================================
        // PROFESSIONAL DETAILS
        // =====================================================

        request.setQualificationId(
                dto.getQualificationId()
        );

        request.setFieldOfStudyId(
                dto.getFieldOfStudyId()
        );

        request.setEmployedId(
                dto.getEmployedId()
        );

        request.setDisabilityStatusId(
                dto.getDisabilityStatusId()
        );

        request.setBloodGroupId(
                dto.getBloodGroupId()
        );

        return request;
    }

    // =====================================================
    // ENTITY -> DTO
    // =====================================================

    @Override
    public ProfileResponseDTO mapToDTO(Profile profile) {

        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setId(profile.getId());

        dto.setDateOfBirth(
                profile.getDateOfBirth()
        );

        // =====================================================
        // PROFILE IMAGE
        // =====================================================

        if (profile.getImageUrl() != null
                && !profile.getImageUrl().isBlank()) {

            dto.setImageUrl(
                    baseUrl + profile.getImageUrl()
            );

        } else {

            dto.setImageUrl(null);

        }

        dto.setAbout(
                profile.getAbout()
        );

        dto.setAboutMe(
                profile.getAboutMe()
        );

        // =====================================================
        // USER
        // =====================================================

        if (profile.getUser() != null) {

            dto.setUserId(
                    profile.getUser().getId()
            );

            dto.setUserName(
                    profile.getUser().getFullName()
            );

            dto.setFirstName(
                    profile.getUser().getFirstName()
            );

            dto.setLastName(
                    profile.getUser().getLastName()
            );

            dto.setVerified(

                    profile.getUser().getEmailVerified()

                            &&

                            profile.getUser().getPhoneVerified()

            );

            dto.setEmail(
                    profile.getUser().getEmail()
            );

            dto.setPhone(
                    profile.getUser().getPhone()
            );
        }

        // =====================================================
        // GENDER
        // =====================================================

        if (profile.getGender() != null) {

            dto.setGenderId(
                    profile.getGender().getId()
            );

            dto.setGenderName(
                    profile.getGender().getName()
            );
        }

        // =====================================================
        // RELIGION
        // =====================================================

        if (profile.getReligion() != null) {

            dto.setReligionId(
                    profile.getReligion().getId()
            );

            dto.setReligionName(
                    profile.getReligion().getName()
            );
        }

        // =====================================================
        // CASTE
        // =====================================================

        if (profile.getCaste() != null) {

            dto.setCasteId(
                    profile.getCaste().getId()
            );

            dto.setCasteName(
                    profile.getCaste().getName()
            );
        }

        // ================= CONTINUES IN PART 3C =================

        // =====================================================
        // SUB CASTE
        // =====================================================

        if (profile.getSubCaste() != null) {

            dto.setSubCasteId(
                    profile.getSubCaste().getId()
            );

            dto.setSubCasteName(
                    profile.getSubCaste().getName()
            );
        }

        // =====================================================
        // MOTHER TONGUE
        // =====================================================

        if (profile.getMotherTongue() != null) {

            dto.setMotherTongueId(
                    profile.getMotherTongue().getId()
            );

            dto.setMotherTongueName(
                    profile.getMotherTongue().getName()
            );
        }

        // =====================================================
        // EDUCATION
        // =====================================================

        if (profile.getEducationLevel() != null) {

            dto.setEducationLevelId(
                    profile.getEducationLevel().getId()
            );

            dto.setEducationLevelName(
                    profile.getEducationLevel().getName()
            );
        }

        // =====================================================
        // OCCUPATION
        // =====================================================

        if (profile.getOccupation() != null) {

            dto.setOccupationId(
                    profile.getOccupation().getId()
            );

            dto.setOccupationName(
                    profile.getOccupation().getName()
            );
        }

        // =====================================================
        // HEIGHT
        // =====================================================

        if (profile.getHeight() != null) {

            dto.setHeightId(
                    profile.getHeight().getId()
            );

            dto.setHeightValue(
                    profile.getHeight().getHeight()
            );
        }

        // =====================================================
        // WEIGHT
        // =====================================================

        if (profile.getWeight() != null) {

            dto.setWeightId(
                    profile.getWeight().getId()
            );

            dto.setWeightValue(
                    profile.getWeight().getValue()
            );
        }

        // =====================================================
        // BODY TYPE
        // =====================================================

        if (profile.getBodyType() != null) {

            dto.setBodyTypeId(
                    profile.getBodyType().getId()
            );

            dto.setBodyTypeName(
                    profile.getBodyType().getValue()
            );
        }

        // =====================================================
        // COMPLEXION
        // =====================================================

        if (profile.getComplexion() != null) {

            dto.setComplexionId(
                    profile.getComplexion().getId()
            );

            dto.setComplexionName(
                    profile.getComplexion().getValue()
            );
        }

        // =====================================================
        // COUNTRY
        // =====================================================

        if (profile.getCountry() != null) {

            dto.setCountryId(
                    profile.getCountry().getId()
            );

            dto.setCountryName(
                    profile.getCountry().getName()
            );
        }

        // =====================================================
        // STATE
        // =====================================================

        if (profile.getState() != null) {

            dto.setStateId(
                    profile.getState().getId()
            );

            dto.setStateName(
                    profile.getState().getName()
            );
        }

        // =====================================================
        // CITY
        // =====================================================

        if (profile.getCity() != null) {

            dto.setCityId(
                    profile.getCity().getId()
            );

            dto.setCityName(
                    profile.getCity().getName()
            );
        }

        // =====================================================
        // INCOME
        // =====================================================

        if (profile.getIncome() != null) {

            dto.setIncomeId(
                    profile.getIncome().getId()
            );

            dto.setIncomeValue(
                    profile.getIncome().getRange()
            );
        }

        // =====================================================
        // DIET
        // =====================================================

        if (profile.getDiet() != null) {

            dto.setDietId(
                    profile.getDiet().getId()
            );

            dto.setDietValue(
                    profile.getDiet().getName()
            );
        }

        // =====================================================
        // SMOKING
        // =====================================================

        if (profile.getSmoking() != null) {

            dto.setSmokingId(
                    profile.getSmoking().getId()
            );

            dto.setSmokingValue(
                    profile.getSmoking().getValue()
            );
        }

        // =====================================================
        // DRINKING
        // =====================================================

        if (profile.getDrinking() != null) {

            dto.setDrinkingId(
                    profile.getDrinking().getId()
            );

            dto.setDrinkingValue(
                    profile.getDrinking().getName()
            );
        }

        // ================= CONTINUES IN PART 3D =================

        // =====================================================
        // MARITAL STATUS
        // =====================================================

        if (profile.getMaritalStatus() != null) {

            dto.setMaritalStatusId(
                    profile.getMaritalStatus().getId()
            );

            dto.setMaritalStatusName(
                    profile.getMaritalStatus().getName()
            );
        }

        // =====================================================
        // PROFILE TYPE
        // =====================================================

        if (profile.getProfileType() != null) {

            dto.setProfileTypeId(
                    profile.getProfileType().getId()
            );

            dto.setProfileTypeName(
                    profile.getProfileType().getName()
            );
        }

        // =====================================================
        // MANGLIK STATUS
        // =====================================================

        if (profile.getManglikStatus() != null) {

            dto.setManglikStatusId(
                    profile.getManglikStatus().getId()
            );

            dto.setManglikStatusName(
                    profile.getManglikStatus().getName()
            );
        }

        // =====================================================
        // FAMILY TYPE
        // =====================================================

        if (profile.getFamilyType() != null) {

            dto.setFamilyTypeId(
                    profile.getFamilyType().getId()
            );

            dto.setFamilyTypeName(
                    profile.getFamilyType().getName()
            );
        }

        // =====================================================
        // FAMILY STATUS
        // =====================================================

        if (profile.getFamilyStatus() != null) {

            dto.setFamilyStatusId(
                    profile.getFamilyStatus().getId()
            );

            dto.setFamilyStatusName(
                    profile.getFamilyStatus().getName()
            );
        }

        // =====================================================
        // FAMILY VALUE
        // =====================================================

        if (profile.getFamilyValue() != null) {

            dto.setFamilyValueId(
                    profile.getFamilyValue().getId()
            );

            dto.setFamilyValueName(
                    profile.getFamilyValue().getName()
            );
        }

        // =====================================================
        // QUALIFICATION
        // =====================================================

        if (profile.getQualification() != null) {

            dto.setQualificationId(
                    profile.getQualification().getId()
            );

            dto.setQualificationName(
                    profile.getQualification().getName()
            );
        }

        // =====================================================
        // FIELD OF STUDY
        // =====================================================

        if (profile.getFieldOfStudy() != null) {

            dto.setFieldOfStudyId(
                    profile.getFieldOfStudy().getId()
            );

            dto.setFieldOfStudyName(
                    profile.getFieldOfStudy().getName()
            );
        }

        // =====================================================
        // EMPLOYMENT
        // =====================================================

        if (profile.getEmployed() != null) {

            dto.setEmployedStatusId(
                    profile.getEmployed().getId()
            );

            dto.setEmployedStatusName(
                    profile.getEmployed().getName()
            );
        }

        // =====================================================
        // DISABILITY STATUS
        // =====================================================

        if (profile.getDisabilityStatus() != null) {

            dto.setDisabilityStatusId(
                    profile.getDisabilityStatus().getId()
            );

            dto.setDisabilityStatusName(
                    profile.getDisabilityStatus().getValue()
            );
        }

        // =====================================================
        // BLOOD GROUP
        // =====================================================

        if (profile.getBloodGroup() != null) {

            dto.setBloodGroupId(
                    profile.getBloodGroup().getId()
            );

            dto.setBloodGroupName(
                    profile.getBloodGroup().getType()
            );
        }

        // =====================================================
        // COMPANY & ADDRESS
        // =====================================================

        dto.setCompanyName(
                profile.getCompanyName()
        );

        dto.setAddress(
                profile.getAddress()
        );

        // =====================================================
        // FAMILY DETAILS
        // =====================================================

        dto.setFatherName(
                profile.getFatherName()
        );

        dto.setFatherOccupation(
                profile.getFatherOccupation()
        );

        dto.setMotherName(
                profile.getMotherName()
        );

        dto.setMotherOccupation(
                profile.getMotherOccupation()
        );

        dto.setSiblingsCount(
                profile.getSiblingsCount()
        );

        // =====================================================
        // PREMIUM DETAILS
        // =====================================================

        dto.setIsPremium(profile.getIsPremium());

        dto.setProfileCompleted(profile.getProfileCompleted());

        dto.setBoostScore(profile.getBoostScore());
        return dto;
    }

// =====================================================
// SEARCH METHODS
// =====================================================

    // =====================================================
    // SEARCH PROFILES
    // =====================================================


    // =====================================================
    // RELIGION
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getByReligion(
            Long religionId,
            Pageable pageable
    ) {

        return repository.findByReligionId(
                religionId,
                pageable
        );
    }

    // =====================================================
    // CASTE
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getByCaste(
            Long casteId,
            Pageable pageable
    ) {

        return repository.findByCasteId(
                casteId,
                pageable
        );
    }

    // =====================================================
    // CITY
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getByCity(
            Long cityId,
            Pageable pageable
    ) {

        return repository.findByCityId(
                cityId,
                pageable
        );
    }

    // =====================================================
    // EDUCATION
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getByEducation(
            Long educationId,
            Pageable pageable
    ) {

        return repository.findByEducationLevelId(
                educationId,
                pageable
        );
    }

    // =====================================================
    // OCCUPATION
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<Profile> getByOccupation(
            Long occupationId,
            Pageable pageable
    ) {

        return repository.findByOccupationId(
                occupationId,
                pageable
        );
    }

    @Override
    public Page<Profile> getByReligionAndCaste(Long religionId, Long casteId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Profile> getByCityAndEducation(Long cityId, Long educationLevelId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Profile> getByOccupationAndCity(Long occupationId, Long cityId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Profile> getActiveByReligionAndCity(Long religionId, Long cityId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProfileResponseDTO> searchProfiles(PartnerPreference pref, Pageable pageable) {
        return null;
    }

    // =====================================================
    // ACTIVE PROFILES
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getActiveProfiles() {

        return repository.findByIsActiveTrue();
    }

// =====================================================
// PROFILE COMPLETION
// =====================================================

    // =====================================================
    // PROFILE COMPLETION
    // =====================================================

    private void updateProfileCompletion(Profile profile) {

        int completedFields = 0;
        int totalFields = 25;

        if (profile.getDateOfBirth() != null) completedFields++;
        if (profile.getGender() != null) completedFields++;
        if (profile.getReligion() != null) completedFields++;
        if (profile.getCaste() != null) completedFields++;
        if (profile.getSubCaste() != null) completedFields++;
        if (profile.getMotherTongue() != null) completedFields++;
        if (profile.getMaritalStatus() != null) completedFields++;
        if (profile.getEducationLevel() != null) completedFields++;
        if (profile.getOccupation() != null) completedFields++;
        if (profile.getHeight() != null) completedFields++;
        if (profile.getWeight() != null) completedFields++;
        if (profile.getBodyType() != null) completedFields++;
        if (profile.getComplexion() != null) completedFields++;
        if (profile.getCountry() != null) completedFields++;
        if (profile.getState() != null) completedFields++;
        if (profile.getCity() != null) completedFields++;
        if (profile.getIncome() != null) completedFields++;
        if (profile.getDiet() != null) completedFields++;
        if (profile.getSmoking() != null) completedFields++;
        if (profile.getDrinking() != null) completedFields++;
        if (profile.getQualification() != null) completedFields++;
        if (profile.getFieldOfStudy() != null) completedFields++;
        if (profile.getCompanyName() != null
                && !profile.getCompanyName().isBlank()) completedFields++;
        if (profile.getAbout() != null
                && !profile.getAbout().isBlank()) completedFields++;
        if (profile.getImageUrl() != null
                && !profile.getImageUrl().isBlank()) completedFields++;

        int percentage = (completedFields * 100) / totalFields;

        profile.setProfileCompleted(percentage>=100);
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    // =====================================================
    // SAFE LONG
    // =====================================================

    private Long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    // =====================================================
    // SAFE INTEGER
    // =====================================================

    private Integer safeInteger(Integer value) {
        return value == null ? 0 : value;
    }

    // =====================================================
    // SAFE BOOLEAN
    // =====================================================

    private Boolean safeBoolean(Boolean value) {
        return value != null && value;
    }


}