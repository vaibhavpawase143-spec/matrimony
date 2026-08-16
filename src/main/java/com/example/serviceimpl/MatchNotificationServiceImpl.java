package com.example.serviceimpl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.MatchNotificationService;
import com.example.service.MatchService;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchNotificationServiceImpl implements MatchNotificationService {

    private final UserRepository userRepository;
    private final MatchService matchService;
    private final NotificationService notificationService;

    private static final int BATCH_SIZE = 200;

    @Async("applicationTaskExecutor")
    @Override
    public void generateForPreferenceUpdate(Long userId) {
        log.info("Starting async match notification generation for preference update of user ID: {}", userId);
        try {
            User currentUser = userRepository.findByIdWithProfileAndPreference(userId).orElse(null);
            if (currentUser == null || currentUser.getPartnerPreference() == null) {
                log.warn("User or partner preference not found for user ID: {}", userId);
                return;
            }

            int pageNumber = 0;
            Page<User> candidatePage;

            do {
                Pageable pageable = PageRequest.of(pageNumber, BATCH_SIZE);
                candidatePage = userRepository.findAllActiveWithProfileAndPreference(pageable);

                for (User candidate : candidatePage.getContent()) {
                    if (candidate.getId().equals(userId) || candidate.getProfile() == null) {
                        continue;
                    }

                    // Gender optimization: skip candidates of the same gender if gender is specified
                    if (currentUser.getProfile() != null && currentUser.getProfile().getGender() != null
                            && candidate.getProfile().getGender() != null
                            && currentUser.getProfile().getGender().getId().equals(candidate.getProfile().getGender().getId())) {
                        continue;
                    }

                    int score = matchService.calculateMatchScore(currentUser, candidate);

                    if (score >= 75) {
                        notificationService.createMatchRecommendation(
                                userId,
                                candidate.getId(),
                                score
                        );
                    }
                }
                pageNumber++;
            } while (candidatePage.hasNext());

            log.info("Completed async match notification generation for preference update of user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error generating match notifications for preference update of user ID: {}", userId, e);
        }
    }

    @Async("applicationTaskExecutor")
    @Override
    public void generateForProfileUpdate(Long userId) {
        log.info("Starting async match notification generation for profile update of user ID: {}", userId);
        try {
            User updatedUser = userRepository.findByIdWithProfileAndPreference(userId).orElse(null);
            if (updatedUser == null || updatedUser.getProfile() == null) {
                log.warn("User or profile not found for user ID: {}", userId);
                return;
            }

            int pageNumber = 0;
            Page<User> ownerPage;

            do {
                Pageable pageable = PageRequest.of(pageNumber, BATCH_SIZE);
                ownerPage = userRepository.findAllActiveWithProfileAndPreference(pageable);

                for (User preferenceOwner : ownerPage.getContent()) {
                    if (preferenceOwner.getId().equals(userId) || preferenceOwner.getPartnerPreference() == null) {
                        continue;
                    }

                    // Gender optimization: skip owners of the same gender if gender is specified
                    if (preferenceOwner.getProfile() != null && preferenceOwner.getProfile().getGender() != null
                            && updatedUser.getProfile().getGender() != null
                            && preferenceOwner.getProfile().getGender().getId().equals(updatedUser.getProfile().getGender().getId())) {
                        continue;
                    }

                    int score = matchService.calculateMatchScore(preferenceOwner, updatedUser);

                    if (score >= 75) {
                        notificationService.createMatchRecommendation(
                                preferenceOwner.getId(),
                                userId,
                                score
                        );
                    }
                }
                pageNumber++;
            } while (ownerPage.hasNext());

            log.info("Completed async match notification generation for profile update of user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error generating match notifications for profile update of user ID: {}", userId, e);
        }
    }
}

