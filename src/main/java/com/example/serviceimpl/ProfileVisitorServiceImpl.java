package com.example.serviceimpl;

import com.example.dto.response.ProfileVisitorResponseDTO;
import com.example.model.Profile;
import com.example.model.ProfileVisitor;
import com.example.model.User;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileVisitorRepository;
import com.example.repository.UserRepository;
import com.example.service.ProfileVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileVisitorServiceImpl
        implements ProfileVisitorService {
    private final ProfileRepository profileRepository;
    private final ProfileVisitorRepository repository;

    private final UserRepository userRepository;

    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow();
    }

    @Override
    public void saveVisit(Long visitedUserId) {

        User visitor =
                getCurrentUser();

        if (
                visitor.getId()
                        .equals(visitedUserId)
        ) {
            return;
        }

        Optional<ProfileVisitor> existing =
                repository
                        .findByVisitor_IdAndVisitedUser_Id(
                                visitor.getId(),
                                visitedUserId
                        );

        if (existing.isPresent()) {

            ProfileVisitor pv =
                    existing.get();

            pv.setViewedAt(
                    LocalDateTime.now()
            );

            repository.save(pv);

            return;
        }

        User visitedUser =
                userRepository
                        .findById(visitedUserId)
                        .orElseThrow();

        ProfileVisitor pv =
                new ProfileVisitor();

        pv.setVisitor(visitor);

        pv.setVisitedUser(
                visitedUser
        );

        repository.save(pv);
    }
    @Override
    public List<ProfileVisitorResponseDTO> getMyVisitors() {

        return repository
                .findByVisitedUser_IdOrderByViewedAtDesc(
                        getCurrentUser().getId()
                )
                .stream()
                .map(v -> {

                    ProfileVisitorResponseDTO dto =
                            new ProfileVisitorResponseDTO();

                    dto.setUserId(
                            v.getVisitor().getId()
                    );

                    dto.setFullName(
                            v.getVisitor().getFullName()
                    );

                    dto.setEmail(
                            v.getVisitor().getEmail()
                    );

                    Profile profile =
                            profileRepository
                                    .findByUserId(
                                            v.getVisitor().getId()
                                    )
                                    .orElse(null);

                    if (profile != null) {

                        dto.setProfileId(
                                profile.getId()
                        );

                        dto.setImageUrl(
                                profile.getImageUrl()
                        );

                    }

                    dto.setViewedAt(
                            v.getViewedAt().toString()
                    );

                    return dto;

                })
                .toList();
    }
}