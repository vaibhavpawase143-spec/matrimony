package com.example.serviceimpl;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.CacheService;
import com.example.service.MatchAsyncService;
import com.example.service.ProfileService;
import com.example.specification.ProfileSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;


    private final ManglikStatusRepository manglikStatusRepository;

    private final FamilyTypeRepository familyTypeRepository;

    private final FamilyStatusRepository familyStatusRepository;
    private final QualificationRepository qualificationRepository;

    private final FieldOfStudyRepository fieldOfStudyRepository;

    private final EmployedRepository employedRepository;

    private final DisabilityStatusRepository disabilityStatusRepository;

    private final BloodGroupRepository bloodGroupRepository;

    private final BrotherTypeRepository brotherTypeRepository;

    private final SisterTypeRepository sisterTypeRepository;
    private final FamilyValueRepository familyValueRepository;
    private final UserRepository userRepository;
    private final ProfileTypeRepository profileTypeRepository;
    private final MatchAsyncService asyncService;

    private final ReligionRepository religionRepository;

    private final CasteRepository casteRepository;

    private final SubCasteRepository subCasteRepository;

    private final EducationLevelRepository educationRepository;

    private final OccupationRepository occupationRepository;

    private final HeightRepository heightRepository;

    private final WeightRepository weightRepository;

    private final CityRepository cityRepository;

    private final MotherTongueRepository motherTongueRepository;

    private final MaritalStatusRepository maritalStatusRepository;

    private final GenderRepository genderRepository;

    private final BodyTypeRepository bodyTypeRepository;

    private final ComplexionRepository complexionRepository;

    private final CountryRepository countryRepository;

    private final StateRepository stateRepository;
    private final IncomeRepository incomeRepository;

    private final DietRepository dietRepository;

    private final SmokingRepository smokingRepository;

    private final DrinkingRepository drinkingRepository;
    private final CacheService cacheService;

    // =====================================================
    // CURRENT USER
    // =====================================================

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    // =====================================================
    // CREATE PROFILE
    // =====================================================

    public ProfileResponseDTO createProfile(
            ProfileRequestDTO dto
    ) {

        User user = getCurrentUser();

        if (repository.existsByUserId(user.getId())) {

            throw new RuntimeException(
                    "Profile already exists!"
            );
        }

        Profile profile = new Profile();

        profile.setUser(user);

        mapDtoToEntity(dto, profile);

        // PROFILE COMPLETION
        updateProfileCompletion(profile);

        userRepository.save(user);

        Profile saved =
                repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }
    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    public ProfileResponseDTO updateMyProfile(
            UpdateProfileRequestDTO dto
    ) {

        User user = getCurrentUser();

        Profile profile =
                repository
                        .findByUserIdWithRelations(user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found"
                                )
                        );

        mapUpdateDto(dto, profile);

        // PROFILE COMPLETION
        updateProfileCompletion(profile);

        userRepository.save(user);

        Profile saved =
                repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }
    // =====================================================
    // GET MY PROFILE
    // =====================================================

    @Transactional(readOnly = true)
    public ProfileResponseDTO getMyProfile() {

        return mapToDTO(
                repository
                        .findByUserIdWithRelations(
                                getCurrentUser().getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found"
                                )
                        )
        );
    }

    // =====================================================
    // GET PROFILE BY ID
    // =====================================================

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileById(
            Long id
    ) {

        return mapToDTO(
                repository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found"
                                )
                        )
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void delete(Long id) {

        Profile profile =
                repository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found"
                                )
                        );

        if (!profile.getUser().getId()
                .equals(getCurrentUser().getId())) {

            throw new RuntimeException(
                    "Access Denied"
            );
        }

        repository.delete(profile);
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

            Profile p = existing.get();

            if (profile.getImageUrl() != null) {
                p.setImageUrl(profile.getImageUrl());
            }

            if (profile.getAbout() != null) {
                p.setAbout(profile.getAbout());
            }

            return repository.save(p);
        }

        profile.setUser(user);

        profile.setIsActive(true);

        return repository.save(profile);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getAll() {

        return repository.findAllWithUser();

    }
    public ProfileResponseDTO

    getProfileByUserId(

            Long userId

    ){

        Profile profile =

                repository

                        .findByUserId(userId)

                        .orElseThrow(

                                ()->new RuntimeException(
                                        "Profile not found"
                                )

                        );

        return mapToDTO(
                profile
        );

    }
    // =====================================================
    // DTO → ENTITY
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

        if (dto.getDietId() != null) {

            p.setDiet(
                    dietRepository
                            .findById(dto.getDietId())
                            .orElse(null)
            );
        }

