package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileCompletionSeeder {


    private final JdbcTemplate jdbcTemplate;

    private final MasterDataCache masterDataCache;


    @Value("${app.seeder.batch-size}")
    private int batchSize;



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

      is_premium=?,
              premium_plan=?,
              premium_start_date=?,
              premium_end_date=?,
            
              boost_score=?,
              updated_at=?

    WHERE id=?
    """;




    public void completeProfiles(){


        log.info("=================================");
        log.info("Starting Profile Completion");
        log.info("=================================");


        Long totalProfiles =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM profiles",
                        Long.class
                );


        log.info("Total Profiles : {}", totalProfiles);



        for(long start = 1; start <= totalProfiles; start += batchSize){


            long end =
                    Math.min(
                            start + batchSize - 1,
                            totalProfiles
                    );


            updateBatch(start,end);


            log.info(
                    "Completed Profiles : {} -> {}",
                    start,
                    end
            );

        }


        log.info("Profile Completion Finished");

    }



@Transactional
    public void updateBatch(
            long start,
            long end
    ){


        List<Long> profileIds =
                jdbcTemplate.query(
                        """
                        SELECT id
                        FROM profiles
                        WHERE id BETWEEN ? AND ?
                        ORDER BY id
                        """,
                        (rs,row)->rs.getLong("id"),
                        start,
                        end
                );



        List<Object[]> updates =
                new ArrayList<>();



        for(Long profileId : profileIds){


            double random = Math.random();

            String premiumPlan;
            boolean premium;
            LocalDateTime premiumStartDate = null;
            LocalDateTime premiumEndDate = null;

            if (random < 0.90) {

                premium = false;
                premiumPlan = "FREE";

            }
            else if (random < 0.95) {

                premium = true;
                premiumPlan = "ONE_MONTH";

            }
            else if (random < 0.98) {

                premium = true;
                premiumPlan = "THREE_MONTHS";

            }
            else if (random < 0.995) {

                premium = true;
                premiumPlan = "SIX_MONTHS";

            }
            else {

                premium = true;
                premiumPlan = "TWELVE_MONTHS";

            }

            if(premium){

                premiumStartDate = LocalDateTime.now();


                switch(premiumPlan){

                    case "ONE_MONTH":
                        premiumEndDate = premiumStartDate.plusMonths(1);
                        break;


                    case "THREE_MONTHS":
                        premiumEndDate = premiumStartDate.plusMonths(3);
                        break;


                    case "SIX_MONTHS":
                        premiumEndDate = premiumStartDate.plusMonths(6);
                        break;


                    case "TWELVE_MONTHS":
                        premiumEndDate = premiumStartDate.plusMonths(12);
                        break;

                }

            }
            if(profileId <= 20){

                log.info(
                        "Profile {} => premium={} plan={}",
                        profileId,
                        premium,
                        premiumPlan
                );

            }
            updates.add(new Object[]{


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


                    "TCS",


                    "Rajesh",

                    "Business",


                    "Sunita",

                    "Teacher",


                    2,


                    "Simple, caring and family oriented person.",

                    "Looking for a compatible life partner with mutual respect.",



                    premium,

                    premiumPlan,

                    premiumStartDate,

                    premiumEndDate,

                    premium ? 10 : 0,

                    LocalDateTime.now(),


                    profileId

            });

        }



        jdbcTemplate.batchUpdate(
                UPDATE_PROFILE_SQL,
                updates
        );

    }

}