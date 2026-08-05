import re

with open('src/main/java/com/example/service/MasterDataCacheService.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Extract keys
keys_section = re.search(r'(private static final String RELIGION_KEY = .*?private static final String KEY_GENDER = GENDER_KEY;)', content, re.DOTALL)
keys = keys_section.group(1) if keys_section else ''

# Extract repositories
repos_section = re.search(r'(private final ReligionRepository.*?private final GenderRepository genderRepository;)', content, re.DOTALL)
repos = repos_section.group(1) if repos_section else ''

# Extract toXxxDto methods
mappers_section = re.search(r'(private ReligionResponseDTO toReligionDto.*?} *\n})', content, re.DOTALL)
mappers = mappers_section.group(1) if mappers_section else ''
mappers = mappers.rsplit('}', 1)[0].strip() # remove the last closing brace of the class

# Write the new file
new_content = f'''package com.example.service;

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
public class MasterDataCacheService {{

    private static final Duration CACHE_TTL = Duration.ofHours(12);

    {keys}

    private final RedisTemplate<String, Object> redisTemplate;
    private final Executor taskExecutor;

    {repos}

    @PostConstruct
    public void initializeCache() {{
        log.info("==========================================");
        log.info("Initializing Master Data Cache...");
        log.info("==========================================");
        warmUpCache();
    }}

    @Scheduled(cron = "0 0 */6 * * *")
    public void refreshMasterCache() {{
        log.info("Refreshing Master Cache...");
        warmUpCache();
    }}

    public void warmUpCache() {{
        CompletableFuture.runAsync(() -> {{
            try {{
                put(KEY_RELIGION, fetchReligionsFromDb());
                put(KEY_CASTE, fetchCastesFromDb());
                put(KEY_SUBCASTE, fetchSubCastesFromDb());
                put(KEY_COUNTRY, fetchCountriesFromDb());
                put(KEY_STATE, fetchStatesFromDb());
                put(KEY_CITY, fetchCitiesFromDb());
                put(KEY_OCCUPATION, fetchOccupationsFromDb());
                put(KEY_EDUCATION, fetchEducationLevelsFromDb());
                put(KEY_QUALIFICATION, fetchQualificationsFromDb());
                put(KEY_MOTHER_TONGUE, fetchMotherTonguesFromDb());
                put(KEY_MARITAL_STATUS, fetchMaritalStatusFromDb());
                put(KEY_PROFILE_TYPE, fetchProfileTypesFromDb());
                put(KEY_HEIGHT, fetchHeightsFromDb());
                put(KEY_WEIGHT, fetchWeightsFromDb());
                put(KEY_INCOME, fetchIncomeFromDb());
                put(KEY_DIET, fetchDietsFromDb());
                put(KEY_SMOKING, fetchSmokingFromDb());
                put(KEY_DRINKING, fetchDrinkingFromDb());
                put(KEY_BODY_TYPE, fetchBodyTypesFromDb());
                put(KEY_COMPLEXION, fetchComplexionsFromDb());
                put(KEY_FAMILY, fetchFamiliesFromDb());
                put(KEY_FAMILY_STATUS, fetchFamilyStatusFromDb());
                put(KEY_FAMILY_TYPE, fetchFamilyTypesFromDb());
                put(KEY_FAMILY_VALUE, fetchFamilyValuesFromDb());
                put(KEY_FIELD_OF_STUDY, fetchFieldOfStudiesFromDb());
                put(KEY_MANGLIK_STATUS, fetchManglikStatusFromDb());
                put(KEY_EMPLOYED, fetchEmployedFromDb());
                put(KEY_DISABILITY_STATUS, fetchDisabilityStatusFromDb());
                put(KEY_GENDER, fetchGenderFromDb());
            }} catch (Exception e) {{
                log.error("Cache warmup failed", e);
            }}
        }}, taskExecutor);
    }}

    private void put(String key, Object value) {{
        try {{
            redisTemplate.opsForValue().set(key, value, CACHE_TTL);
        }} catch (Exception e) {{
            log.error("Failed to put data into Redis for key: {{}}", key, e);
        }}
    }}

    @SuppressWarnings("unchecked")
    private <T> List<T> getOrFetch(String key, Supplier<List<T>> dbFetcher) {{
        try {{
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj != null) {{
                return (List<T>) obj;
            }}
        }} catch (Exception e) {{
            log.error("Redis get failed for key: {{}}. Falling back to database.", key, e);
        }}

        List<T> data = null;
        try {{
            data = dbFetcher.get();
        }} catch (Exception e) {{
            log.error("Database fetch failed for key: {{}}", key, e);
            return Collections.emptyList();
        }}

        if (data != null && !data.isEmpty()) {{
            final List<T> finalData = data;
            CompletableFuture.runAsync(() -> put(key, finalData), taskExecutor);
        }}

        return data != null ? data : Collections.emptyList();
    }}

    public void evict(String key) {{
        try {{
            redisTemplate.delete(key);
        }} catch (Exception e) {{
            log.error("Failed to evict key: {{}}", key, e);
        }}
    }}

    public List<ReligionResponseDTO> getReligions() {{ return getOrFetch(KEY_RELIGION, this::fetchReligionsFromDb); }}
    public List<CasteResponseDTO> getCastes() {{ return getOrFetch(KEY_CASTE, this::fetchCastesFromDb); }}
    public List<SubCasteResponseDTO> getSubCastes() {{ return getOrFetch(KEY_SUBCASTE, this::fetchSubCastesFromDb); }}
    public List<CountryResponseDTO> getCountries() {{ return getOrFetch(KEY_COUNTRY, this::fetchCountriesFromDb); }}
    public List<StateResponseDTO> getStates() {{ return getOrFetch(KEY_STATE, this::fetchStatesFromDb); }}
    public List<CityResponseDTO> getCities() {{ return getOrFetch(KEY_CITY, this::fetchCitiesFromDb); }}
    public List<OccupationResponseDTO> getOccupations() {{ return getOrFetch(KEY_OCCUPATION, this::fetchOccupationsFromDb); }}
    public List<EducationLevelResponseDto> getEducationLevels() {{ return getOrFetch(KEY_EDUCATION, this::fetchEducationLevelsFromDb); }}
    public List<QualificationResponseDTO> getQualifications() {{ return getOrFetch(KEY_QUALIFICATION, this::fetchQualificationsFromDb); }}
    public List<MotherTongueResponseDTO> getMotherTongues() {{ return getOrFetch(KEY_MOTHER_TONGUE, this::fetchMotherTonguesFromDb); }}
    public List<MaritalStatusResponseDTO> getMaritalStatus() {{ return getOrFetch(KEY_MARITAL_STATUS, this::fetchMaritalStatusFromDb); }}
    public List<ProfileTypeResponseDTO> getProfileTypes() {{ return getOrFetch(KEY_PROFILE_TYPE, this::fetchProfileTypesFromDb); }}
    public List<HeightResponseDTO> getHeights() {{ return getOrFetch(KEY_HEIGHT, this::fetchHeightsFromDb); }}
    public List<WeightResponseDTO> getWeights() {{ return getOrFetch(KEY_WEIGHT, this::fetchWeightsFromDb); }}
    public List<IncomeResponseDTO> getIncome() {{ return getOrFetch(KEY_INCOME, this::fetchIncomeFromDb); }}
    public List<DietResponseDto> getDiets() {{ return getOrFetch(KEY_DIET, this::fetchDietsFromDb); }}
    public List<SmokingResponseDTO> getSmoking() {{ return getOrFetch(KEY_SMOKING, this::fetchSmokingFromDb); }}
    public List<DrinkingResponseDto> getDrinking() {{ return getOrFetch(KEY_DRINKING, this::fetchDrinkingFromDb); }}
    public List<BodyTypeResponseDTO> getBodyTypes() {{ return getOrFetch(KEY_BODY_TYPE, this::fetchBodyTypesFromDb); }}
    public List<ComplexionResponseDTO> getComplexions() {{ return getOrFetch(KEY_COMPLEXION, this::fetchComplexionsFromDb); }}
    public List<FamilyResponseDto> getFamilies() {{ return getOrFetch(KEY_FAMILY, this::fetchFamiliesFromDb); }}
    public List<FamilyStatusResponseDto> getFamilyStatus() {{ return getOrFetch(KEY_FAMILY_STATUS, this::fetchFamilyStatusFromDb); }}
    public List<FamilyTypeResponseDto> getFamilyTypes() {{ return getOrFetch(KEY_FAMILY_TYPE, this::fetchFamilyTypesFromDb); }}
    public List<FamilyValueResponseDto> getFamilyValues() {{ return getOrFetch(KEY_FAMILY_VALUE, this::fetchFamilyValuesFromDb); }}
    public List<FieldOfStudyResponseDTO> getFieldOfStudies() {{ return getOrFetch(KEY_FIELD_OF_STUDY, this::fetchFieldOfStudiesFromDb); }}
    public List<ManglikStatusResponseDTO> getManglikStatus() {{ return getOrFetch(KEY_MANGLIK_STATUS, this::fetchManglikStatusFromDb); }}
    public List<EmployedResponseDto> getEmployed() {{ return getOrFetch(KEY_EMPLOYED, this::fetchEmployedFromDb); }}
    public List<DisabilityStatusResponseDto> getDisabilityStatus() {{ return getOrFetch(KEY_DISABILITY_STATUS, this::fetchDisabilityStatusFromDb); }}
    public List<GenderResponseDTO> getGender() {{ return getOrFetch(KEY_GENDER, this::fetchGenderFromDb); }}

    private List<ReligionResponseDTO> fetchReligionsFromDb() {{ return religionRepository.findAllByDeletedAtIsNull().stream().map(this::toReligionDto).toList(); }}
    private List<CasteResponseDTO> fetchCastesFromDb() {{ return casteRepository.findAllByDeletedAtIsNull().stream().map(this::toCasteDto).toList(); }}
    private List<SubCasteResponseDTO> fetchSubCastesFromDb() {{ return subCasteRepository.findAllWithRelations().stream().map(this::toSubCasteDto).toList(); }}
    private List<CountryResponseDTO> fetchCountriesFromDb() {{ return countryRepository.findByDeletedAtIsNull().stream().map(this::toCountryDto).toList(); }}
    private List<StateResponseDTO> fetchStatesFromDb() {{ return stateRepository.findAllByDeletedAtIsNull().stream().map(this::toStateDto).toList(); }}
    private List<CityResponseDTO> fetchCitiesFromDb() {{ return cityRepository.findByDeletedAtIsNull().stream().map(this::toCityDto).toList(); }}
    private List<OccupationResponseDTO> fetchOccupationsFromDb() {{ return occupationRepository.findAllByDeletedAtIsNull().stream().map(this::toOccupationDto).toList(); }}
    private List<EducationLevelResponseDto> fetchEducationLevelsFromDb() {{ return educationLevelRepository.findAllByDeletedAtIsNull().stream().map(this::toEducationDto).toList(); }}
    private List<QualificationResponseDTO> fetchQualificationsFromDb() {{ return qualificationRepository.findAllByDeletedAtIsNull().stream().map(this::toQualificationDto).toList(); }}
    private List<MotherTongueResponseDTO> fetchMotherTonguesFromDb() {{ return motherTongueRepository.findAllByDeletedAtIsNull().stream().map(this::toMotherTongueDto).toList(); }}
    private List<MaritalStatusResponseDTO> fetchMaritalStatusFromDb() {{ return maritalStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toMaritalStatusDto).toList(); }}
    private List<ProfileTypeResponseDTO> fetchProfileTypesFromDb() {{ return profileTypeRepository.findAllByDeletedAtIsNull().stream().map(this::toProfileTypeDto).toList(); }}
    private List<HeightResponseDTO> fetchHeightsFromDb() {{ return heightRepository.findAllByDeletedAtIsNull().stream().map(this::toHeightDto).toList(); }}
    private List<WeightResponseDTO> fetchWeightsFromDb() {{ return weightRepository.findAllByDeletedAtIsNull().stream().map(this::toWeightDto).toList(); }}
    private List<IncomeResponseDTO> fetchIncomeFromDb() {{ return incomeRepository.findAllByDeletedAtIsNull().stream().map(this::toIncomeDto).toList(); }}
    private List<DietResponseDto> fetchDietsFromDb() {{ return dietRepository.findByDeletedAtIsNull().stream().map(this::toDietDto).toList(); }}
    private List<SmokingResponseDTO> fetchSmokingFromDb() {{ return smokingRepository.findAllByDeletedAtIsNull().stream().map(this::toSmokingDto).toList(); }}
    private List<DrinkingResponseDto> fetchDrinkingFromDb() {{ return drinkingRepository.findByDeletedAtIsNull().stream().map(this::toDrinkingDto).toList(); }}
    private List<BodyTypeResponseDTO> fetchBodyTypesFromDb() {{ return bodyTypeRepository.findByDeletedAtIsNull().stream().map(this::toBodyTypeDto).toList(); }}
    private List<ComplexionResponseDTO> fetchComplexionsFromDb() {{ return complexionRepository.findByDeletedAtIsNull().stream().map(this::toComplexionDto).toList(); }}
    private List<FamilyResponseDto> fetchFamiliesFromDb() {{ return familyRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyDto).toList(); }}
    private List<FamilyStatusResponseDto> fetchFamilyStatusFromDb() {{ return familyStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyStatusDto).toList(); }}
    private List<FamilyTypeResponseDto> fetchFamilyTypesFromDb() {{ return familyTypeRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyTypeDto).toList(); }}
    private List<FamilyValueResponseDto> fetchFamilyValuesFromDb() {{ return familyValueRepository.findAllByDeletedAtIsNull().stream().map(this::toFamilyValueDto).toList(); }}
    private List<FieldOfStudyResponseDTO> fetchFieldOfStudiesFromDb() {{ return fieldOfStudyRepository.findAllByDeletedAtIsNull().stream().map(this::toFieldOfStudyDto).toList(); }}
    private List<ManglikStatusResponseDTO> fetchManglikStatusFromDb() {{ return manglikStatusRepository.findAllByDeletedAtIsNull().stream().map(this::toManglikStatusDto).toList(); }}
    private List<EmployedResponseDto> fetchEmployedFromDb() {{ return employedRepository.findAllByDeletedAtIsNull().stream().map(this::toEmployedDto).toList(); }}
    private List<DisabilityStatusResponseDto> fetchDisabilityStatusFromDb() {{ return disabilityStatusRepository.findByDeletedAtIsNull().stream().map(this::toDisabilityStatusDto).toList(); }}
    private List<GenderResponseDTO> fetchGenderFromDb() {{ return genderRepository.findByIsActiveTrue().stream().map(this::toGenderDto).toList(); }}

    {mappers}

    private CountryResponseDTO toCountryDto(Country e) {{
        CountryResponseDTO dto = new CountryResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }}

    private StateResponseDTO toStateDto(State e) {{
        StateResponseDTO dto = new StateResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setCountryId(e.getCountry() != null ? e.getCountry().getId() : null);
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }}

    private CityResponseDTO toCityDto(City e) {{
        CityResponseDTO dto = new CityResponseDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setIsActive(e.getIsActive());
        dto.setStateId(e.getState() != null ? e.getState().getId() : null);
        dto.setAdminId(e.getAdmin() != null ? e.getAdmin().getId() : null);
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }}
}}
'''

with open('src/main/java/com/example/service/MasterDataCacheService.java', 'w', encoding='utf-8') as f:
    f.write(new_content)

print("Redesign applied successfully.")
