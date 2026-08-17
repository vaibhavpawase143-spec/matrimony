package com.example.serviceimpl;

import com.example.dto.request.ProfileSearchRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.ProfileSearchResultDTO;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.UserPhoto;
import com.example.repository.ProfileRepository;
import com.example.repository.UserPhotoRepository;
import com.example.repository.UserRepository;
import com.example.service.ProfileSearchService;
import com.example.specification.ProfileSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileSearchServiceImpl implements ProfileSearchService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfileSearchResultDTO> searchProfiles(ProfileSearchRequestDTO request) {
        if (request != null) {
            if (request.getAgeFrom() != null && (request.getAgeFrom() < 18 || request.getAgeFrom() > 100)) {
                throw new com.example.exception.BadRequestException("Minimum age must be between 18 and 100.");
            }
            if (request.getAgeTo() != null && (request.getAgeTo() < 18 || request.getAgeTo() > 100)) {
                throw new com.example.exception.BadRequestException("Maximum age must be between 18 and 100.");
            }
            if (request.getAgeFrom() != null && request.getAgeTo() != null
                    && request.getAgeFrom() > request.getAgeTo()) {
                throw new com.example.exception.BadRequestException("Minimum age cannot be greater than maximum age.");
            }
        }

        Long currentUserId = getCurrentUserId();

        int page = request != null && request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request != null && request.getSize() != null && request.getSize() > 0 ? request.getSize() : 20;

        Sort sort = buildSort(request.getSortBy(), request.getSortOrder());
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Profile> profilePage = profileRepository.findAll(
                ProfileSearchSpecification.buildSearchSpecification(request, currentUserId),
                pageable);

        List<Profile> profiles = profilePage.getContent();

        List<Long> userIdsNeedingPhotos = profiles.stream()
                .filter(p -> (p.getImageUrl() == null || p.getImageUrl().isBlank()) && p.getUser() != null)
                .map(p -> p.getUser().getId())
                .distinct()
                .toList();

        java.util.Map<Long, String> userPhotoMap = new java.util.HashMap<>();
        if (!userIdsNeedingPhotos.isEmpty()) {
            List<UserPhoto> primaryPhotos = userPhotoRepository.findByUserIdInAndPrimaryPhotoTrue(userIdsNeedingPhotos);
            for (UserPhoto up : primaryPhotos) {
                if (up.getUser() != null && up.getPhotoUrl() != null) {
                    userPhotoMap.putIfAbsent(up.getUser().getId(), up.getPhotoUrl());
                }
            }
        }

        List<ProfileSearchResultDTO> content = profiles
                .stream()
                .map(p -> mapToSearchResultDTO(p, userPhotoMap))
                .toList();

        return new PageResponse<>(
                content,
                profilePage.getNumber(),
                profilePage.getSize(),
                profilePage.getTotalElements(),
                profilePage.getTotalPages(),
                profilePage.isLast());
    }

    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                String username = authentication.getName();
                return userRepository.findByEmailIgnoreCase(username)
                        .map(User::getId)
                        .orElse(null);
            }
        } catch (Exception e) {
            // Ignored if unauthenticated public search
        }
        return null;
    }

    private Sort buildSort(String sortBy, String sortOrder) {
        boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
        if (sortBy == null)
            sortBy = "relevance";

        return switch (sortBy.toLowerCase()) {
            case "newest", "createdat" -> isAsc ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
            case "age_low_high", "age_asc" -> Sort.by("dateOfBirth").descending(); // Younger first
            case "age_high_low", "age_desc" -> Sort.by("dateOfBirth").ascending(); // Older first
            case "relevance", "boost" -> Sort.by(Sort.Direction.DESC, "isPremium", "boostScore", "createdAt");
            default -> isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        };
    }

    private ProfileSearchResultDTO mapToSearchResultDTO(Profile p, java.util.Map<Long, String> userPhotoMap) {
        Integer age = p.getDateOfBirth() != null ? Period.between(p.getDateOfBirth(), LocalDate.now()).getYears()
                : null;

        User user = p.getUser();
        boolean isVerified = user != null && Boolean.TRUE.equals(user.getEmailVerified())
                && Boolean.TRUE.equals(user.getPhoneVerified());

        String photoUrl = p.getImageUrl();
        if ((photoUrl == null || photoUrl.isBlank()) && user != null) {
            photoUrl = userPhotoMap.get(user.getId());
        }

        return ProfileSearchResultDTO.builder()
                .id(p.getId())
                .userId(user != null ? user.getId() : null)
                .fullName(user != null ? user.getFullName() : null)
                .firstName(user != null ? user.getFirstName() : null)
                .lastName(user != null ? user.getLastName() : null)
                .imageUrl(photoUrl)
                .age(age)
                .genderId(p.getGender() != null ? p.getGender().getId() : null)
                .genderName(p.getGender() != null ? p.getGender().getName() : null)
                .religionId(p.getReligion() != null ? p.getReligion().getId() : null)
                .religionName(p.getReligion() != null ? p.getReligion().getName() : null)
                .casteId(p.getCaste() != null ? p.getCaste().getId() : null)
                .casteName(p.getCaste() != null ? p.getCaste().getName() : null)
                .subCasteId(p.getSubCaste() != null ? p.getSubCaste().getId() : null)
                .subCasteName(p.getSubCaste() != null ? p.getSubCaste().getName() : null)
                .motherTongueId(p.getMotherTongue() != null ? p.getMotherTongue().getId() : null)
                .motherTongueName(p.getMotherTongue() != null ? p.getMotherTongue().getName() : null)
                .maritalStatusId(p.getMaritalStatus() != null ? p.getMaritalStatus().getId() : null)
                .maritalStatusName(p.getMaritalStatus() != null ? p.getMaritalStatus().getName() : null)
                .cityId(p.getCity() != null ? p.getCity().getId() : null)
                .cityName(p.getCity() != null ? p.getCity().getName() : null)
                .stateId(p.getState() != null ? p.getState().getId() : null)
                .stateName(p.getState() != null ? p.getState().getName() : null)
                .countryId(p.getCountry() != null ? p.getCountry().getId() : null)
                .countryName(p.getCountry() != null ? p.getCountry().getName() : null)
                .educationLevelId(p.getEducationLevel() != null ? p.getEducationLevel().getId() : null)
                .educationLevelName(p.getEducationLevel() != null ? p.getEducationLevel().getName() : null)
                .occupationId(p.getOccupation() != null ? p.getOccupation().getId() : null)
                .occupationName(p.getOccupation() != null ? p.getOccupation().getName() : null)
                .incomeId(p.getIncome() != null ? p.getIncome().getId() : null)
                .incomeName(p.getIncome() != null ? p.getIncome().getRange() : null)
                .heightId(p.getHeight() != null ? p.getHeight().getId() : null)
                .heightValue(p.getHeight() != null ? p.getHeight().getHeight() : null)
                .weightId(p.getWeight() != null ? p.getWeight().getId() : null)
                .weightValue(p.getWeight() != null ? p.getWeight().getValue() : null)
                .dietId(p.getDiet() != null ? p.getDiet().getId() : null)
                .dietName(p.getDiet() != null ? p.getDiet().getName() : null)
                .smokingId(p.getSmoking() != null ? p.getSmoking().getId() : null)
                .smokingName(p.getSmoking() != null ? p.getSmoking().getValue() : null)
                .drinkingId(p.getDrinking() != null ? p.getDrinking().getId() : null)
                .drinkingName(p.getDrinking() != null ? p.getDrinking().getName() : null)
                .manglikStatusId(p.getManglikStatus() != null ? p.getManglikStatus().getId() : null)
                .manglikStatusName(p.getManglikStatus() != null ? p.getManglikStatus().getName() : null)
                .profileTypeId(p.getProfileType() != null ? p.getProfileType().getId() : null)
                .profileTypeName(p.getProfileType() != null ? p.getProfileType().getName() : null)
                .profileCompleted(p.getProfileCompleted())
                .isPremium(p.getIsPremium())
                .verified(isVerified)
                .createdAt(p.getCreatedAt())
                .build();
    }
}
