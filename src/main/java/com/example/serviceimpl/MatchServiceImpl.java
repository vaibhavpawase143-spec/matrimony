package com.example.serviceimpl;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.*;
import com.example.repository.MatchRepository;
import com.example.repository.SwipeRepository;
import com.example.repository.UserRepository;
import com.example.service.MatchService;
import com.example.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final SwipeRepository swipeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ================= SWIPE =================

    @Override
    public void swipe(Long fromUserId, Long toUserId, SwipeType type) {

        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("You cannot swipe yourself");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("From user not found"));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("To user not found"));

        Swipe swipe = swipeRepository
                .findByFromUserAndToUser(fromUser, toUser)
                .orElse(new Swipe());

        swipe.setFromUser(fromUser);
        swipe.setToUser(toUser);

        if (type == SwipeType.PASS) {
            swipe.setType(SwipeType.PASS);
            swipeRepository.save(swipe);
            return;
        }

        swipe.setType(SwipeType.LIKE);
        swipeRepository.save(swipe);

        notificationService.create(fromUserId, toUserId, NotificationType.REQUEST);

        Optional<Swipe> reverseSwipe =
                swipeRepository.findByFromUserAndToUser(toUser, fromUser);

        if (reverseSwipe.isPresent() &&
                reverseSwipe.get().getType() == SwipeType.LIKE) {

            createMatchIfNotExists(fromUser, toUser);

            notificationService.create(fromUserId, toUserId, NotificationType.MATCH);
            notificationService.create(toUserId, fromUserId, NotificationType.MATCH);
        }
    }

    @Override
    public List<User> getMyMatches(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Match> matches = matchRepository.findByUser1OrUser2(user, user);

        return matches.stream()
                .map(match -> {
                    if (match.getUser1().getId().equals(userId)) {
                        return match.getUser2();
                    } else {
                        return match.getUser1();
                    }
                })
                .collect(Collectors.toList());
    }

    // ================= MATCH CREATION =================

    private void createMatchIfNotExists(User u1, User u2) {

        Long smaller = Math.min(u1.getId(), u2.getId());
        Long larger = Math.max(u1.getId(), u2.getId());

        boolean exists = matchRepository.existsByUser1_IdAndUser2_Id(smaller, larger);

        if (!exists) {
            Match match = new Match();
            match.setUsers(u1, u2);

            try {
                matchRepository.save(match);
            } catch (Exception e) {
                System.out.println("Duplicate match avoided");
            }
        }
    }

    // ================= GET MATCHES =================

    @Override
    public PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size) {

        User currentUser = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Match> matchPage = matchRepository.findByUser1OrUser2(currentUser, currentUser, pageable);

        List<MatchResponseDTO> content = matchPage.getContent()
                .stream()
                .map(match -> mapToDTO(match, currentUser))
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                matchPage.getNumber(),
                matchPage.getSize(),
                matchPage.getTotalElements(),
                matchPage.getTotalPages(),
                matchPage.isLast()
        );
    }

    // ================= TOP MATCHES =================

    @Override
    public List<MatchResponseDTO> getTopMatches(Long userId, int limit) {

        User currentUser = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> users = userRepository.findTopMatches(userId, PageRequest.of(0, limit));

        return users.stream()
                .map(u -> userRepository.findByIdWithProfile(u.getId()).orElse(u)) // 🔥 FIX
                .sorted((u1, u2) ->
                        calculateMatchScore(currentUser, u2) -
                                calculateMatchScore(currentUser, u1)
                )
                .map(user -> mapUserToDTO(user, currentUser))
                .collect(Collectors.toList());
    }

    // ================= EXPLANATION =================

    @Override
    public MatchExplanationResponseDTO getMatchExplanation(Long userId, Long profileId) {

        User u1 = userRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User u2 = userRepository.findByIdWithProfile(profileId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int score = calculateMatchScore(u1, u2);

        return MatchExplanationResponseDTO.builder()
                .profileId(profileId)
                .totalScore(score)
                .matchPercentage(score + "%")
                .reason("Compatibility based on profile & partner preferences")
                .cityMatch(false)
                .religionMatch(false)
                .casteMatch(false)
                .ageMatch(false)
                .build();
    }

    // ================= DYNAMIC SCORE =================

    private int calculateMatchScore(User u1, User u2) {

        int score = 0;

        Profile p1 = u1.getProfile();
        Profile p2 = u2.getProfile();

        if (p1 == null || p2 == null) return score;

        // BASIC MATCH
        if (p1.getCity() != null && p2.getCity() != null &&
                p1.getCity().getId().equals(p2.getCity().getId())) score += 20;

        if (p1.getReligion() != null && p2.getReligion() != null &&
                p1.getReligion().getId().equals(p2.getReligion().getId())) score += 20;

        if (p1.getCaste() != null && p2.getCaste() != null &&
                p1.getCaste().getId().equals(p2.getCaste().getId())) score += 20;

        // AGE MATCH
        if (p1.getDateOfBirth() != null && p2.getDateOfBirth() != null) {
            int age1 = LocalDate.now().getYear() - p1.getDateOfBirth().getYear();
            int age2 = LocalDate.now().getYear() - p2.getDateOfBirth().getYear();
            if (Math.abs(age1 - age2) <= 3) score += 20;
        }

        // PARTNER PREFERENCE
        PartnerPreference pref = u1.getPartnerPreference();

        if (pref != null) {

            if (pref.getCity() != null && p2.getCity() != null &&
                    pref.getCity().getId().equals(p2.getCity().getId())) score += 10;

            if (pref.getReligion() != null && p2.getReligion() != null &&
                    pref.getReligion().getId().equals(p2.getReligion().getId())) score += 10;

            if (pref.getCaste() != null && p2.getCaste() != null &&
                    pref.getCaste().getId().equals(p2.getCaste().getId())) score += 10;

            if (pref.getMinAge() != null && pref.getMaxAge() != null &&
                    p2.getDateOfBirth() != null) {

                int age2 = LocalDate.now().getYear() - p2.getDateOfBirth().getYear();

                if (age2 >= pref.getMinAge() && age2 <= pref.getMaxAge()) score += 10;
            }
        }

        return Math.min(score, 100);
    }

    // ================= MAPPER =================

    private MatchResponseDTO mapToDTO(Match match, User currentUser) {

        User other = match.getUser1().equals(currentUser)
                ? match.getUser2()
                : match.getUser1();

        User fullUser = userRepository.findByIdWithProfile(other.getId())
                .orElse(other);

        return mapUserToDTO(fullUser, currentUser);
    }

    private MatchResponseDTO mapUserToDTO(User user, User currentUser) {

        Profile profile = user.getProfile();

        String city = null;
        String religion = null;
        String caste = null;

        if (profile != null) {
            if (profile.getCity() != null) city = profile.getCity().getName();
            if (profile.getReligion() != null) religion = profile.getReligion().getName();
            if (profile.getCaste() != null) caste = profile.getCaste().getName();
        }

        int score = calculateMatchScore(currentUser, user);

        return MatchResponseDTO.builder()
                .userId(user.getId())
                .name(user.getFullName())
                .city(city)
                .religion(religion)
                .caste(caste)
                .matchScore(score)
                .matchPercentage(score + "%")
                .build();
    }
}