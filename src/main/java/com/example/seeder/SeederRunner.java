package com.example.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SeederRunner implements CommandLineRunner {


    private final TestUserSeeder testUserSeeder;

    private final ProfileCompletionSeeder profileCompletionSeeder;



    @Value("${app.user-seeder.enabled:false}")
    private boolean userSeederEnabled;


    @Value("${app.profile-completion.enabled:false}")
    private boolean profileCompletionEnabled;



    @Override
    public void run(String... args) {


        log.info("====================================");
        log.info("Seeder Runner Started");
        log.info("====================================");



        // =====================================
        // USER CREATION SEEDER
        // =====================================

        if (userSeederEnabled) {

            log.info("Starting User Seeder...");

            testUserSeeder.seedUsers();

            log.info("User Seeder Completed");

        } else {

            log.info("User Seeder Disabled");

        }



        // =====================================
        // PROFILE COMPLETION SEEDER
        // =====================================

        if (profileCompletionEnabled) {

            log.info("Starting Profile Completion Seeder...");

            profileCompletionSeeder.completeProfiles();

            log.info("Profile Completion Seeder Completed");

        } else {

            log.info("Profile Completion Seeder Disabled");

        }



        log.info("====================================");
        log.info("Seeder Runner Finished");
        log.info("====================================");

    }

}