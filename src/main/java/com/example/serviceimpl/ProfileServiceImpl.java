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
    private final EducationLevelRepository educationRepository;
    private final OccupationRepository occupationRepository;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final CityRepository cityRepository;
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

        Profile saved = repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }

    // ================= UPDATE =================
    public ProfileResponseDTO updateMyProfile(UpdateProfileRequestDTO dto) {

        User user = getCurrentUser(); // ✅ FIXED

        Profile profile = repository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        mapUpdateDto(dto, profile);

        Profile saved = repository.save(profile);

        safeRedis(user.getId());

        return mapToDTO(saved);
    }

    // ================= GET =================
    public ProfileResponseDTO getMyProfile() {
        return mapToDTO(repository.findByUserId(getCurrentUser().getId())
                .orElseThrow(() -> new RuntimeException("Profile not found")));
    }

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

    // ================= READ =================
    @Override public Optional<Profile> getById(Long id) { return repository.findById(id); }
    @Override public Optional<Profile> getByUserId(Long userId) { return repository.findByUserId(userId); }
    @Override public List<Profile> getAll() { return repository.findAllWithUser(); }
    @Override public List<Profile> getActiveProfiles() { return repository.findActiveProfilesWithUser(); }

    // ================= FILTER =================
    @Override public List<Profile> getByReligion(Long id) { return repository.findByReligionId(id); }
    @Override public List<Profile> getByCaste(Long id) { return repository.findByCasteId(id); }
    @Override public List<Profile> getByCity(Long id) { return repository.findByCityId(id); }
    @Override public List<Profile> getByEducation(Long id) { return repository.findByEducationLevelId(id); }
    @Override public List<Profile> getByOccupation(Long id) { return repository.findByOccupationId(id); }

    @Override public List<Profile> getByReligionAndCaste(Long r, Long c) { return repository.findByReligionIdAndCasteId(r, c); }
    @Override public List<Profile> getByCityAndEducation(Long c, Long e) { return repository.findByCityIdAndEducationLevelId(c, e); }
    @Override public List<Profile> getByOccupationAndCity(Long o, Long c) { return repository.findByOccupationIdAndCityId(o, c); }
    @Override public List<Profile> getActiveByReligionAndCity(Long r, Long c) { return repository.findByReligionIdAndCityIdAndIsActiveTrue(r, c); }

    // ================= FIXED MAPPER =================
    private void mapDtoToEntity(ProfileRequestDTO dto, Profile p) {

        if (dto.getReligionId() != null)
            p.setReligion(religionRepository.findById(dto.getReligionId()).orElse(null));

        if (dto.getCasteId() != null)
            p.setCaste(casteRepository.findById(dto.getCasteId()).orElse(null));

        if (dto.getEducationLevelId() != null)
            p.setEducationLevel(educationRepository.findById(dto.getEducationLevelId()).orElse(null));

        if (dto.getOccupationId() != null)
            p.setOccupation(occupationRepository.findById(dto.getOccupationId()).orElse(null));

        if (dto.getHeightId() != null)
            p.setHeight(heightRepository.findById(dto.getHeightId()).orElse(null));

        if (dto.getWeightId() != null)
            p.setWeight(weightRepository.findById(dto.getWeightId()).orElse(null));

        if (dto.getCityId() != null)
            p.setCity(cityRepository.findById(dto.getCityId()).orElse(null));

        p.setAbout(dto.getAbout());
        p.setImageUrl(dto.getImageUrl());
    }

    private void mapUpdateDto(UpdateProfileRequestDTO dto, Profile p) {

        if (dto.getCityId() != null)
            p.setCity(cityRepository.findById(dto.getCityId()).orElse(null));

        if (dto.getAbout() != null)
            p.setAbout(dto.getAbout());

        if (dto.getImageUrl() != null)
            p.setImageUrl(dto.getImageUrl());
    }

    public ProfileResponseDTO mapToDTO(Profile p) {
        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setId(p.getId());
        dto.setUserId(p.getUser().getId());
        dto.setUserName(p.getUser().getFullName());
        dto.setImageUrl(p.getImageUrl());
        dto.setAbout(p.getAbout());
        dto.setIsActive(p.getIsActive());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        if (p.getCity() != null) {
            dto.setCityId(p.getCity().getId());
            dto.setCityName(p.getCity().getName());
        }

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