// =====================================================
// SMOKING
// =====================================================

        if (dto.getSmokingId() != null) {

            p.setSmoking(
                    smokingRepository
                            .findById(dto.getSmokingId())
                            .orElse(null)
            );
        }

// =====================================================
// DRINKING
// =====================================================

        if (dto.getDrinkingId() != null) {

            p.setDrinking(
                    drinkingRepository
                            .findById(dto.getDrinkingId())
                            .orElse(null)
            );
        }
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
        if (dto.getProfileTypeId() != null) {

            p.setProfileType(

                    profileTypeRepository
                            .findById(
                                    dto.getProfileTypeId()
                            )
                            .orElse(null)

            );

        }

        if(dto.getManglikStatusId()!=null){

            p.setManglikStatus(

                    manglikStatusRepository
                            .findById(
                                    dto.getManglikStatusId()
                            )
                            .orElse(null)

            );

        }

        if(dto.getFamilyTypeId()!=null){

            p.setFamilyType(

                    familyTypeRepository
                            .findById(
                                    dto.getFamilyTypeId()
                            )
                            .orElse(null)

            );

        }

        if(dto.getFamilyStatusId()!=null){

            p.setFamilyStatus(

                    familyStatusRepository
                            .findById(
                                    dto.getFamilyStatusId()
                            )
                            .orElse(null)

            );

        }

        if(dto.getFamilyValueId()!=null){

            p.setFamilyValue(

                    familyValueRepository
                            .findById(
                                    dto.getFamilyValueId()
                            )
                            .orElse(null)

            );

        }
        if(dto.getQualificationId()!=null){

            p.setQualification(
                    qualificationRepository
                            .findById(dto.getQualificationId())
                            .orElse(null)
            );

        }

        if(dto.getFieldOfStudyId()!=null){

            p.setFieldOfStudy(
                    fieldOfStudyRepository
                            .findById(dto.getFieldOfStudyId())
                            .orElse(null)
            );

        }

        if(dto.getEmployedId()!=null){

            p.setEmployed(
                    employedRepository
                            .findById(dto.getEmployedId())
                            .orElse(null)
            );

        }
        if(dto.getDisabilityStatusId()!=null){

            p.setDisabilityStatus(
                    disabilityStatusRepository
                            .findById(dto.getDisabilityStatusId())
                            .orElse(null)
            );

        }

        if(dto.getBloodGroupId()!=null){

            p.setBloodGroup(
                    bloodGroupRepository
                            .findById(dto.getBloodGroupId())
                            .orElse(null)
            );

        }
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
    // UPDATE DTO → ENTITY
    // =====================================================

    private void mapUpdateDto(
            UpdateProfileRequestDTO dto,
            Profile p
    ) {

        mapDtoToEntity(
                convertUpdateDto(dto),
                p
        );
    }

    // =====================================================
    // UPDATE DTO CONVERTER
    // =====================================================

    private ProfileRequestDTO convertUpdateDto(
            UpdateProfileRequestDTO dto
    ) {

        ProfileRequestDTO r =
                new ProfileRequestDTO();

        // =====================================================
        // BASIC
        // =====================================================

        r.setFirstName(
                dto.getFirstName()
        );

        r.setLastName(
                dto.getLastName()
        );



        r.setEmail(
                dto.getEmail()
        );

        r.setPhone(
                dto.getPhone()
        );

        r.setDateOfBirth(
                dto.getDateOfBirth()
        );

        r.setImageUrl(
                dto.getImageUrl()
        );

        r.setAbout(
                dto.getAbout()
        );

        r.setAboutMe(
                dto.getAboutMe()
        );

        // =====================================================
        // CAREER
        // =====================================================

        r.setIncomeId(
                dto.getIncomeId()
        );

        r.setCompanyName(
                dto.getCompanyName()
        );

        // =====================================================
        // LOCATION
        // =====================================================

        r.setAddress(
                dto.getAddress()
        );

        // =====================================================
        // LIFESTYLE
        // =====================================================

        r.setDietId(
                dto.getDietId()
        );

        r.setSmokingId(
                dto.getSmokingId()
        );

        r.setDrinkingId(
                dto.getDrinkingId()
        );

        // =====================================================
        // FAMILY
        // =====================================================

        r.setFatherName(
                dto.getFatherName()
        );

        r.setFatherOccupation(
                dto.getFatherOccupation()
        );

        r.setMotherName(
                dto.getMotherName()
        );

        r.setMotherOccupation(
                dto.getMotherOccupation()
        );

        r.setSiblingsCount(
                dto.getSiblingsCount()
        );
        // =====================================================
        // PARTNER PREFERENCE
        // =====================================================


        // =====================================================
        // MASTER IDS
        // =====================================================

        r.setReligionId(
                dto.getReligionId()
        );

        r.setCasteId(
                dto.getCasteId()
        );

        r.setSubCasteId(
                dto.getSubCasteId()
        );

        r.setMotherTongueId(
                dto.getMotherTongueId()
        );

        r.setMaritalStatusId(
                dto.getMaritalStatusId()
        );
        r.setProfileTypeId(
                dto.getProfileTypeId()
        );
        r.setManglikStatusId(
                dto.getManglikStatusId()
        );

        r.setFamilyTypeId(
                dto.getFamilyTypeId()
        );

        r.setFamilyStatusId(
                dto.getFamilyStatusId()
        );

        r.setFamilyValueId(
                dto.getFamilyValueId()
        );
        r.setGenderId(
                dto.getGenderId()
        );

        r.setEducationLevelId(
                dto.getEducationLevelId()
        );

        r.setOccupationId(
                dto.getOccupationId()
        );

        r.setHeightId(
                dto.getHeightId()
        );

        r.setWeightId(
                dto.getWeightId()
        );

        r.setBodyTypeId(
                dto.getBodyTypeId()
        );

        r.setComplexionId(
                dto.getComplexionId()
        );

        r.setCountryId(
                dto.getCountryId()
        );

        r.setStateId(
                dto.getStateId()
        );

        r.setCityId(
                dto.getCityId()
        );
        r.setQualificationId(
                dto.getQualificationId()
        );

        r.setFieldOfStudyId(
                dto.getFieldOfStudyId()
        );

        r.setEmployedId(
                dto.getEmployedId()
        );
        r.setDisabilityStatusId(
                dto.getDisabilityStatusId()
        );

        r.setBloodGroupId(
                dto.getBloodGroupId()
        );


        return r;
    }

    // =====================================================
    // ENTITY → DTO
    // =====================================================

    public ProfileResponseDTO mapToDTO(
            Profile p
    ) {

        ProfileResponseDTO dto =
                new ProfileResponseDTO();

        dto.setId(p.getId());

        dto.setDateOfBirth(
                p.getDateOfBirth()
        );

        dto.setImageUrl(
                p.getImageUrl()
        );
        dto.setAbout(
                p.getAbout()
        );
        if(p.getBloodGroup()!=null){

            dto.setBloodGroupId(
                    p.getBloodGroup().getId()
            );

            dto.setBloodGroupName(
                    p.getBloodGroup().getType()
            );

        }


        dto.setAboutMe(
                p.getAboutMe()
        );

        // =====================================================
        // USER
        // =====================================================

        if (p.getUser() != null) {

            dto.setUserId(
                    p.getUser().getId()
            );

            dto.setUserName(
                    p.getUser().getFullName()
            );

            dto.setFirstName(
                    p.getUser().getFirstName()
            );

            dto.setLastName(
                    p.getUser().getLastName()
            );

            dto.setEmail(
                    p.getUser().getEmail()
            );

            dto.setPhone(
                    p.getUser().getPhone()
            );
        }

        // =====================================================
        // GENDER
        // =====================================================

        if (p.getGender() != null) {

            dto.setGenderId(
                    p.getGender().getId()
            );

            dto.setGenderName(
                    p.getGender().getName()
            );
        }

        // =====================================================
        // RELIGION
        // =====================================================

        if (p.getReligion() != null) {

            dto.setReligionId(
                    p.getReligion().getId()
            );

            dto.setReligionName(
                    p.getReligion().getName()
            );
        }

        // =====================================================
        // CASTE
        // =====================================================

        if (p.getCaste() != null) {

            dto.setCasteId(
                    p.getCaste().getId()
            );

            dto.setCasteName(
                    p.getCaste().getName()
            );
        }

        // =====================================================
        // WEIGHT
        // =====================================================

        if (p.getWeight() != null) {

            dto.setWeightId(
                    p.getWeight().getId()
            );

            dto.setWeightValue(
                    p.getWeight().getValue()
            );
        }

        // =====================================================
        // HEIGHT
        // =====================================================

        if (p.getHeight() != null) {

            dto.setHeightId(
                    p.getHeight().getId()
            );

            dto.setHeightValue(
                    p.getHeight().getHeight()
            );
        }

        // =====================================================
        // BODY TYPE
        // =====================================================

        if (p.getBodyType() != null) {

            dto.setBodyTypeId(
                    p.getBodyType().getId()
            );

            dto.setBodyTypeName(
                    p.getBodyType().getValue()
            );
        }

        // =====================================================
        // COMPLEXION
        // =====================================================

        if (p.getComplexion() != null) {

            dto.setComplexionId(
                    p.getComplexion().getId()
            );

            dto.setComplexionName(
                    p.getComplexion().getValue()
            );
        }

        // =====================================================
        // COUNTRY
        // =====================================================

        if (p.getCountry() != null) {

            dto.setCountryId(
                    p.getCountry().getId()
            );

            dto.setCountryName(
                    p.getCountry().getName()
            );
        }

        // =====================================================
        // STATE
        // =====================================================

        if (p.getState() != null) {

            dto.setStateId(
                    p.getState().getId()
            );

            dto.setStateName(
                    p.getState().getName()
            );
        }

        // =====================================================
        // CITY
        // =====================================================

        if (p.getCity() != null) {

            dto.setCityId(
                    p.getCity().getId()
            );

            dto.setCityName(
                    p.getCity().getName()
            );
        }
// =====================================================
// INCOME
// =====================================================

        if (p.getIncome() != null) {

            dto.setIncomeId(
                    p.getIncome().getId()
            );

            dto.setIncomeValue(
                    p.getIncome().getRange()
            );
        }

// =====================================================
// DIET
// =====================================================

        if (p.getDiet() != null) {

            dto.setDietId(
                    p.getDiet().getId()
            );

            dto.setDietValue(
                    p.getDiet().getName()
            );
        }

// =====================================================
// SMOKING
// =====================================================

        if (p.getSmoking() != null) {

            dto.setSmokingId(
                    p.getSmoking().getId()
            );

            dto.setSmokingValue(
                    p.getSmoking().getValue()
            );
        }

// =====================================================
// DRINKING
// =====================================================

        if (p.getDrinking() != null) {

            dto.setDrinkingId(
                    p.getDrinking().getId()
            );

            dto.setDrinkingValue(
                    p.getDrinking().getValue()
            );
        }
        // =====================================================
// SUB CASTE
// =====================================================

        if (p.getSubCaste() != null) {

            dto.setSubCasteId(
                    p.getSubCaste().getId()
            );

            dto.setSubCasteName(
                    p.getSubCaste().getName()
            );
        }

// =====================================================
// EDUCATION
// =====================================================

        if (p.getEducationLevel() != null) {

            dto.setEducationLevelId(
                    p.getEducationLevel().getId()
            );

            dto.setEducationLevelName(
                    p.getEducationLevel().getName()
            );
        }

// =====================================================
// OCCUPATION
// =====================================================

        if (p.getOccupation() != null) {

            dto.setOccupationId(
                    p.getOccupation().getId()
            );

            dto.setOccupationName(
                    p.getOccupation().getName()
            );
        }

// =====================================================
// MOTHER TONGUE
// =====================================================

        if (p.getMotherTongue() != null) {

            dto.setMotherTongueId(
                    p.getMotherTongue().getId()
            );

            dto.setMotherTongueName(
                    p.getMotherTongue().getName()
            );
        }

// =====================================================
// MARITAL STATUS
// =====================================================

        if (p.getMaritalStatus() != null) {

            dto.setMaritalStatusId(
                    p.getMaritalStatus().getId()
            );

            dto.setMaritalStatusName(
                    p.getMaritalStatus().getName()
            );
        }
        if (p.getProfileType() != null) {

            dto.setProfileTypeId(
                    p.getProfileType().getId()
            );

            dto.setProfileTypeName(
                    p.getProfileType().getName()
            );

        }

        if(p.getManglikStatus()!=null){

            dto.setManglikStatusId(
                    p.getManglikStatus().getId()
            );

            dto.setManglikStatusName(
                    p.getManglikStatus().getName()
            );

        }

        if(p.getFamilyType()!=null){

            dto.setFamilyTypeId(
                    p.getFamilyType().getId()
            );

            dto.setFamilyTypeName(
                    p.getFamilyType().getName()
            );

        }

        if(p.getFamilyStatus()!=null){

            dto.setFamilyStatusId(
                    p.getFamilyStatus().getId()
            );

            dto.setFamilyStatusName(
                    p.getFamilyStatus().getName()
            );

        }

        if(p.getFamilyValue()!=null){

            dto.setFamilyValueId(
                    p.getFamilyValue().getId()
            );

            dto.setFamilyValueName(
                    p.getFamilyValue().getName()
            );

        }
        // =====================================================
// QUALIFICATION
// =====================================================

        if(p.getQualification()!=null){

            dto.setQualificationId(
                    p.getQualification().getId()
            );

            dto.setQualificationName(
                    p.getQualification().getName()
            );
        }

// =====================================================
// FIELD OF STUDY
// =====================================================

        if(p.getFieldOfStudy()!=null){

            dto.setFieldOfStudyId(
                    p.getFieldOfStudy().getId()
            );

            dto.setFieldOfStudyName(
                    p.getFieldOfStudy().getName()
            );
        }

// =====================================================
// EMPLOYED STATUS
// =====================================================

        if(p.getEmployed()!=null){

            dto.setEmployedStatusId(
                    p.getEmployed().getId()
            );

            dto.setEmployedStatusName(
                    p.getEmployed().getName()
            );
        }

// =====================================================
// DISABILITY STATUS
// =====================================================

        if(p.getDisabilityStatus()!=null){

            dto.setDisabilityStatusId(
                    p.getDisabilityStatus().getId()
            );

            dto.setDisabilityStatusName(
                    p.getDisabilityStatus().getValue()
            );
        }

// =====================================================
// BLOOD GROUP
// =====================================================

        if(p.getBloodGroup()!=null){

            dto.setBloodGroupId(
                    p.getBloodGroup().getId()
            );

            dto.setBloodGroupName(
                    p.getBloodGroup().getType()
            );
        }

// =====================================================
// BROTHER TYPE
// =====================================================


// =====================================================
// COMPANY
// =====================================================

        dto.setCompanyName(
                p.getCompanyName()
        );

// =====================================================
// ADDRESS
// =====================================================

        dto.setAddress(
                p.getAddress()
        );

// =====================================================
// FAMILY
// =====================================================

        dto.setFatherName(
                p.getFatherName()
        );

        dto.setFatherOccupation(
                p.getFatherOccupation()
        );

        dto.setMotherName(
                p.getMotherName()
        );

        dto.setMotherOccupation(
                p.getMotherOccupation()
        );

        dto.setSiblingsCount(
                p.getSiblingsCount()
        );

// =====================================================
// PARTNER PREFERENCE
// =====================================================



// =====================================================
// ABOUT ME
// =====================================================

        dto.setAboutMe(
                p.getAboutMe()
        );
        dto.setCurrentStep(
                p.getCurrentStep()
        );

        dto.setProfileCompleted(
                p.getProfileCompleted()
        );

        dto.setIsActive(
                p.getIsActive()
        );
        return dto;
    }


    // =====================================================
    // SEARCH
    // =====================================================

    public Page<ProfileResponseDTO> searchProfiles(
            PartnerPreference pref,
            Pageable pageable
    ) {

        Specification<Profile> spec =
                ProfileSpecification
                        .matchPreferences(pref);

        return repository
                .findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    // =====================================================
    // REDIS
    // =====================================================

    private void safeRedis(Long userId) {

        try {

            cacheService.evictUserMatches(userId);

        } catch (Exception e) {

            System.out.println("Redis skipped");
        }

        asyncService.preloadMatches(userId);
    }
    private void updateProfileCompletion(Profile p) {

        int total = 0;

        int filled = 0;

        // ================= BASIC =================

        total++;
        if (p.getReligion() != null) filled++;

        total++;
        if (p.getCaste() != null) filled++;

        total++;
        if (p.getSubCaste() != null) filled++;

        total++;
        if (p.getMotherTongue() != null) filled++;

        total++;
        if (p.getMaritalStatus() != null) filled++;

        total++;
        if (p.getGender() != null) filled++;

        total++;
        if (p.getDateOfBirth() != null) filled++;

        // ================= PROFILE TYPE =================

        total++;
        if (p.getProfileType() != null) filled++;

        total++;
        if (p.getManglikStatus() != null) filled++;

        total++;
        if (p.getFamilyType() != null) filled++;

        total++;
        if (p.getFamilyStatus() != null) filled++;

        total++;
        if (p.getFamilyValue() != null) filled++;

        // ================= EDUCATION =================

        total++;
        if (p.getEducationLevel() != null) filled++;

        total++;
        if (p.getQualification() != null) filled++;

        total++;
        if (p.getFieldOfStudy() != null) filled++;

        total++;
        if (p.getOccupation() != null) filled++;

        total++;
        if (p.getEmployed() != null) filled++;

        total++;
        if (p.getCompanyName() != null &&
                !p.getCompanyName().isBlank()) filled++;

        total++;
        if (p.getIncome() != null) filled++;

        // ================= LOCATION =================

        total++;
        if (p.getCountry() != null) filled++;

        total++;
        if (p.getState() != null) filled++;

        total++;
        if (p.getCity() != null) filled++;

        total++;
        if (p.getAddress() != null &&
                !p.getAddress().isBlank()) filled++;

        // ================= PHYSICAL =================

        total++;
        if (p.getHeight() != null) filled++;

        total++;
        if (p.getWeight() != null) filled++;

        total++;
        if (p.getComplexion() != null) filled++;

        total++;
        if (p.getBodyType() != null) filled++;

        total++;
        if (p.getBloodGroup() != null) filled++;

        total++;
        if (p.getDisabilityStatus() != null) filled++;

        // ================= LIFESTYLE =================

        total++;
        if (p.getDiet() != null) filled++;

        total++;
        if (p.getSmoking() != null) filled++;

        total++;
        if (p.getDrinking() != null) filled++;

        // ================= FAMILY =================

        total++;
        if (p.getFatherName() != null &&
                !p.getFatherName().isBlank()) filled++;

        total++;
        if (p.getMotherName() != null &&
                !p.getMotherName().isBlank()) filled++;

        total++;
        if (p.getFatherOccupation() != null &&
                !p.getFatherOccupation().isBlank()) filled++;

        total++;
        if (p.getMotherOccupation() != null &&
                !p.getMotherOccupation().isBlank()) filled++;

        total++;
        if (p.getSiblingsCount() != null) filled++;

        // ================= ABOUT =================

        total++;
        if (p.getAboutMe() != null &&
                !p.getAboutMe().isBlank()) filled++;

        total++;
        if (p.getImageUrl() != null &&
                !p.getImageUrl().isBlank()) filled++;

        // ================= PARTNER PREF =================


        int percentage =
                (filled * 100) / total;

        p.setCurrentStep(
                percentage
        );

        p.setProfileCompleted(
                percentage >= 90
        );

        System.out.println(
                "Filled = " + filled
        );

        System.out.println(
                "Total = " + total
        );

        System.out.println(
                "PROFILE % = " + percentage
        );
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> getById(Long id) {

        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> getByUserId(Long userId) {

        return repository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getActiveProfiles() {

        return repository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByReligion(Long religionId) {

        return repository.findByReligionId(religionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByCaste(Long casteId) {

        return repository.findByCasteId(casteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByCity(Long cityId) {

        return repository.findByCityId(cityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByEducation(Long educationLevelId) {

        return repository.findByEducationLevelId(
                educationLevelId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByOccupation(Long occupationId) {

        return repository.findByOccupationId(
                occupationId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByReligionAndCaste(
            Long religionId,
            Long casteId
    ) {

        return repository.findByReligionIdAndCasteId(
                religionId,
                casteId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByCityAndEducation(
            Long cityId,
            Long educationLevelId
    ) {

        return repository
                .findByCityIdAndEducationLevelId(
                        cityId,
                        educationLevelId
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getByOccupationAndCity(
            Long occupationId,
            Long cityId
    ) {

        return repository
                .findByOccupationIdAndCityId(
                        occupationId,
                        cityId
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getActiveByReligionAndCity(
            Long religionId,
            Long cityId
    ) {

        return repository
                .findByReligionIdAndCityIdAndIsActiveTrue(
                        religionId,
                        cityId
                );
    }

}