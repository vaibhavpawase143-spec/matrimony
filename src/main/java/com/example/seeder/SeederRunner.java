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
    private final PartnerPreferenceSeeder partnerPreferenceSeeder;
    private final com.example.seeder.TestUserSeeder testUserSeeder;
    private final ProfileCompletionSeeder profileCompletionSeeder;
    private final ProfileSeeder profileSeeder;
    private final PhotoSeeder photoSeeder;
    private final InterestSeeder interestSeeder;
    private final ShortlistSeeder shortlistSeeder;
    private final ChatSeeder chatSeeder;
    private final SubscriptionSeeder subscriptionSeeder;
    private final LikeSeeder likeSeeder;

    @Value("${app.seeder.premium-percentage:30}")
    private int premiumPercentage;
    @Value("${app.seeder.total-conversations:500}")
    private int totalConversations;

    @Value("${app.seeder.min-messages:5}")
    private int minMessages;

    @Value("${app.seeder.max-messages:30}")
    private int maxMessages;
    @Value("${app.seeder.total-shortlists:30}")
    private int totalShortlists;
    @Value("${app.seeder.enabled:false}")
    private boolean seederEnabled;
    @Value("${app.seeder.total-likes:100}")
    private int totalLikes;
    @Value("${app.profile-completion.enabled:false}")
    private boolean profileCompletionEnabled;
    @Value("${app.seeder.total-interests:40}")
    private int totalInterests;
    @Value("${app.seeder.total-users:100}")
    private int totalUsers;

    @Override
    public void run(String... args) {

        log.info("====================================");
        log.info("Seeder Runner Started");
        log.info("====================================");

        // =====================================
        // USER SEEDER
        // =====================================

        if (seederEnabled) {
            log.info("====================================");
            log.info("Seeder Configuration");
            log.info("Users                : {}", totalUsers);
            log.info("Interests           : {}", totalInterests);
            log.info("Shortlists          : {}", totalShortlists);
            log.info("Conversations       : {}", totalConversations);
            log.info("Messages/Chat       : {} - {}", minMessages, maxMessages);
            log.info("Premium Percentage  : {}%", premiumPercentage);
            log.info("Likes               : {}", totalLikes);
            log.info("====================================");
            log.info("Starting User Seeder...");
            log.info("Generating {} users...", totalUsers);
        //    testUserSeeder.seedUsers(totalUsers);

            log.info("User Seeder Completed");

            log.info("Starting Profile Seeder...");

       //     profileSeeder.seedProfiles();

            log.info("Profile Seeder Completed");
            log.info("Starting Partner Preference Seeder...");

         //   partnerPreferenceSeeder.seedPartnerPreferences();

            log.info("Partner Preference Seeder Completed");
            log.info("Starting Photo Seeder...");

           // photoSeeder.seedPhotos();

            log.info("Photo Seeder Completed");
            log.info("Starting Interest Seeder...");

          //  interestSeeder.seedInterests(totalInterests);

            log.info("Interest Seeder Completed");
            log.info("Starting Shortlist Seeder...");

       //     shortlistSeeder.seedShortlists(totalShortlists);

            log.info("Shortlist Seeder Completed");

            log.info("Starting Chat Seeder...");

//            chatSeeder.seedChats(
//                    totalConversations,
//                    minMessages,
//                    maxMessages
//            );

            log.info("Chat Seeder Completed");
            log.info("Starting Subscription Seeder...");
//
//            subscriptionSeeder.seedSubscriptions(
//                    premiumPercentage
//            );

            log.info("Subscription Seeder Completed");
            log.info("Starting Like Seeder...");

           // likeSeeder.seedLikes(totalLikes);

            log.info("Like Seeder Completed");
        } else {

            log.info("User Seeder Disabled");

        }

        // =====================================
        // PROFILE COMPLETION SEEDER
        // =====================================

        if (profileCompletionEnabled) {

            log.info("Starting Profile Completion Seeder...");

         //   profileCompletionSeeder.completeProfiles();

            log.info("Profile Completion Seeder Completed");

        } else {

            log.info("Profile Completion Seeder Disabled");

        }

        log.info("====================================");
        log.info("Seeder Runner Finished");
        log.info("====================================");
    }
}