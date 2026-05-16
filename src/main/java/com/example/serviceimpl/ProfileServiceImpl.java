package com.example.serviceimpl;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.CacheService;
import com.example.service.MatchAsyncService;
import com.example.service.ProfileService;
import com.example.specification.ProfileSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final UserRepository userRepository;
    private final MatchAsyncService asyncService;
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final SubCasteRepository subCasteRepository;
    private final EducationLevelRepository educationRepository;
    private final OccupationRepository occupationRepository;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final CityRepository cityRepository;
    private final MotherTongueRepository motherTongueRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final CacheService cacheService;

    // ================= CURRENT USER =================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= CREATE =================
    public ProfileResponseDTO createProfile(ProfileRequestDTO dto) {

        User user = getCurrentUser(); // ✅ FIXED

        if (repository.existsByUserId(user.getId())) {
            throw new RuntimeException("Profile already exists!");
        }

        Profile profile = new Profile();
        profile.setUser(user);

        mapDtoToEntity(dto, profile);

        // Save user if firstName/lastName were updated
        userRepository.save(user);

        Profile saved = repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }

    // ================= UPDATE =================
    public ProfileResponseDTO updateMyProfile(UpdateProfileRequestDTO dto) {

        User user = getCurrentUser(); // ✅ FIXED

        Profile profile = repository.findByUserIdWithRelations(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        mapUpdateDto(dto, profile);

        // Save user if firstName/lastName were updated
        userRepository.save(user);

        Profile saved = repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }

    // ================= GET =================
    @Transactional(readOnly = true)
    public ProfileResponseDTO getMyProfile() {
        return mapToDTO(repository.findByUserIdWithRelations(getCurrentUser().getId())
                .orElseThrow(() -> new RuntimeException("Profile not found")));
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfileById(Long id) {
        return mapToDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found")));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!profile.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access Denied");
        }

        repository.delete(profile);
    }

    // ================= SAVE =================
    @Override
    public Profile saveProfile(Profile profile) {

        User user = getCurrentUser();

        Optional<Profile> existing = repository.findByUserId(user.getId());

        if (existing.isPresent()) {
            Profile p = existing.get();

            if (profile.getImageUrl() != null) p.setImageUrl(profile.getImageUrl());
            if (profile.getAbout() != null) p.setAbout(profile.getAbout());

            return repository.save(p);
        }

        profile.setUser(user);
        profile.setIsActive(true);

        return repository.save(profile);
    }

    // ================= SAVE WITH USER =================
    public Profile saveProfile(Profile profile, User user) {

        Optional<Profile> existing = repository.findByUserId(user.getId());

        if (existing.isPresent()) {
            Profile p = existing.get();

            if (profile.getImageUrl() != null) p.setImageUrl(profile.getImageUrl());
            if (profile.getAbout() != null) p.setAbout(profile.getAbout());

            return repository.save(p);
        }

        profile.setUser(user);
        profile.setIsActive(true);

        return repository.save(profile);
    }

    // ================= READ =================
    @Override public Optional<Profile> getById(Long id) { return repository.findById(id); }
    @Override public Optional<Profile> getByUserId(Long userId) { return repository.findByUserIdWithRelations(userId); }
    @Override @Transactional(readOnly = true) public List<Profile> getAll() { return repository.findAllWithUser(); }
    @Override @Transactional(readOnly = true) public List<Profile> getActiveProfiles() { return repository.findActiveProfilesWithUser(); }

    // ================= FILTER =================
    @Override public List<Profile> getByReligion(Long id) { return repository.findByReligionId(id); }
    @Override public List<Profile> getByCaste(Long id) { return repository.findByCasteId(id); }
    @Override public List<Profile> getByCity(Long id) { return repository.findByCityId(id); }
    @Override public List<Profile> getByEducation(Long id) { return repository.findByEducationLevelId(id); }
    @Override public List<Profile> getByOccupation(Long id) { return repository.findByOccupationId(id); }

    // ================= COMBINATIONS =================
    @Override public List<Profile> getByReligionAndCaste(Long r, Long c) { return repository.findByReligionIdAndCasteId(r, c); }
    @Override public List<Profile> getByCityAndEducation(Long c, Long e) { return repository.findByCityIdAndEducationLevelId(c, e); }
    @Override public List<Profile> getByOccupationAndCity(Long o, Long c) { return repository.findByOccupationIdAndCityId(o, c); }
    @Override public List<Profile> getActiveByReligionAndCity(Long r, Long c) { return repository.findByReligionIdAndCityIdAndIsActiveTrue(r, c); }

    // ================= FIXED MAPPER =================
    private void mapDtoToEntity(ProfileRequestDTO dto, Profile p) {
        // Map basic profile fields
        p.setGender(dto.getGender());
        p.setDateOfBirth(dto.getDateOfBirth());
        p.setAbout(dto.getAbout());
        p.setImageUrl(dto.getImageUrl());

        // Update user's first name and last name if provided
        if (dto.getFirstName() != null || dto.getLastName() != null) {
            User user = p.getUser();
            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(dto.getLastName());
            }
        }

        // Map religion fields
        if (dto.getReligionId() != null)
            p.setReligion(religionRepository.findById(dto.getReligionId()).orElse(null));

        // Map caste fields
        if (dto.getCasteId() != null)
            p.setCaste(casteRepository.findById(dto.getCasteId()).orElse(null));

        // Map education level fields
        if (dto.getEducationLevelId() != null)
            p.setEducationLevel(educationRepository.findById(dto.getEducationLevelId()).orElse(null));

        // Map occupation fields
        if (dto.getOccupationId() != null)
            p.setOccupation(occupationRepository.findById(dto.getOccupationId()).orElse(null));

        // Map height fields
        if (dto.getHeightId() != null)
            p.setHeight(heightRepository.findById(dto.getHeightId()).orElse(null));

        // Map weight fields
        if (dto.getWeightId() != null)
            p.setWeight(weightRepository.findById(dto.getWeightId()).orElse(null));

        // Map city fields
        if (dto.getCityId() != null)
            p.setCity(cityRepository.findById(dto.getCityId()).orElse(null));

        // Map mother tongue fields
        if (dto.getMotherTongueId() != null)
            p.setMotherTongue(motherTongueRepository.findById(dto.getMotherTongueId()).orElse(null));

        // Map marital status fields
        if (dto.getMaritalStatusId() != null)
            p.setMaritalStatus(maritalStatusRepository.findById(dto.getMaritalStatusId()).orElse(null));
    }

    private void mapUpdateDto(UpdateProfileRequestDTO dto, Profile p) {
        // Map basic profile fields
        if (dto.getGender() != null)
            p.setGender(dto.getGender());

        if (dto.getDateOfBirth() != null)
            p.setDateOfBirth(dto.getDateOfBirth());

        // Handle both about and aboutMe for frontend compatibility
        if (dto.getAbout() != null)
            p.setAbout(dto.getAbout());
        if (dto.getAboutMe() != null)
            p.setAbout(dto.getAboutMe());

        if (dto.getImageUrl() != null)
            p.setImageUrl(dto.getImageUrl());

        // Update user's first name and last name if provided
        if (dto.getFirstName() != null || dto.getLastName() != null) {
            User user = p.getUser();
            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(dto.getLastName());
            }
        }

        // Map religion fields
        if (dto.getReligionId() != null)
            p.setReligion(religionRepository.findById(dto.getReligionId()).orElse(null));

        // Map caste fields
        if (dto.getCasteId() != null)
            p.setCaste(casteRepository.findById(dto.getCasteId()).orElse(null));

        // Map sub caste fields
        if (dto.getSubCasteId() != null) {
            p.setSubCaste(subCasteRepository.findById(dto.getSubCasteId()).orElse(null));
        }

        // Map education level fields
        if (dto.getEducationLevelId() != null)
            p.setEducationLevel(educationRepository.findById(dto.getEducationLevelId()).orElse(null));

        // Map occupation fields
        if (dto.getOccupationId() != null)
            p.setOccupation(occupationRepository.findById(dto.getOccupationId()).orElse(null));

        // Map height fields
        if (dto.getHeightId() != null)
            p.setHeight(heightRepository.findById(dto.getHeightId()).orElse(null));

        // Map weight fields
        if (dto.getWeightId() != null)
            p.setWeight(weightRepository.findById(dto.getWeightId()).orElse(null));

        // Map city fields
        if (dto.getCityId() != null)
            p.setCity(cityRepository.findById(dto.getCityId()).orElse(null));

        // Map mother tongue fields
        if (dto.getMotherTongueId() != null)
            p.setMotherTongue(motherTongueRepository.findById(dto.getMotherTongueId()).orElse(null));

        // Map marital status fields
        if (dto.getMaritalStatusId() != null)
            p.setMaritalStatus(maritalStatusRepository.findById(dto.getMaritalStatusId()).orElse(null));

        // Map physical details
        if (dto.getComplexion() != null)
            p.setComplexion(dto.getComplexion());
        if (dto.getBodyType() != null)
            p.setBodyType(dto.getBodyType());

        // Map education & career
        if (dto.getAnnualIncome() != null)
            p.setAnnualIncome(dto.getAnnualIncome());
        if (dto.getCompanyName() != null)
            p.setCompanyName(dto.getCompanyName());

        // Map location
        if (dto.getCountry() != null)
            p.setCountry(dto.getCountry());
        if (dto.getState() != null)
            p.setState(dto.getState());
        if (dto.getAddress() != null)
            p.setAddress(dto.getAddress());

        // Map lifestyle
        if (dto.getDiet() != null)
            p.setDiet(dto.getDiet());
        if (dto.getSmoking() != null)
            p.setSmoking(dto.getSmoking());
        if (dto.getDrinking() != null)
            p.setDrinking(dto.getDrinking());

        // Map family details
        if (dto.getFatherName() != null)
            p.setFatherName(dto.getFatherName());
        if (dto.getFatherOccupation() != null)
            p.setFatherOccupation(dto.getFatherOccupation());
        if (dto.getMotherName() != null)
            p.setMotherName(dto.getMotherName());
        if (dto.getMotherOccupation() != null)
            p.setMotherOccupation(dto.getMotherOccupation());
        if (dto.getSiblingsCount() != null)
            p.setSiblingsCount(dto.getSiblingsCount());

        // Map partner preferences
        if (dto.getPreferredAgeMin() != null)
            p.setPreferredAgeMin(dto.getPreferredAgeMin());
        if (dto.getPreferredAgeMax() != null)
            p.setPreferredAgeMax(dto.getPreferredAgeMax());
        if (dto.getPreferredLocation() != null)
            p.setPreferredLocation(dto.getPreferredLocation());
        if (dto.getPreferredEducation() != null)
            p.setPreferredEducation(dto.getPreferredEducation());
        if (dto.getOtherExpectations() != null)
            p.setOtherExpectations(dto.getOtherExpectations());
    }

    public ProfileResponseDTO mapToDTO(Profile p) {
        System.out.println("🔄 Mapping Profile to DTO - Profile ID: " + p.getId());
        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setId(p.getId());
        
        // Safely map user fields
        if (p.getUser() != null) {
            dto.setUserId(p.getUser().getId());
            dto.setUserName(p.getUser().getFullName());
            dto.setFirstName(p.getUser().getFirstName());
            dto.setLastName(p.getUser().getLastName());
            dto.setMiddleName(p.getUser().getMiddleName());
            dto.setEmail(p.getUser().getEmail());
            dto.setPhone(p.getUser().getPhone());
            System.out.println("   - User fields mapped: " + p.getUser().getFullName());
        }
        
        dto.setGender(p.getGender());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setImageUrl(p.getImageUrl());
        dto.setAbout(p.getAbout());
        dto.setAboutMe(p.getAbout()); // Map about to aboutMe for frontend compatibility
        dto.setIsActive(p.getIsActive());
        dto.setCurrentStep(p.getCurrentStep());
        dto.setProfileCompleted(p.getProfileCompleted());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        // Map religion fields
        if (p.getReligion() != null) {
            dto.setReligionId(p.getReligion().getId());
            dto.setReligionName(p.getReligion().getName());
        }

        // Map caste fields
        if (p.getCaste() != null) {
            dto.setCasteId(p.getCaste().getId());
            dto.setCasteName(p.getCaste().getName());
        }

        // Map sub caste fields
        if (p.getSubCaste() != null) {
            dto.setSubCasteId(p.getSubCaste().getId());
            dto.setSubCasteName(p.getSubCaste().getName());
        }

        // Map education level fields
        if (p.getEducationLevel() != null) {
            dto.setEducationLevelId(p.getEducationLevel().getId());
            dto.setEducationLevelName(p.getEducationLevel().getName());
        }

        // Map occupation fields
        if (p.getOccupation() != null) {
            dto.setOccupationId(p.getOccupation().getId());
            dto.setOccupationName(p.getOccupation().getName());
        }

        // Map height fields
        if (p.getHeight() != null) {
            dto.setHeightId(p.getHeight().getId());
            dto.setHeightValue(p.getHeight().getHeight());
        }

        // Map weight fields
        if (p.getWeight() != null) {
            dto.setWeightId(p.getWeight().getId());
            dto.setWeightValue(p.getWeight().getValue());
        }

        // Map city fields
        if (p.getCity() != null) {
            dto.setCityId(p.getCity().getId());
            dto.setCityName(p.getCity().getName());
        }

        // Map mother tongue fields
        if (p.getMotherTongue() != null) {
            dto.setMotherTongueId(p.getMotherTongue().getId());
            dto.setMotherTongueName(p.getMotherTongue().getName());
        }

        // Map marital status fields
        if (p.getMaritalStatus() != null) {
            dto.setMaritalStatusId(p.getMaritalStatus().getId());
            dto.setMaritalStatusName(p.getMaritalStatus().getName());
        }

        // Map physical details
        dto.setComplexion(p.getComplexion());
        dto.setBodyType(p.getBodyType());

        // Map education & career
        dto.setAnnualIncome(p.getAnnualIncome());
        dto.setCompanyName(p.getCompanyName());

        // Map location
        dto.setCountry(p.getCountry());
        dto.setState(p.getState());
        dto.setAddress(p.getAddress());

        // Map lifestyle
        dto.setDiet(p.getDiet());
        dto.setSmoking(p.getSmoking());
        dto.setDrinking(p.getDrinking());

        // Map family details
        dto.setFatherName(p.getFatherName());
        dto.setFatherOccupation(p.getFatherOccupation());
        dto.setMotherName(p.getMotherName());
        dto.setMotherOccupation(p.getMotherOccupation());
        dto.setSiblingsCount(p.getSiblingsCount());

        // Map partner preferences
        dto.setPreferredAgeMin(p.getPreferredAgeMin());
        dto.setPreferredAgeMax(p.getPreferredAgeMax());
        dto.setPreferredLocation(p.getPreferredLocation());
        dto.setPreferredEducation(p.getPreferredEducation());
        dto.setOtherExpectations(p.getOtherExpectations());

        return dto;
    }

    // ================= SEARCH =================
    public Page<ProfileResponseDTO> searchProfiles(PartnerPreference pref, Pageable pageable) {
        Specification<Profile> spec = ProfileSpecification.matchPreferences(pref);
        return repository.findAll(spec, pageable).map(this::mapToDTO);
    }

    // ================= REDIS SAFE =================
    private void safeRedis(Long userId) {
        try {
            cacheService.evictUserMatches(userId);
        } catch (Exception e) {
            System.out.println("Redis skipped");
        }

        asyncService.preloadMatches(userId);
    }
}
