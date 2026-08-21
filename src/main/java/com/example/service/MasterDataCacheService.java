package com.example.service;

import com.example.dto.response.*;
import com.example.model.*;
import com.example.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterDataCacheService {

    private static final Duration CACHE_TTL = Duration.ofHours(12);

    private static final String RELIGION_KEY = "master:religions";
    private static final String CASTE_KEY = "master:castes";
    private static final String SUBCASTE_KEY = "master:subcastes";
    private static final String CITY_KEY = "master:cities";
    private static final String STATE_KEY = "master:states";
    private static final String COUNTRY_KEY = "master:countries";
    private static final String OCCUPATION_KEY = "master:occupations";
    private static final String EDUCATION_KEY = "master:educations";
    private static final String QUALIFICATION_KEY = "master:qualifications";
    private static final String MOTHER_TONGUE_KEY = "master:motherTongues";
    private static final String MARITAL_STATUS_KEY = "master:maritalStatus";
    private static final String PROFILE_TYPE_KEY = "master:profileTypes";
    private static final String HEIGHT_KEY = "master:heights";
    private static final String WEIGHT_KEY = "master:weights";
    private static final String INCOME_KEY = "master:income";
    private static final String DIET_KEY = "master:diets";
    private static final String DRINKING_KEY = "master:drinking";
    private static final String SMOKING_KEY = "master:smoking";
    private static final String BODY_TYPE_KEY = "master:bodyTypes";
    private static final String COMPLEXION_KEY = "master:complexions";
    private static final String FAMILY_KEY = "master:family";
    private static final String FAMILY_STATUS_KEY = "master:familyStatus";
    private static final String FAMILY_TYPE_KEY = "master:familyType";
    private static final String FAMILY_VALUE_KEY = "master:familyValue";
    private static final String FIELD_OF_STUDY_KEY = "master:fieldOfStudy";
    private static final String MANGLIK_KEY = "master:manglik";
    private static final String EMPLOYED_KEY = "master:employed";
    private static final String DISABILITY_KEY = "master:disability";
    private static final String GENDER_KEY = "master:gender";
    // Alias constants for backward compatibility
    private static final String KEY_RELIGION = RELIGION_KEY;
    private static final String KEY_CASTE = CASTE_KEY;
    private static final String KEY_SUBCASTE = SUBCASTE_KEY;
    private static final String KEY_CITY = CITY_KEY;
    private static final String KEY_STATE = STATE_KEY;
    private static final String KEY_COUNTRY = COUNTRY_KEY;
    private static final String KEY_OCCUPATION = OCCUPATION_KEY;
    private static final String KEY_EDUCATION = EDUCATION_KEY;
    private static final String KEY_QUALIFICATION = QUALIFICATION_KEY;
    private static final String KEY_MOTHER_TONGUE = MOTHER_TONGUE_KEY;
    private static final String KEY_MARITAL_STATUS = MARITAL_STATUS_KEY;
    private static final String KEY_PROFILE_TYPE = PROFILE_TYPE_KEY;
    private static final String KEY_HEIGHT = HEIGHT_KEY;
    private static final String KEY_WEIGHT = WEIGHT_KEY;
    private static final String KEY_INCOME = INCOME_KEY;
    private static final String KEY_DIET = DIET_KEY;
    private static final String KEY_SMOKING = SMOKING_KEY;
    private static final String KEY_DRINKING = DRINKING_KEY;
    private static final String KEY_BODY_TYPE = BODY_TYPE_KEY;
    private static final String KEY_COMPLEXION = COMPLEXION_KEY;
    private static final String KEY_FAMILY = FAMILY_KEY;
    private static final String KEY_FAMILY_STATUS = FAMILY_STATUS_KEY;
    private static final String KEY_FAMILY_TYPE = FAMILY_TYPE_KEY;
    private static final String KEY_FAMILY_VALUE = FAMILY_VALUE_KEY;
    private static final String KEY_FIELD_OF_STUDY = FIELD_OF_STUDY_KEY;
    private static final String KEY_MANGLIK_STATUS = MANGLIK_KEY;
    private static final String KEY_EMPLOYED = EMPLOYED_KEY;
    private static final String KEY_DISABILITY_STATUS = DISABILITY_KEY;
    private static final String KEY_GENDER = GENDER_KEY;

    private final RedisTemplate<String, Object> redisTemplate;
    private final Executor taskExecutor;

    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final SubCasteRepository subCasteRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final OccupationRepository occupationRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final QualificationRepository qualificationRepository;
    private final MotherTongueRepository motherTongueRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final ProfileTypeRepository profileTypeRepository;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final IncomeRepository incomeRepository;
    private final DietRepository dietRepository;
    private final DrinkingRepository drinkingRepository;
    private final SmokingRepository smokingRepository;
    private final BodyTypeRepository bodyTypeRepository;
    private final ComplexionRepository complexionRepository;
    private final FamilyRepository familyRepository;
    private final FamilyStatusRepository familyStatusRepository;
    private final FamilyTypeRepository familyTypeRepository;
    private final FamilyValueRepository familyValueRepository;
    private final FieldOfStudyRepository fieldOfStudyRepository;
    private final ManglikStatusRepository manglikStatusRepository;
    private final EmployedRepository employedRepository;
    private final DisabilityStatusRepository disabilityStatusRepository;
    private final GenderRepository genderRepository;
    private final java.util.concurrent.ConcurrentHashMap<String, Object> localCache = new java.util.concurrent.ConcurrentHashMap<>();

    @PostConstruct
    public void initializeCache() {
        log.info("==========================================");
        log.info("Warming up Master Data L1 ConcurrentHashMap Cache...");
        log.info("==========================================");
        CompletableFuture.runAsync(() -> {
            try {
                getReligions();
                getCastes();
                getSubCastes();
                getCountries();
                getStates();
                getCities();
                getOccupations();
                getEducationLevels();
                getQualifications();
                getMotherTongues();
                getMaritalStatus();
                getProfileTypes();
                getHeights();
                getWeights();
                getIncome();
                getDiets();
                getSmoking();
                getDrinking();
                getBodyTypes();
                getComplexions();
                getGender();
                log.info("✅ Master Data L1 Cache warmup complete!");
            } catch (Exception e) {
                log.error("Master Data L1 Cache warmup error", e);
            }
        }, taskExecutor);
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void refreshMasterCache() {
        localCache.clear();
        initializeCache();
    }

    public void warmUpCache() {
        initializeCache();
    }

    private void put(String key, Object value) {
        localCache.put(key, value);
        try {
            redisTemplate.opsForValue().set(key, value, CACHE_TTL);
        } catch (Exception e) {
            log.warn("Redis put skipped for key: {}", key);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getOrFetch(String key, Supplier<List<T>> dbFetcher) {
        // 1. Fast L1 In-Memory Cache Lookup (0 ms)
        Object cached = localCache.get(key);
        if (cached != null) {
            return (List<T>) cached;
        }

        // 2. Direct DB Fetch on L1 Miss (Fast & Reliable ~2ms)
        List<T> data = null;
        try {
            data = dbFetcher.get();
        } catch (Exception e) {
            log.error("Database fetch failed for key: {}", key, e);
            return Collections.emptyList();
        }

        if (data != null && !data.isEmpty()) {
            localCache.put(key, data);
            final List<T> finalData = data;
            CompletableFuture.runAsync(() -> {
                try {
                    redisTemplate.opsForValue().set(key, finalData, CACHE_TTL);
                } catch (Exception e) {
                    // ignore async redis write failure
                }
            }, taskExecutor);
        }

        return data != null ? data : Collections.emptyList();
    }

    public java.util.Map<String, Object> getAllMasterData() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("religions", getReligions());
        map.put("castes", getCastes());
        map.put("subCastes", getSubCastes());
        map.put("countries", getCountries());
        map.put("states", getStates());
        map.put("cities", getCities());
        map.put("occupations", getOccupations());
        map.put("educationLevels", getEducationLevels());
        map.put("qualifications", getQualifications());
        map.put("motherTongues", getMotherTongues());
        map.put("maritalStatuses", getMaritalStatus());
        map.put("profileTypes", getProfileTypes());
        map.put("heights", getHeights());
        map.put("weights", getWeights());
        map.put("incomes", getIncome());
        map.put("diets", getDiets());
        map.put("smokingOptions", getSmoking());
        map.put("drinkingOptions", getDrinking());
        map.put("bodyTypes", getBodyTypes());
        map.put("complexions", getComplexions());
        map.put("familyTypes", getFamilyTypes());
        map.put("familyStatuses", getFamilyStatus());
        map.put("familyValues", getFamilyValues());
        map.put("fieldsOfStudy", getFieldOfStudies());
        map.put("manglikStatuses", getManglikStatus());
        map.put("employmentStatuses", getEmployed());
        map.put("disabilityStatuses", getDisabilityStatus());
        map.put("genders", getGender());
        return map;
    }

    public void evict(String key) {
        localCache.remove(key);

        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Failed to evict key: {}", key, e);
        }
    }
    public void evictReligions() {
        localCache.remove(RELIGION_KEY);

        try {
            redisTemplate.delete(RELIGION_KEY);
        } catch (Exception e) {
            log.warn("Failed to evict religion Redis cache", e);
        }
    }

    public List<ReligionResponseDTO> getReligions() { return getOrFetch(KEY_RELIGION, this::fetchReligionsFromDb); }
    public List<CasteResponseDTO> getCastes() { return getOrFetch(KEY_CASTE, this::fetchCastesFromDb); }
    public List<SubCasteResponseDTO> getSubCastes() { return getOrFetch(KEY_SUBCASTE, this::fetchSubCastesFromDb); }
    public List<CountryResponseDTO> getCountries() { return getOrFetch(KEY_COUNTRY, this::fetchCountriesFromDb); }
    public List<StateResponseDTO> getStates() { return getOrFetch(KEY_STATE, this::fetchStatesFromDb); }
    public List<CityResponseDTO> getCities() { return getOrFetch(KEY_CITY, this::fetchCitiesFromDb); }
    public List<OccupationResponseDTO> getOccupations() { return getOrFetch(KEY_OCCUPATION, this::fetchOccupationsFromDb); }
    public List<EducationLevelResponseDto> getEducationLevels() { return getOrFetch(KEY_EDUCATION, this::fetchEducationLevelsFromDb); }
    public List<QualificationResponseDTO> getQualifications() { return getOrFetch(KEY_QUALIFICATION, this::fetchQualificationsFromDb); }
    public List<MotherTongueResponseDTO> getMotherTongues() { return getOrFetch(KEY_MOTHER_TONGUE, this::fetchMotherTonguesFromDb); }
    public List<MaritalStatusResponseDTO> getMaritalStatus() { return getOrFetch(KEY_MARITAL_STATUS, this::fetchMaritalStatusFromDb); }
    public List<ProfileTypeResponseDTO> getProfileTypes() { return getOrFetch(KEY_PROFILE_TYPE, this::fetchProfileTypesFromDb); }
    public List<HeightResponseDTO> getHeights() { return getOrFetch(KEY_HEIGHT, this::fetchHeightsFromDb); }
    public List<WeightResponseDTO> getWeights() { return getOrFetch(KEY_WEIGHT, this::fetchWeightsFromDb); }
    public List<IncomeResponseDTO> getIncome() { return getOrFetch(KEY_INCOME, this::fetchIncomeFromDb); }
    public List<DietResponseDto> getDiets() { return getOrFetch(KEY_DIET, this::fetchDietsFromDb); }
    public List<SmokingResponseDTO> getSmoking() { return getOrFetch(KEY_SMOKING, this::fetchSmokingFromDb); }
    public List<DrinkingResponseDto> getDrinking() { return getOrFetch(KEY_DRINKING, this::fetchDrinkingFromDb); }
    public List<BodyTypeResponseDTO> getBodyTypes() { return getOrFetch(KEY_BODY_TYPE, this::fetchBodyTypesFromDb); }
    public List<ComplexionResponseDTO> getComplexions() { return getOrFetch(KEY_COMPLEXION, this::fetchComplexionsFromDb); }
    public List<FamilyResponseDto> getFamilies() { return getOrFetch(KEY_FAMILY, this::fetchFamiliesFromDb); }
    public List<FamilyStatusResponseDto> getFamilyStatus() { return getOrFetch(KEY_FAMILY_STATUS, this::fetchFamilyStatusFromDb); }
    public List<FamilyTypeResponseDto> getFamilyTypes() { return getOrFetch(KEY_FAMILY_TYPE, this::fetchFamilyTypesFromDb); }
    public List<FamilyValueResponseDto> getFamilyValues() { return getOrFetch(KEY_FAMILY_VALUE, this::fetchFamilyValuesFromDb); }
    public List<FieldOfStudyResponseDTO> getFieldOfStudies() { return getOrFetch(KEY_FIELD_OF_STUDY, this::fetchFieldOfStudiesFromDb); }
    public List<ManglikStatusResponseDTO> getManglikStatus() { return getOrFetch(KEY_MANGLIK_STATUS, this::fetchManglikStatusFromDb); }
    public List<EmployedResponseDto> getEmployed() { return getOrFetch(KEY_EMPLOYED, this::fetchEmployedFromDb); }
    public List<DisabilityStatusResponseDto> getDisabilityStatus() { return getOrFetch(KEY_DISABILITY_STATUS, this::fetchDisabilityStatusFromDb); }
    public List<GenderResponseDTO> getGender() { return getOrFetch(KEY_GENDER, this::fetchGenderFromDb); }

    private List<ReligionResponseDTO> fetchReligionsFromDb() { return religionRepository.findAllByDeletedAtIsNull().stream().map(this::toReligionDto).toList(); }
    private List<CasteResponseDTO> fetchCastesFromDb() { return casteRepository.findAllWithRelations().stream().map(this::toCasteDto).toList(); }
    private List<SubCasteResponseDTO> fetchSubCastesFromDb() { return subCasteRepository.findAllWithRelations().stream().map(this::toSubCasteDto).toList(); }
    private List<CountryResponseDTO> fetchCountriesFromDb() { return countryRepository.findByDeletedAtIsNull().stream().map(this::toCountryDto).toList(); }
    private List<StateResponseDTO> fetchStatesFromDb() { return stateRepository.findAllWithRelations().stream().map(this::toStateDto).toList(); }
    private List<CityResponseDTO> fetchCitiesFromDb() { return cityRepository.findAllWithRelations().stream().map(this::toCityDto).toList(); }
    private List<OccupationResponseDTO> fetchOccupationsFromDb() { return occupationRepository.findAllByDeletedAtIsNull().stream().map(this::toOccupationDto).toList(); }
    private List<EducationLevelResponseDto> fetchEducationLevelsFromDb() { return educationLevelRepository.findAllByDeletedAtIsNull().stream().map(this::toEducationDto).toList(); }
    private List<QualificationResponseDTO> fetchQualificationsFromDb() { return qualificationRepository.findAllByDeletedAtIsNull().stream().map(this::toQualificationDto).toList(); }
    private List<MotherTongueResponseDTO> fetchMotherTonguesFromDb() { return motherTongueRepository.findAllByDeletedAtIsNull().stream().map(this::toMotherTongueDto).toList(); }
    private List<MaritalStatusResponseDTO> fetchMaritalStatusFromDb() { return maritalStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toMaritalStatusDto).toList(); }
    private List<ProfileTypeResponseDTO> fetchProfileTypesFromDb() { return profileTypeRepository.findAllByDeletedAtIsNull().stream().map(this::toProfileTypeDto).toList(); }
    private List<HeightResponseDTO> fetchHeightsFromDb() { return heightRepository.findAllByDeletedAtIsNull().stream().map(this::toHeightDto).toList(); }
    private List<WeightResponseDTO> fetchWeightsFromDb() {
        return weightRepository.findAllWithAdmin()
                .stream()
                .map(this::toWeightDto)
                .toList();
    }
    private List<IncomeResponseDTO> fetchIncomeFromDb() { return incomeRepository.findAllByDeletedAtIsNull().stream().map(this::toIncomeDto).toList(); }
    private List<DietResponseDto> fetchDietsFromDb() { return dietRepository.findByDeletedAtIsNull().stream().map(this::toDietDto).toList(); }
    private List<SmokingResponseDTO> fetchSmokingFromDb() { return smokingRepository.findAllByDeletedAtIsNull().stream().map(this::toSmokingDto).toList(); }
    private List<DrinkingResponseDto> fetchDrinkingFromDb() { return drinkingRepository.findByDeletedAtIsNull().stream().map(this::toDrinkingDto).toList(); }
    private List<BodyTypeResponseDTO> fetchBodyTypesFromDb() { return bodyTypeRepository.findByDeletedAtIsNull().stream().map(this::toBodyTypeDto).toList(); }
    private List<ComplexionResponseDTO> fetchComplexionsFromDb() { return complexionRepository.findByDeletedAtIsNull().stream().map(this::toComplexionDto).toList(); }
    private List<FamilyResponseDto> fetchFamiliesFromDb() { return familyRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyDto).toList(); }
    private List<FamilyStatusResponseDto> fetchFamilyStatusFromDb() { return familyStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyStatusDto).toList(); }
    private List<FamilyTypeResponseDto> fetchFamilyTypesFromDb() { return familyTypeRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyTypeDto).toList(); }
    private List<FamilyValueResponseDto> fetchFamilyValuesFromDb() { return familyValueRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyValueDto).toList(); }
    private List<FieldOfStudyResponseDTO> fetchFieldOfStudiesFromDb() { return fieldOfStudyRepository.findAllByDeletedAtIsNull().stream().map(this::toFieldOfStudyDto).toList(); }
    private List<ManglikStatusResponseDTO> fetchManglikStatusFromDb() { return manglikStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toManglikStatusDto).toList(); }
    private List<EmployedResponseDto> fetchEmployedFromDb() { return employedRepository.findAllByDeletedAtIsNull().stream().map(this::toEmployedDto).toList(); }
    private List<DisabilityStatusResponseDto> fetchDisabilityStatusFromDb() { return disabilityStatusRepository.findByDeletedAtIsNull().stream().map(this::toDisabilityStatusDto).toList(); }
    private List<GenderResponseDTO> fetchGenderFromDb() { return genderRepository.findByIsActiveTrue().stream().map(this::toGenderDto).toList(); }

    private ReligionResponseDTO toReligionDto(Religion e) {
        return ReligionResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin() != null ? e.getAdmin().getId() : null)
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private CasteResponseDTO toCasteDto(Caste e) {
        return CasteResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin() != null ? e.getAdmin().getId() : null)
                .religionId(e.getReligion() != null ? e.getReligion().getId() : null)
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())


                .build();
    }

    private SubCasteResponseDTO toSubCasteDto(SubCaste e) {
        return SubCasteResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin() != null ? e.getAdmin().getId() : null)
                .adminName(e.getAdmin() != null ? e.getAdmin().getName() : null)
                .casteId(e.getCaste() != null ? e.getCaste().getId() : null)
                .casteName(e.getCaste() != null ? e.getCaste().getName() : null)
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private OccupationResponseDTO toOccupationDto(Occupation e) {
        return OccupationResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private EducationLevelResponseDto toEducationDto(EducationLevel e) {
        return EducationLevelResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private QualificationResponseDTO toQualificationDto(Qualification e) {
        return QualificationResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private MotherTongueResponseDTO toMotherTongueDto(MotherTongue e) {
        return MotherTongueResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private MaritalStatusResponseDTO toMaritalStatusDto(MaritalStatus e) {
        return MaritalStatusResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin() != null ? e.getAdmin().getId() : null)
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private ProfileTypeResponseDTO toProfileTypeDto(ProfileType e) {
        return ProfileTypeResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private HeightResponseDTO toHeightDto(Height e) {
        return HeightResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .height(e.getHeight())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private WeightResponseDTO toWeightDto(Weight e) {
        return WeightResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin() != null ? e.getAdmin().getId() : null)
                .value(e.getValue())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private IncomeResponseDTO toIncomeDto(Income e) {
        return IncomeResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .range(e.getRange())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private DietResponseDto toDietDto(Diet e) {
        DietResponseDto dto = new DietResponseDto();
        dto.setId(e.getId());
        dto.setAdminId(e.getAdmin().getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private SmokingResponseDTO toSmokingDto(Smoking e) {
        return SmokingResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .value(e.getValue())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private DrinkingResponseDto toDrinkingDto(Drinking e) {
        DrinkingResponseDto dto = new DrinkingResponseDto();
        dto.setId(e.getId());
        dto.setAdminId(e.getAdmin().getId());
        dto.setName(e.getName());
        dto.setValue(e.getValue());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private BodyTypeResponseDTO toBodyTypeDto(BodyType e) {
        BodyTypeResponseDTO dto = new BodyTypeResponseDTO();
        dto.setId(e.getId());
        dto.setAdminId(e.getAdmin().getId());
        dto.setValue(e.getValue());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private ComplexionResponseDTO toComplexionDto(Complexion e) {
        ComplexionResponseDTO dto = new ComplexionResponseDTO();
        dto.setId(e.getId());
        dto.setAdminId(e.getAdmin().getId());
        dto.setValue(e.getValue());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private FamilyResponseDto toFamilyDto(Family e) {
        return FamilyResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private FamilyStatusResponseDto toFamilyStatusDto(FamilyStatus e) {
        return FamilyStatusResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private FamilyTypeResponseDto toFamilyTypeDto(FamilyType e) {
        return FamilyTypeResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private FamilyValueResponseDto toFamilyValueDto(FamilyValue e) {
        return FamilyValueResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private FieldOfStudyResponseDTO toFieldOfStudyDto(FieldOfStudy e) {
        return FieldOfStudyResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private ManglikStatusResponseDTO toManglikStatusDto(ManglikStatus e) {
        return ManglikStatusResponseDTO.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private EmployedResponseDto toEmployedDto(Employed e) {
        return EmployedResponseDto.builder()
                .id(e.getId())
                .adminId(e.getAdmin().getId())
                .name(e.getName())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .deletedBy(e.getDeletedBy())
                .build();
    }

    private DisabilityStatusResponseDto toDisabilityStatusDto(DisabilityStatus e) {
        DisabilityStatusResponseDto dto = new DisabilityStatusResponseDto();
        dto.setId(e.getId());
        dto.setAdminId(e.getAdmin().getId());
        dto.setValue(e.getValue());
        dto.setIsActive(e.getIsActive());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private GenderResponseDTO toGenderDto(Gender e) {
        return GenderResponseDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .build();
    }

    private CountryResponseDTO toCountryDto(Country e) {
        CountryResponseDTO dto = new CountryResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private StateResponseDTO toStateDto(State e) {
        StateResponseDTO dto = new StateResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setCountryId(e.getCountry() != null ? e.getCountry().getId() : null);
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private CityResponseDTO toCityDto(City e) {
        CityResponseDTO dto = new CityResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setStateId(e.getState() != null ? e.getState().getId() : null);
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
