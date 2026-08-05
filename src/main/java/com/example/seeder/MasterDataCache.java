package com.example.seeder;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class MasterDataCache {

    private final JdbcTemplate jdbcTemplate;

    // =====================================================
    // RANDOM
    // =====================================================

    private final Random random = new Random();

    // =====================================================
    // MASTER ID LISTS
    // =====================================================

    private List<Long> genderIds = new ArrayList<>();

    private List<Long> religionIds = new ArrayList<>();

    private List<Long> countryIds = new ArrayList<>();

    private List<Long> motherTongueIds = new ArrayList<>();

    private List<Long> educationLevelIds = new ArrayList<>();

    private List<Long> occupationIds = new ArrayList<>();

    private List<Long> heightIds = new ArrayList<>();

    private List<Long> weightIds = new ArrayList<>();

    private List<Long> bodyTypeIds = new ArrayList<>();

    private List<Long> complexionIds = new ArrayList<>();

    private List<Long> incomeIds = new ArrayList<>();

    private List<Long> dietIds = new ArrayList<>();

    private List<Long> smokingIds = new ArrayList<>();

    private List<Long> drinkingIds = new ArrayList<>();

    private List<Long> profileTypeIds = new ArrayList<>();

    private List<Long> manglikStatusIds = new ArrayList<>();

    private List<Long> familyTypeIds = new ArrayList<>();

    private List<Long> familyStatusIds = new ArrayList<>();

    private List<Long> familyValueIds = new ArrayList<>();

    private List<Long> qualificationIds = new ArrayList<>();

    private List<Long> fieldOfStudyIds = new ArrayList<>();

    private List<Long> employedIds = new ArrayList<>();

    private List<Long> disabilityStatusIds = new ArrayList<>();

    private List<Long> bloodGroupIds = new ArrayList<>();

    private List<Long> maritalStatusIds = new ArrayList<>();


    // =====================================================
    // RELATIONSHIP MAPS
    // =====================================================

    /**
     * religion_id -> caste ids
     */
    private final Map<Long, List<Long>> religionToCastes = new HashMap<>();

    /**
     * caste_id -> sub caste ids
     */
    private final Map<Long, List<Long>> casteToSubCastes = new HashMap<>();

    /**
     * country_id -> state ids
     */
    private final Map<Long, List<Long>> countryToStates = new HashMap<>();

    /**
     * state_id -> city ids
     */
    private final Map<Long, List<Long>> stateToCities = new HashMap<>();


    // =====================================================
    // INITIAL LOAD
    // =====================================================

    @PostConstruct
    public void loadMasterData() {

        log.info("==========================================");
        log.info("Loading Master Data...");
        log.info("==========================================");

        genderIds = loadIds("genders");

        religionIds = loadIds("religions");

        countryIds = loadIds("countries");

        motherTongueIds = loadIds("mother_tongues");

        educationLevelIds = loadIds("education_levels");

        occupationIds = loadIds("occupations");

        heightIds = loadIds("heights");

        weightIds = loadIds("weights");

        bodyTypeIds = loadIds("body_types");

        complexionIds = loadIds("complexions");

        incomeIds = loadIds("incomes");

        dietIds = loadIds("diets");

        smokingIds = loadIds("smoking");

        drinkingIds = loadIds("drinking");

        profileTypeIds = loadIds("profile_types");

        manglikStatusIds = loadIds("manglik_status");

        familyTypeIds = loadIds("family_types");

        familyStatusIds = loadIds("family_status");

        familyValueIds = loadIds("family_values");

        qualificationIds = loadIds("qualifications");

        fieldOfStudyIds = loadIds("fields_of_study");

        employedIds = loadIds("employed");

        disabilityStatusIds = loadIds("disability_statuses");

        bloodGroupIds = loadIds("blood_groups");

        maritalStatusIds = loadIds("marital_status");
        // ==========================================
        // LOAD RELATIONSHIP MAPS
        // ==========================================

        loadReligionToCastes();

        loadCasteToSubCastes();

        loadCountryToStates();

        loadStateToCities();

        log.info("==========================================");
        log.info("Master Data Loaded Successfully");
        log.info("Religions      : {}", religionIds.size());
        log.info("Countries      : {}", countryIds.size());
        log.info("Religion->Caste: {}", religionToCastes.size());
        log.info("Caste->SubCast : {}", casteToSubCastes.size());
        log.info("Country->State : {}", countryToStates.size());
        log.info("State->City    : {}", stateToCities.size());
        log.info("==========================================");
    }

    // =====================================================
    // LOAD SIMPLE MASTER IDS
    // =====================================================

    private List<Long> loadIds(String table) {

        return jdbcTemplate.query(

                "SELECT id FROM " + table + " WHERE is_active=true ORDER BY id",

                (rs, rowNum) -> rs.getLong("id")

        );

    }

    // =====================================================
    // RELIGION -> CASTE
    // =====================================================

    private void loadReligionToCastes() {

        jdbcTemplate.query(

                """
                SELECT
                    id,
                    religion_id
                FROM castes
                WHERE is_active = true
                ORDER BY religion_id,id
                """,

                rs -> {

                    Long casteId = rs.getLong("id");

                    Long religionId = rs.getLong("religion_id");

                    religionToCastes

                            .computeIfAbsent(
                                    religionId,
                                    k -> new ArrayList<>()
                            )

                            .add(casteId);

                }

        );

    }

    // =====================================================
    // CASTE -> SUB CASTE
    // =====================================================

    private void loadCasteToSubCastes() {

        jdbcTemplate.query(

                """
                SELECT
                    id,
                    caste_id
                FROM sub_castes
                WHERE is_active = true
                ORDER BY caste_id,id
                """,

                rs -> {

                    Long subCasteId = rs.getLong("id");

                    Long casteId = rs.getLong("caste_id");

                    casteToSubCastes

                            .computeIfAbsent(
                                    casteId,
                                    k -> new ArrayList<>()
                            )

                            .add(subCasteId);

                }

        );

    }

    // =====================================================
    // COUNTRY -> STATE
    // =====================================================

    private void loadCountryToStates() {

        jdbcTemplate.query(

                """
                SELECT
                    id,
                    country_id
                FROM states
                WHERE is_active = true
                ORDER BY country_id,id
                """,

                rs -> {

                    Long stateId = rs.getLong("id");

                    Long countryId = rs.getLong("country_id");

                    countryToStates

                            .computeIfAbsent(
                                    countryId,
                                    k -> new ArrayList<>()
                            )

                            .add(stateId);

                }

        );

    }

    // =====================================================
    // STATE -> CITY
    // =====================================================

    private void loadStateToCities() {

        jdbcTemplate.query(

                """
                SELECT
                    id,
                    state_id
                FROM cities
                WHERE is_active = true
                ORDER BY state_id,id
                """,

                rs -> {

                    Long cityId = rs.getLong("id");

                    Long stateId = rs.getLong("state_id");

                    stateToCities

                            .computeIfAbsent(
                                    stateId,
                                    k -> new ArrayList<>()
                            )

                            .add(cityId);

                }

        );

    }
    // =====================================================
    // GENERIC RANDOM HELPER
    // =====================================================

    private Long getRandomId(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return null;
        }

        return ids.get(ThreadLocalRandom.current().nextInt(ids.size()));
    }

    // =====================================================
    // RELIGION
    // =====================================================

    public Long randomReligion() {

        List<Long> validReligions = new ArrayList<>();

        for (Long religionId : religionIds) {
            if (hasCastes(religionId)) {
                validReligions.add(religionId);
            }
        }

        return getRandomId(validReligions);
    }

    public Long randomCaste(Long religionId) {

        if (religionId == null) {
            return null;
        }

        List<Long> castes = religionToCastes.get(religionId);

        if (castes == null || castes.isEmpty()) {
            return null;
        }

        return getRandomId(castes);
    }

    public Long randomSubCaste(Long casteId) {

        List<Long> subCastes = casteToSubCastes.get(casteId);

        if (subCastes == null || subCastes.isEmpty()) {
            return null;
        }

        return getRandomId(subCastes);

    }

    // =====================================================
    // LOCATION
    // =====================================================

    public Long randomCountry() {
        return getRandomId(countryIds);
    }

    public Long randomState(Long countryId) {

        List<Long> states = countryToStates.get(countryId);

        if (states == null || states.isEmpty()) {
            return null;
        }

        List<Long> validStates = new ArrayList<>();

        for (Long stateId : states) {
            if (hasCities(stateId)) {
                validStates.add(stateId);
            }
        }

        return getRandomId(validStates);
    }

    public Long randomCity(Long stateId) {

        List<Long> cities = stateToCities.get(stateId);

        if (cities == null || cities.isEmpty()) {
            return null;
        }

        return getRandomId(cities);

    }

    // =====================================================
    // OTHER MASTER DATA
    // =====================================================

    public Long randomGender() {
        return getRandomId(genderIds);
    }

    public Long randomMotherTongue() {
        return getRandomId(motherTongueIds);
    }

    public Long randomEducationLevel() {
        return getRandomId(educationLevelIds);
    }

    public Long randomOccupation() {
        return getRandomId(occupationIds);
    }

    public Long randomHeight() {
        return getRandomId(heightIds);
    }

    public Long randomWeight() {
        return getRandomId(weightIds);
    }

    public Long randomBodyType() {
        return getRandomId(bodyTypeIds);
    }

    public Long randomComplexion() {
        return getRandomId(complexionIds);
    }

    public Long randomIncome() {
        return getRandomId(incomeIds);
    }

    public Long randomDiet() {
        return getRandomId(dietIds);
    }

    public Long randomSmoking() {
        return getRandomId(smokingIds);
    }

    public Long randomDrinking() {
        return getRandomId(drinkingIds);
    }

    public Long randomProfileType() {
        return getRandomId(profileTypeIds);
    }

    public Long randomManglikStatus() {
        return getRandomId(manglikStatusIds);
    }

    public Long randomFamilyType() {
        return getRandomId(familyTypeIds);
    }

    public Long randomFamilyStatus() {
        return getRandomId(familyStatusIds);
    }

    public Long randomFamilyValue() {
        return getRandomId(familyValueIds);
    }

    public Long randomQualification() {
        return getRandomId(qualificationIds);
    }

    public Long randomFieldOfStudy() {
        return getRandomId(fieldOfStudyIds);
    }

    public Long randomEmployed() {
        return getRandomId(employedIds);
    }

    public Long randomDisabilityStatus() {
        return getRandomId(disabilityStatusIds);
    }

    public Long randomBloodGroup() {
        return getRandomId(bloodGroupIds);
    }

    public Long randomMaritalStatus() {
        return getRandomId(maritalStatusIds);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    public boolean hasCastes(Long religionId) {

        return religionToCastes.containsKey(religionId)
                && !religionToCastes.get(religionId).isEmpty();

    }

    public boolean hasSubCastes(Long casteId) {

        return casteToSubCastes.containsKey(casteId)
                && !casteToSubCastes.get(casteId).isEmpty();

    }

    public boolean hasStates(Long countryId) {

        return countryToStates.containsKey(countryId)
                && !countryToStates.get(countryId).isEmpty();

    }

    public boolean hasCities(Long stateId) {

        return stateToCities.containsKey(stateId)
                && !stateToCities.get(stateId).isEmpty();

    }

}