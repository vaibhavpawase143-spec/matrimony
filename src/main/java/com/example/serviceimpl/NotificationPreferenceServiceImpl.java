package com.example.serviceimpl;

import com.example.dto.request.NotificationPreferenceRequestDTO;
import com.example.dto.response.NotificationPreferenceResponseDTO;
import com.example.model.NotificationPreference;
import com.example.model.User;
import com.example.repository.NotificationPreferenceRepository;
import com.example.repository.UserRepository;
import com.example.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceServiceImpl
        implements NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    private NotificationPreferenceResponseDTO mapToResponse(
            NotificationPreference preference) {

        NotificationPreferenceResponseDTO dto =
                new NotificationPreferenceResponseDTO();

        dto.setUserId(preference.getUser().getId());

        dto.setMatchNotifications(
                preference.getMatchNotifications());

        dto.setInterestNotifications(
                preference.getInterestNotifications());

        dto.setMessageNotifications(
                preference.getMessageNotifications());

        dto.setProfileViewNotifications(
                preference.getProfileViewNotifications());

        dto.setPromotionalEmails(
                preference.getPromotionalEmails());

        return dto;
    }



    @Override
    public NotificationPreferenceResponseDTO
    getMyPreferences() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        NotificationPreference preference =
                preferenceRepository
                        .findByUser(user)
                        .orElseGet(() -> {

                            NotificationPreference p =
                                    new NotificationPreference();

                            p.setUser(user);

                            // Default values
                            p.setMatchNotifications(true);
                            p.setInterestNotifications(true);
                            p.setMessageNotifications(true);
                            p.setProfileViewNotifications(false);
                            p.setPromotionalEmails(false);

                            return preferenceRepository.save(p);
                        });

        return mapToResponse(preference);
    }

    @Override
    public NotificationPreferenceResponseDTO updatePreferences(
            NotificationPreferenceRequestDTO request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        NotificationPreference preference =
                preferenceRepository
                        .findByUser(user)
                        .orElseGet(() -> {

                            NotificationPreference p =
                                    new NotificationPreference();

                            p.setUser(user);

                            p.setMatchNotifications(true);
                            p.setInterestNotifications(true);
                            p.setMessageNotifications(true);
                            p.setProfileViewNotifications(false);
                            p.setPromotionalEmails(false);

                            return preferenceRepository.save(p);
                        });

        preference.setMatchNotifications(
                request.getMatchNotifications());

        preference.setInterestNotifications(
                request.getInterestNotifications());

        preference.setMessageNotifications(
                request.getMessageNotifications());

        preference.setProfileViewNotifications(
                request.getProfileViewNotifications());

        preference.setPromotionalEmails(
                request.getPromotionalEmails());

        NotificationPreference updated =
                preferenceRepository.save(preference);

        return mapToResponse(updated);
    }
}