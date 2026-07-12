package com.example.serviceimpl;

import com.example.repository.UserRepository;
import com.example.service.MatchNotificationService;
import com.example.service.MatchService;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchNotificationServiceImpl
        implements MatchNotificationService {

    private final UserRepository userRepository;

    private final MatchService matchService;

    private final NotificationService notificationService;

    @Override
    public void generateForPreferenceUpdate(Long userId) {

        List<User> users =
                userRepository.findAllActiveWithProfileAndPreference();

        for (User candidate : users) {

            if (candidate.getId().equals(userId)) {
                continue;
            }

            if (candidate.getProfile() == null) {
                continue;
            }

            int score =
                    matchService.calculateMatchScore(
                            userId,
                            candidate.getId()
                    );

            if (score >= 75) {

                notificationService.createMatchRecommendation(
                        userId,
                        candidate.getId(),
                        score
                );
            }
        }
    }
    @Override
    public void generateForProfileUpdate(Long userId) {

        List<User> users =
                userRepository.findAllActiveWithProfileAndPreference();

        for (User preferenceOwner : users) {

            if (preferenceOwner.getId().equals(userId)) {
                continue;
            }

            if (preferenceOwner.getPartnerPreference() == null) {
                continue;
            }

            int score =
                    matchService.calculateMatchScore(
                            preferenceOwner.getId(),
                            userId
                    );

            if (score >= 75) {

                notificationService.createMatchRecommendation(
                        preferenceOwner.getId(),
                        userId,
                        score
                );
            }
        }
    }
}

