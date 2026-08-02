package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileCompletionSeeder {

    private final JdbcTemplate jdbcTemplate;
    private final MasterDataCache masterDataCache;

    @Value("${app.seeder.batch-size:50000}")
    private int batchSize;

    private static final ThreadLocalRandom RANDOM =
            ThreadLocalRandom.current();

    private static final List<String> COMPANIES = List.of(
            "TCS",
            "Infosys",
            "Wipro",
            "Accenture",
            "Capgemini",
            "Cognizant",
            "HCL",
            "Tech Mahindra"
    );

    private static final List<String> FATHER_NAMES = List.of(
            "Rajesh",
            "Mahesh",
            "Sunil",
            "Suresh",
            "Anil",
            "Ramesh",
            "Vijay",
            "Ashok"
    );

    private static final List<String> MOTHER_NAMES = List.of(
            "Sunita",
            "Asha",
            "Meena",
            "Anita",
            "Rekha",
            "Savita",
            "Usha",
            "Kavita"
    );

    private static final List<String> ABOUT = List.of(
            "Simple and family oriented person.",
            "Honest and caring by nature.",
            "Career focused with family values.",
            "Looking for a compatible life partner.",
            "Kind, responsible and ambitious."
    );

    private static final String UPDATE_PROFILE_SQL = """
        UPDATE profiles
        SET

            profile_type_id=?,
            manglik_status_id=?,

            family_type_id=?,
            family_status_id=?,
            family_value_id=?,

            qualification_id=?,
            field_of_study_id=?,
            employed_id=?,

            disability_status_id=?,
            blood_group_id=?,
            marital_status_id=?,

            education_level_id=?,
            occupation_id=?,
            income_id=?,

            height_id=?,
            weight_id=?,
            body_type_id=?,
            complexion_id=?,

            diet_id=?,
            smoking_id=?,
            drinking_id=?,

            company_name=?,

            father_name=?,
            father_occupation=?,

            mother_name=?,
            mother_occupation=?,

            siblings_count=?,

            about=?,
            about_me=?,

            boost_score=?,
            updated_at=?

        WHERE id=?
        """;

    @Transactional
    public void completeProfiles() {

        log.info("=================================");
        log.info("PROFILE COMPLETION STARTED");
        log.info("=================================");

        Long totalProfiles =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM profiles",
                        Long.class
                );

        if (totalProfiles == null || totalProfiles == 0) {

            log.warn("No profiles found.");
            return;
        }

        log.info("Total Profiles : {}", totalProfiles);

        for (long start = 1;
             start <= totalProfiles;
             start += batchSize) {

            long end =
                    Math.min(
                            start + batchSize - 1,
                            totalProfiles
                    );

            updateBatch(start, end);

            log.info(
                    "Completed Profiles {} -> {}",
                    start,
                    end
            );
        }

        log.info("=================================");
        log.info("PROFILE COMPLETION FINISHED");
        log.info("=================================");
    }

    @Transactional
    public void updateBatch(
            long start,
            long end
    ) {

        List<Long> profileIds =
                jdbcTemplate.query(
                        """
                        SELECT id
                        FROM profiles
                        WHERE id BETWEEN ? AND ?
                        ORDER BY id
                        """,
                        (rs, rowNum) ->
                                rs.getLong("id"),
                        start,
                        end
                );

        LocalDateTime now =
                LocalDateTime.now();

        List<ProfileUpdate> updates =
                new ArrayList<>(profileIds.size());

        for (Long profileId : profileIds) {

            updates.add(

                    new ProfileUpdate(

                            profileId,

                            masterDataCache.randomProfileType(),
                            masterDataCache.randomManglikStatus(),

                            masterDataCache.randomFamilyType(),
                            masterDataCache.randomFamilyStatus(),
                            masterDataCache.randomFamilyValue(),

                            masterDataCache.randomQualification(),
                            masterDataCache.randomFieldOfStudy(),
                            masterDataCache.randomEmployed(),

                            masterDataCache.randomDisabilityStatus(),
                            masterDataCache.randomBloodGroup(),
                            masterDataCache.randomMaritalStatus(),

                            masterDataCache.randomEducationLevel(),
                            masterDataCache.randomOccupation(),
                            masterDataCache.randomIncome(),

                            masterDataCache.randomHeight(),
                            masterDataCache.randomWeight(),
                            masterDataCache.randomBodyType(),
                            masterDataCache.randomComplexion(),

                            masterDataCache.randomDiet(),
                            masterDataCache.randomSmoking(),
                            masterDataCache.randomDrinking(),

                            randomCompany(),
                            randomFather(),
                            "Business",

                            randomMother(),
                            "Teacher",

                            RANDOM.nextInt(0,5),

                            randomAbout(),
                            randomAbout(),

                            RANDOM.nextInt(0,11),

                            now

                    )

            );

        }
        jdbcTemplate.batchUpdate(
                UPDATE_PROFILE_SQL,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(
                            PreparedStatement ps,
                            int index
                    ) throws SQLException {

                        ProfileUpdate p = updates.get(index);

                        int i = 1;

                        ps.setObject(i++, p.profileTypeId());
                        ps.setObject(i++, p.manglikStatusId());

                        ps.setObject(i++, p.familyTypeId());
                        ps.setObject(i++, p.familyStatusId());
                        ps.setObject(i++, p.familyValueId());

                        ps.setObject(i++, p.qualificationId());
                        ps.setObject(i++, p.fieldOfStudyId());
                        ps.setObject(i++, p.employedId());

                        ps.setObject(i++, p.disabilityStatusId());
                        ps.setObject(i++, p.bloodGroupId());
                        ps.setObject(i++, p.maritalStatusId());

                        ps.setObject(i++, p.educationLevelId());
                        ps.setObject(i++, p.occupationId());
                        ps.setObject(i++, p.incomeId());

                        ps.setObject(i++, p.heightId());
                        ps.setObject(i++, p.weightId());
                        ps.setObject(i++, p.bodyTypeId());
                        ps.setObject(i++, p.complexionId());

                        ps.setObject(i++, p.dietId());
                        ps.setObject(i++, p.smokingId());
                        ps.setObject(i++, p.drinkingId());

                        ps.setString(i++, p.companyName());

                        ps.setString(i++, p.fatherName());
                        ps.setString(i++, p.fatherOccupation());

                        ps.setString(i++, p.motherName());
                        ps.setString(i++, p.motherOccupation());

                        ps.setInt(i++, p.siblingsCount());

                        ps.setString(i++, p.about());
                        ps.setString(i++, p.aboutMe());

                        ps.setInt(i++, p.boostScore());

                        ps.setTimestamp(
                                i++,
                                Timestamp.valueOf(p.updatedAt())
                        );

                        ps.setLong(i++, p.profileId());
                    }

                    @Override
                    public int getBatchSize() {
                        return updates.size();
                    }
                }
        );
    }

    private String randomCompany() {

        return COMPANIES.get(
                RANDOM.nextInt(COMPANIES.size())
        );
    }

    private String randomFather() {

        return FATHER_NAMES.get(
                RANDOM.nextInt(FATHER_NAMES.size())
        );
    }

    private String randomMother() {

        return MOTHER_NAMES.get(
                RANDOM.nextInt(MOTHER_NAMES.size())
        );
    }

    private String randomAbout() {

        return ABOUT.get(
                RANDOM.nextInt(ABOUT.size())
        );
    }

    private record ProfileUpdate(

            Long profileId,

            Long profileTypeId,
            Long manglikStatusId,

            Long familyTypeId,
            Long familyStatusId,
            Long familyValueId,

            Long qualificationId,
            Long fieldOfStudyId,
            Long employedId,

            Long disabilityStatusId,
            Long bloodGroupId,
            Long maritalStatusId,

            Long educationLevelId,
            Long occupationId,
            Long incomeId,

            Long heightId,
            Long weightId,
            Long bodyTypeId,
            Long complexionId,

            Long dietId,
            Long smokingId,
            Long drinkingId,

            String companyName,

            String fatherName,
            String fatherOccupation,

            String motherName,
            String motherOccupation,

            int siblingsCount,

            String about,
            String aboutMe,

            int boostScore,

            LocalDateTime updatedAt

    ) {
    }

}