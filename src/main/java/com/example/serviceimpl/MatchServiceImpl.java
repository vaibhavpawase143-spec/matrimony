package com.example.serviceimpl;

import com.example.dto.response.MatchExplanationResponseDTO;
import com.example.dto.response.MatchResponseDTO;
import com.example.dto.response.PageResponse;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.MatchService;
import com.example.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;
import com.example.dto.response.MatchDetailsResponseDTO;
import com.example.dto.response.FieldMatchDTO;
import com.example.exception.PremiumRequiredException;
import com.example.repository.UserSubscriptionRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final SwipeRepository swipeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final UserBlockRepository userBlockRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

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
    public MatchDetailsResponseDTO getMatchDetails(Long userId, Long partnerId) {


        User currentUser = userRepository.findByIdWithProfileAndPreference(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User partner = userRepository.findByIdWithProfile(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        Profile partnerProfile = partner.getProfile();
        PartnerPreference preference = currentUser.getPartnerPreference();

        if (partnerProfile == null) {
            throw new RuntimeException("Partner profile not found");
        }

        if (preference == null) {
            throw new RuntimeException("Partner preference not found");
        }

        List<FieldMatchDTO> fields = new ArrayList<>();

        // ================= AGE =================

        int partnerAge =
                LocalDate.now().getYear() - partnerProfile.getDateOfBirth().getYear();

        fields.add(
                FieldMatchDTO.builder()
                        .fieldName("Age")
                        .myValue(getAgeRange(preference))
                        .partnerValue(String.valueOf(partnerAge))
                        .matched(isAgeMatched(preference, partnerProfile))
                        .build()
        );

        // ================= HEIGHT =================

        fields.add(
                FieldMatchDTO.builder()
                        .fieldName("Height")
                        .myValue(getHeightRange(preference))
                        .partnerValue(
                                partnerProfile.getHeight() != null
                                        ? partnerProfile.getHeight().getHeight()
                                        : "-"
                        )
                        .matched(isHeightMatched(preference, partnerProfile))
                        .build()
        );
        // ================= WEIGHT =================

        fields.add(
                FieldMatchDTO.builder()
                        .fieldName("Weight")
                        .myValue(getWeightRange(preference))
                        .partnerValue(
                                partnerProfile.getWeight() != null
                                        ? partnerProfile.getWeight().getValue()
                                        : "-"
                        )
                        .matched(isWeightMatched(preference, partnerProfile))
                        .build()
        );

        // ================= SIMPLE FIELDS =================

        addField(
                fields,
                "Religion",
                preference.getReligion() != null
                        ? preference.getReligion().getName()
                        : null,
                partnerProfile.getReligion() != null
                        ? partnerProfile.getReligion().getName()
                        : null,
                isReligionMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Caste",
                preference.getCaste() != null
                        ? preference.getCaste().getName()
                        : null,
                partnerProfile.getCaste() != null
                        ? partnerProfile.getCaste().getName()
                        : null,
                isCasteMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "City",
                preference.getCity() != null
                        ? preference.getCity().getName()
                        : null,
                partnerProfile.getCity() != null
                        ? partnerProfile.getCity().getName()
                        : null,
                isCityMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Education",
                preference.getEducationLevel() != null
                        ? preference.getEducationLevel().getName()
                        : null,
                partnerProfile.getEducationLevel() != null
                        ? partnerProfile.getEducationLevel().getName()
                        : null,
                isEducationMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Occupation",
                preference.getOccupation() != null
                        ? preference.getOccupation().getName()
                        : null,
                partnerProfile.getOccupation() != null
                        ? partnerProfile.getOccupation().getName()
                        : null,
                isOccupationMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Marital Status",
                preference.getMaritalStatus() != null
                        ? preference.getMaritalStatus().getName()
                        : null,
                partnerProfile.getMaritalStatus() != null
                        ? partnerProfile.getMaritalStatus().getName()
                        : null,
                isMaritalMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Smoking",
                preference.getSmoking() != null
                        ? preference.getSmoking().getValue()
                        : null,
                partnerProfile.getSmoking() != null
                        ? partnerProfile.getSmoking().getValue()
                        : null,
                isSmokingMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Drinking",
                preference.getDrinking() != null
                        ? preference.getDrinking().getValue()
                        : null,
                partnerProfile.getDrinking() != null
                        ? partnerProfile.getDrinking().getValue()
                        : null,
                isDrinkingMatched(preference, partnerProfile)
        );

        addField(
                fields,
                "Diet",
                preference.getDiet() != null
                        ? preference.getDiet().getName()
                        : null,
                partnerProfile.getDiet() != null
                        ? partnerProfile.getDiet().getName()
                        : null,
                isDietMatched(preference, partnerProfile)
        );

        int totalFields = fields.size();

        int matchedFields = (int) fields.stream()
                .filter(FieldMatchDTO::isMatched)
                .count();

        int percentage = totalFields == 0
                ? 0
                : (matchedFields * 100) / totalFields;



        return MatchDetailsResponseDTO.builder()
                .userId(partner.getId())
                .fullName(partner.getFullName())
                .profilePhoto(partnerProfile.getImageUrl())
                .matchPercentage(percentage)
                .totalFields(totalFields)
                .matchedFields(matchedFields)
                .fieldMatches(fields)
                .build();
    }

    @Override
    public PageResponse<MatchResponseDTO> getMatches(Long userId, int page, int size) {

        validatePremium(userId);

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


        User currentUser = userRepository.findByIdWithProfileAndPreference(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> blockedIds =
                userBlockRepository.findBlockedUserIds(userId);

        List<User> users =
                userRepository.findTopMatches(
                        userId,
                        PageRequest.of(0, limit + blockedIds.size() + 20)
                );

        return users.stream()

                .filter(user -> !blockedIds.contains(user.getId()))

                .filter(user -> !user.getId().equals(userId))

                .map(u ->
                        userRepository
                                .findByIdWithProfileAndPreference(u.getId())
                                .orElse(u)
                )

                .sorted((u1, u2) ->
                        calculateMatchScore(currentUser, u2)
                                - calculateMatchScore(currentUser, u1)
                )

                .map(user -> mapUserToDTO(user, currentUser))

                .limit(limit)

                .collect(Collectors.toList());
    }
    // ================= EXPLANATION =================

    @Override
    public MatchExplanationResponseDTO getMatchExplanation(Long userId, Long profileId) {


        User u1 = userRepository.findByIdWithProfileAndPreference(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User u2 = userRepository.findByIdWithProfileAndPreference(profileId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PartnerPreference pref = u1.getPartnerPreference();
        Profile profile = u2.getProfile();

        return MatchExplanationResponseDTO.builder()
                .profileId(profileId)
                .totalScore(calculateMatchScore(u1, u2))
                .matchPercentage(calculateMatchScore(u1, u2) + "%")

                .ageMatch(isAgeMatched(pref, profile))
                .heightMatch(isHeightMatched(pref, profile))
                .weightMatch(isWeightMatched(pref, profile))
                .religionMatch(isReligionMatched(pref, profile))
                .casteMatch(isCasteMatched(pref, profile))
                .cityMatch(isCityMatched(pref, profile))
                .educationMatch(isEducationMatched(pref, profile))
                .occupationMatch(isOccupationMatched(pref, profile))
                .maritalMatch(isMaritalMatched(pref, profile))
                .smokingMatch(isSmokingMatched(pref, profile))
                .drinkingMatch(isDrinkingMatched(pref, profile))
                .dietMatch(isDietMatched(pref, profile))

                .reason("Compatibility based on profile & partner preferences")
                .build();
    }

    // ================= DYNAMIC SCORE =================

    private int calculateMatchScore(User currentUser,
                                    User candidateUser) {

        PartnerPreference pref = currentUser.getPartnerPreference();
        Profile profile = candidateUser.getProfile();

        if (pref == null || profile == null) {
            return 0;
        }

        int matched = 0;
        int total = 0;

        // Age
        if (pref.getMinAge() != null && pref.getMaxAge() != null) {
            total++;
            if (isAgeMatched(pref, profile)) {
                matched++;
            }
        }

        // Height
        if (pref.getMinHeight() != null && pref.getMaxHeight() != null) {
            total++;
            if (isHeightMatched(pref, profile)) {
                matched++;
            }
        }

        // Weight
        if (pref.getMinWeight() != null && pref.getMaxWeight() != null) {
            total++;
            if (isWeightMatched(pref, profile)) {
                matched++;
            }
        }

        // Religion
        if (pref.getReligion() != null) {
            total++;
            if (isReligionMatched(pref, profile)) {
                matched++;
            }
        }

        // Caste
        if (pref.getCaste() != null) {
            total++;
            if (isCasteMatched(pref, profile)) {
                matched++;
            }
        }

        // City
        if (pref.getCity() != null) {
            total++;
            if (isCityMatched(pref, profile)) {
                matched++;
            }
        }

        // Education
        if (pref.getEducationLevel() != null) {
            total++;
            if (isEducationMatched(pref, profile)) {
                matched++;
            }
        }

        // Occupation
        if (pref.getOccupation() != null) {
            total++;
            if (isOccupationMatched(pref, profile)) {
                matched++;
            }
        }

        // Marital Status
        if (pref.getMaritalStatus() != null) {
            total++;
            if (isMaritalMatched(pref, profile)) {
                matched++;
            }
        }

        // Smoking
        if (pref.getSmoking() != null) {
            total++;
            if (isSmokingMatched(pref, profile)) {
                matched++;
            }
        }

        // Drinking
        if (pref.getDrinking() != null) {
            total++;
            if (isDrinkingMatched(pref, profile)) {
                matched++;
            }
        }

        // Diet
        if (pref.getDiet() != null) {
            total++;
            if (isDietMatched(pref, profile)) {
                matched++;
            }
        }

        if (total == 0) {
            return 0;
        }

        return (matched * 100) / total;
    }
    @Override
    public int calculateMatchScore(
            Long currentUserId,
            Long candidateUserId
    ) {

        User currentUser =
                userRepository.findByIdWithProfileAndPreference(currentUserId)
                        .orElseThrow(() ->
                                new RuntimeException("Current user not found"));

        User candidateUser =
                userRepository.findByIdWithProfileAndPreference(candidateUserId)
                        .orElseThrow(() ->
                                new RuntimeException("Candidate user not found"));

        return calculateMatchScore(currentUser, candidateUser);
    }
    private int getMatchedFieldCount(User currentUser, User partner) {

        PartnerPreference pref = currentUser.getPartnerPreference();
        Profile profile = partner.getProfile();

        if (pref == null || profile == null) {
            return 0;
        }

        int matched = 0;

        if (pref.getMinAge() != null && pref.getMaxAge() != null && isAgeMatched(pref, profile))
            matched++;

        if (pref.getMinHeight() != null && pref.getMaxHeight() != null && isHeightMatched(pref, profile))
            matched++;

        if (pref.getMinWeight() != null && pref.getMaxWeight() != null && isWeightMatched(pref, profile))
            matched++;

        if (pref.getReligion() != null && isReligionMatched(pref, profile))
            matched++;

        if (pref.getCaste() != null && isCasteMatched(pref, profile))
            matched++;

        if (pref.getCity() != null && isCityMatched(pref, profile))
            matched++;

        if (pref.getEducationLevel() != null && isEducationMatched(pref, profile))
            matched++;

        if (pref.getOccupation() != null && isOccupationMatched(pref, profile))
            matched++;

        if (pref.getMaritalStatus() != null && isMaritalMatched(pref, profile))
            matched++;

        if (pref.getSmoking() != null && isSmokingMatched(pref, profile))
            matched++;

        if (pref.getDrinking() != null && isDrinkingMatched(pref, profile))
            matched++;

        if (pref.getDiet() != null && isDietMatched(pref, profile))
            matched++;

        return matched;
    }
    private int getTotalPreferenceFields(PartnerPreference pref) {

        int total = 0;

        if (pref.getMinAge() != null && pref.getMaxAge() != null) total++;

        if (pref.getMinHeight() != null && pref.getMaxHeight() != null) total++;

        if (pref.getMinWeight() != null && pref.getMaxWeight() != null) total++;

        if (pref.getReligion() != null) total++;

        if (pref.getCaste() != null) total++;

        if (pref.getCity() != null) total++;

        if (pref.getEducationLevel() != null) total++;

        if (pref.getOccupation() != null) total++;

        if (pref.getMaritalStatus() != null) total++;

        if (pref.getSmoking() != null) total++;

        if (pref.getDrinking() != null) total++;

        if (pref.getDiet() != null) total++;

        return total;
    }
    private boolean isAgeMatched(PartnerPreference pref, Profile profile) {

        if (pref.getMinAge() == null ||
                pref.getMaxAge() == null ||
                profile.getDateOfBirth() == null) {
            return false;
        }

        int age = LocalDate.now().getYear() - profile.getDateOfBirth().getYear();

        return age >= pref.getMinAge() &&
                age <= pref.getMaxAge();
    }
    private boolean isReligionMatched(PartnerPreference pref, Profile profile) {

        return pref.getReligion() != null &&
                profile.getReligion() != null &&
                pref.getReligion().getName().trim()
                        .equalsIgnoreCase(
                                profile.getReligion().getName().trim()
                        );
    }
    private boolean isCasteMatched(PartnerPreference pref, Profile profile) {

        return pref.getCaste() != null &&
                profile.getCaste() != null &&
                pref.getCaste().getName().trim()
                        .equalsIgnoreCase(
                                profile.getCaste().getName().trim()
                        );
    }
    private boolean isCityMatched(PartnerPreference pref, Profile profile) {

        return pref.getCity() != null &&
                profile.getCity() != null &&
                pref.getCity().getName().trim()
                        .equalsIgnoreCase(
                                profile.getCity().getName().trim()
                        );
    }
    private boolean isEducationMatched(PartnerPreference pref, Profile profile) {

        return pref.getEducationLevel() != null &&
                profile.getEducationLevel() != null &&
                pref.getEducationLevel().getName().trim()
                        .equalsIgnoreCase(
                                profile.getEducationLevel().getName().trim()
                        );
    }
    private boolean isOccupationMatched(PartnerPreference pref, Profile profile) {

        return pref.getOccupation() != null &&
                profile.getOccupation() != null &&
                pref.getOccupation().getName().trim()
                        .equalsIgnoreCase(
                                profile.getOccupation().getName().trim()
                        );
    }
    private boolean isMaritalMatched(PartnerPreference pref, Profile profile) {

        return pref.getMaritalStatus() != null &&
                profile.getMaritalStatus() != null &&
                pref.getMaritalStatus().getName().trim()
                        .equalsIgnoreCase(
                                profile.getMaritalStatus().getName().trim()
                        );
    }
    private boolean isSmokingMatched(PartnerPreference pref, Profile profile) {

        return pref.getSmoking() != null &&
                profile.getSmoking() != null &&
                pref.getSmoking().getValue().trim()
                        .equalsIgnoreCase(
                                profile.getSmoking().getValue().trim()
                        );
    }
    private boolean isDrinkingMatched(PartnerPreference pref, Profile profile) {

        return pref.getDrinking() != null
                && profile.getDrinking() != null
                && Objects.equals(
                pref.getDrinking().getId(),
                profile.getDrinking().getId()
        );
    }
    private boolean isDietMatched(PartnerPreference pref, Profile profile) {

        return pref.getDiet() != null &&
                profile.getDiet() != null &&
                pref.getDiet().getName().trim()
                        .equalsIgnoreCase(
                                profile.getDiet().getName().trim()
                        );
    }
//    private Integer extractHeight(String height) {
//
//        if (height == null || height.isBlank()) {
//            return null;
//        }
//
//        try {
//            return Integer.parseInt(height.replaceAll("[^0-9]", ""));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    private Integer extractWeight(String weight) {
//
//        if (weight == null || weight.isBlank()) {
//            return null;
//        }
//
//        try {
//            return Integer.parseInt(weight.replaceAll("[^0-9]", ""));
//        } catch (Exception e) {
//            return null;
//        }
//    }
    private boolean isHeightMatched(PartnerPreference pref, Profile profile) {

        if (pref.getMinHeight() == null ||
                pref.getMaxHeight() == null ||
                profile.getHeight() == null) {

            return false;
        }

        Long candidateHeightId = profile.getHeight().getId();

        return candidateHeightId >= pref.getMinHeight()
                && candidateHeightId <= pref.getMaxHeight();
    }
    private boolean isWeightMatched(PartnerPreference pref, Profile profile) {

        if (pref.getMinWeight() == null ||
                pref.getMaxWeight() == null ||
                profile.getWeight() == null) {

            return false;
        }

        Long candidateWeightId = profile.getWeight().getId();

        return candidateWeightId >= pref.getMinWeight()
                && candidateWeightId <= pref.getMaxWeight();
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
    private void addField(List<FieldMatchDTO> fields,
                          String fieldName,
                          String myValue,
                          String partnerValue,
                          boolean matched) {

        fields.add(
                FieldMatchDTO.builder()
                        .fieldName(fieldName)
                        .myValue(myValue)
                        .partnerValue(partnerValue)
                        .matched(matched)
                        .build()
        );
    }
    private String getAgeRange(PartnerPreference preference) {

        if (preference.getMinAge() == null ||
                preference.getMaxAge() == null) {

            return "-";
        }

        return preference.getMinAge() + " - " + preference.getMaxAge();
    }
    private String getHeightRange(PartnerPreference preference) {

        if (preference.getMinHeight() == null ||
                preference.getMaxHeight() == null) {

            return "-";
        }

        String minHeight = heightRepository
                .findById(preference.getMinHeight())
                .map(Height::getHeight)
                .orElse("-");

        String maxHeight = heightRepository
                .findById(preference.getMaxHeight())
                .map(Height::getHeight)
                .orElse("-");

        return minHeight + " - " + maxHeight;
    }
    private String getWeightRange(PartnerPreference preference) {

        if (preference.getMinWeight() == null ||
                preference.getMaxWeight() == null) {

            return "-";
        }

        String minWeight = weightRepository
                .findById(preference.getMinWeight())
                .map(Weight::getValue)
                .orElse("-");

        String maxWeight = weightRepository
                .findById(preference.getMaxWeight())
                .map(Weight::getValue)
                .orElse("-");

        return minWeight + " - " + maxWeight;
    }
    private void validatePremium(Long userId) {

        boolean premium = userSubscriptionRepository
                .findFirstByUser_IdAndIsActiveTrueAndStatusAndEndDateAfter(
                        userId,
                        "ACTIVE",
                        LocalDateTime.now()
                )
                .isPresent();

        if (!premium) {
            throw new PremiumRequiredException(
                    "Premium membership required to access matches."
            );
        }
    }


    private MatchResponseDTO mapUserToDTO(User user, User currentUser) {

        Profile profile = user.getProfile();

        String city = null;
        String religion = null;
        String caste = null;
        String occupation = null;
        String imageUrl = null;

        Integer age = null;

        Long profileId = null;

        Boolean isPremium = false;

        if (profile != null) {

            profileId = profile.getId();

            imageUrl = profile.getImageUrl();

            isPremium = profile.getIsPremium();

            if (profile.getCity() != null)
                city = profile.getCity().getName();

            if (profile.getReligion() != null)
                religion = profile.getReligion().getName();

            if (profile.getCaste() != null)
                caste = profile.getCaste().getName();

            if (profile.getOccupation() != null)
                occupation = profile.getOccupation().getName();

            if (profile.getDateOfBirth() != null) {

                age = LocalDate.now().getYear()
                        - profile.getDateOfBirth().getYear();
            }
        }

        int score = calculateMatchScore(currentUser, user);

        return MatchResponseDTO.builder()
                .userId(user.getId())
                .profileId(profileId)
                .name(user.getFullName())
                .imageUrl(imageUrl)
                .age(age)
                .city(city)
                .religion(religion)
                .caste(caste)
                .occupation(occupation)
                .isPremium(isPremium)
                .matchScore(score)
                .matchPercentage(score + "%")
                .build();
    }
}