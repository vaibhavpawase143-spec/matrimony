package com.example.serviceimpl;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.*;
import com.example.repository.MatchRepository;
import com.example.repository.SwipeRepository;
import com.example.repository.UserRepository;
import com.example.service.MatchService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final SwipeRepository swipeRepository;
    private final UserRepository userRepository;

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
        swipe.setType(type);

        swipeRepository.save(swipe);

        if (type == SwipeType.LIKE) {

            Optional<Swipe> reverseSwipe =
                    swipeRepository.findByFromUserAndToUser(toUser, fromUser);

            if (reverseSwipe.isPresent() &&
                    reverseSwipe.get().getType() == SwipeType.LIKE) {

                createMatchIfNotExists(fromUser, toUser);
            }
        }
    }

    // ================= MATCH CREATION =================

    private void createMatchIfNotExists(User u1, User u2) {

        User smaller = u1.getId() < u2.getId() ? u1 : u2;
        User larger = u1.getId() < u2.getId() ? u2 : u1;

        boolean exists = matchRepository.existsByUser1AndUser2(smaller, larger);

        if (!exists) {
            Match match = new Match();
            match.setUsers(smaller, larger); // IMPORTANT
            matchRepository.save(match);
        }
    }

    // ================= GET MATCHES =================

    @Override
    public PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Match> matchPage = matchRepository.findByUser1OrUser2(user, user, pageable);

        List<MatchResponseDTO> content = matchPage.getContent()
                .stream()
                .map(match -> mapToDTO(match, user))
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

        List<User> users = userRepository.findTopMatches(userId, PageRequest.of(0, limit));

        return users.stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }

    // ================= EXPLANATION =================

    @Override
    public MatchExplanationResponseDTO getMatchExplanation(Long userId, Long profileId) {

        return MatchExplanationResponseDTO.builder()
                .reason("You both have high compatibility")
                .totalScore(85)
                .build();
    }

    // ================= MY MATCHES =================

    @Override
    public List<User> getMyMatches(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Match> matches = matchRepository.findByUser1OrUser2(user, user);
        return matches.stream()
                .map(match -> match.getUser1().equals(user)
                        ? match.getUser2()
                        : match.getUser1())
                .collect(Collectors.toList());
    }

    // ================= MAPPER =================

    private MatchResponseDTO mapToDTO(Match match, User currentUser) {

        User other = match.getUser1().equals(currentUser)
                ? match.getUser2()
                : match.getUser1();

        return mapUserToDTO(other);
    }

    private MatchResponseDTO mapUserToDTO(User user) {

        Profile profile = user.getProfile();

        return MatchResponseDTO.builder()
                .userId(user.getId())
                .name(user.getFullName())

                .city(profile != null && profile.getCity() != null
                        ? profile.getCity().getName()
                        : null)

                .religion(profile != null && profile.getReligion() != null
                        ? profile.getReligion().getName()
                        : null)

                .caste(profile != null && profile.getCaste() != null
                        ? profile.getCaste().getName()
                        : null)

                .matchScore(80)
                .matchPercentage("80%")
                .build();
    }
}