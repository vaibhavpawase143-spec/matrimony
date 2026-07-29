package com.example.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MasterDataCache {

    private final JdbcTemplate jdbcTemplate;

    private final Random random = new Random();

    // ===========================
    // MASTER ID LISTS
    // ===========================

    private List<Long> genderIds = new ArrayList<>();
    private List<Long> religionIds = new ArrayList<>();
    private List<Long> casteIds = new ArrayList<>();
    private List<Long> subCasteIds = new ArrayList<>();
    private List<Long> countryIds = new ArrayList<>();
    private List<Long> stateIds = new ArrayList<>();
    private List<Long> cityIds = new ArrayList<>();
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
    @PostConstruct
    public void loadMasterData() {

        genderIds = loadIds("genders");
        religionIds = loadIds("religions");
        casteIds = loadIds("castes");
        subCasteIds = loadIds("sub_castes");
        countryIds = loadIds("countries");
        stateIds = loadIds("states");
        cityIds = loadIds("cities");
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
        System.out.println("========================================");
        System.out.println("Master Data Loaded Successfully");
        System.out.println("Religion : " + religionIds.size());
        System.out.println("Caste : " + casteIds.size());
        System.out.println("City : " + cityIds.size());
        System.out.println("Occupation : " + occupationIds.size());
        System.out.println("========================================");
    }
    private List<Long> loadIds(String tableName) {

        return jdbcTemplate.query(
                "SELECT id FROM " + tableName + " ORDER BY id",
                (rs, rowNum) -> rs.getLong("id")
        );

    }public Long randomProfileType(){
        return getRandomId(profileTypeIds);
    }

    public Long randomManglikStatus(){
        return getRandomId(manglikStatusIds);
    }

    public Long randomFamilyType(){
        return getRandomId(familyTypeIds);
    }

    public Long randomFamilyStatus(){
        return getRandomId(familyStatusIds);
    }

    public Long randomFamilyValue(){
        return getRandomId(familyValueIds);
    }

    public Long randomQualification(){
        return getRandomId(qualificationIds);
    }

    public Long randomFieldOfStudy(){
        return getRandomId(fieldOfStudyIds);
    }

    public Long randomEmployed(){
        return getRandomId(employedIds);
    }

    public Long randomDisabilityStatus(){
        return getRandomId(disabilityStatusIds);
    }

    public Long randomBloodGroup(){
        return getRandomId(bloodGroupIds);
    }

    public Long randomMaritalStatus(){
        return getRandomId(maritalStatusIds);
    }
    private Long getRandomId(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new IllegalStateException("Master data not loaded.");
        }

        return ids.get(random.nextInt(ids.size()));
    }
    public Long randomGender() {
        return getRandomId(genderIds);
    }

    public Long randomReligion() {
        return getRandomId(religionIds);
    }

    public Long randomCaste() {
        return getRandomId(casteIds);
    }

    public Long randomSubCaste() {
        return getRandomId(subCasteIds);
    }

    public Long randomCountry() {
        return getRandomId(countryIds);
    }

    public Long randomState() {
        return getRandomId(stateIds);
    }

    public Long randomCity() {
        return getRandomId(cityIds);
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
}