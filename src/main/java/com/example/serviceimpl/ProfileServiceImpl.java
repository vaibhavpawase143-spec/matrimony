package com.example.serviceimpl;

import com.example.dto.request.ProfileRequestDTO;
import com.example.dto.request.UpdateProfileRequestDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ProfileService;

import com.example.specification.ProfileSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final UserRepository userRepository;

    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final EducationLevelRepository educationRepository;
    private final OccupationRepository occupationRepository;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;
    private final CityRepository cityRepository;

    // ================= CURRENT USER =================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= CREATE PROFILE =================
    public ProfileResponseDTO createProfile(ProfileRequestDTO dto) {

        User currentUser = getCurrentUser();

        if (repository.existsByUserId(currentUser.getId())) {
            throw new RuntimeException("Profile already exists!");
        }

        Profile profile = new Profile();
        profile.setUser(currentUser);

        mapDtoToEntity(dto, profile);

        return mapToDTO(repository.save(profile));
    }

    // ================= UPDATE PROFILE =================
    public ProfileResponseDTO updateMyProfile(UpdateProfileRequestDTO dto) {

        User currentUser = getCurrentUser();

        Profile profile = repository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        mapUpdateDto(dto, profile);

        return mapToDTO(repository.save(profile));
    }

    // ================= GET MY PROFILE =================
    public ProfileResponseDTO getMyProfile() {
        User currentUser = getCurrentUser();

        Profile profile = repository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToDTO(profile);
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        User currentUser = getCurrentUser();

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access Denied");
        }

        repository.delete(profile);
    }

    // ================= SAVE (CREATE OR UPDATE) =================
    @Override
    public Profile saveProfile(Profile profile) {

        User currentUser = getCurrentUser();

        Optional<Profile> existingOpt = repository.findByUserId(currentUser.getId());

        if (existingOpt.isPresent()) {

            Profile existing = existingOpt.get();

            if (!existing.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Access Denied");
            }

            // 🔁 Update fields (NULL SAFE)
            if (profile.getReligion() != null)
                existing.setReligion(profile.getReligion());

            if (profile.getCaste() != null)
                existing.setCaste(profile.getCaste());

            if (profile.getEducationLevel() != null)
                existing.setEducationLevel(profile.getEducationLevel());

            if (profile.getOccupation() != null)
                existing.setOccupation(profile.getOccupation());

            if (profile.getHeight() != null)
                existing.setHeight(profile.getHeight());

            if (profile.getWeight() != null)
                existing.setWeight(profile.getWeight());

            if (profile.getCity() != null)
                existing.setCity(profile.getCity());

            if (profile.getAbout() != null)
                existing.setAbout(profile.getAbout());

            if (profile.getIsActive() != null)
                existing.setIsActive(profile.getIsActive());

            return repository.save(existing);

        } else {

            profile.setUser(currentUser);

            if (profile.getIsActive() == null) {
                profile.setIsActive(true);
            }

            return repository.save(profile);
        }
    }

    // ================= READ =================
    @Override public Optional<Profile> getById(Long id) { return repository.findById(id); }
    @Override public Optional<Profile> getByUserId(Long userId) { return repository.findByUserId(userId); }
    @Override public List<Profile> getAll() { return repository.findAll(); }
    @Override public List<Profile> getActiveProfiles() { return repository.findByIsActiveTrue(); }

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

    // ================= MAPPERS =================
    private void mapDtoToEntity(ProfileRequestDTO dto, Profile profile) {
        profile.setReligion(religionRepository.findById(dto.getReligionId()).orElse(null));
        profile.setCaste(casteRepository.findById(dto.getCasteId()).orElse(null));
        profile.setEducationLevel(educationRepository.findById(dto.getEducationLevelId()).orElse(null));
        profile.setOccupation(occupationRepository.findById(dto.getOccupationId()).orElse(null));
        profile.setHeight(heightRepository.findById(dto.getHeightId()).orElse(null));
        profile.setWeight(weightRepository.findById(dto.getWeightId()).orElse(null));
        profile.setCity(cityRepository.findById(dto.getCityId()).orElse(null));
        profile.setAbout(dto.getAbout());
    }

    private void mapUpdateDto(UpdateProfileRequestDTO dto, Profile profile) {
        if (dto.getReligionId() != null)
            profile.setReligion(religionRepository.findById(dto.getReligionId()).orElse(null));

        if (dto.getCasteId() != null)
            profile.setCaste(casteRepository.findById(dto.getCasteId()).orElse(null));

        if (dto.getCityId() != null)
            profile.setCity(cityRepository.findById(dto.getCityId()).orElse(null));

        if (dto.getAbout() != null)
            profile.setAbout(dto.getAbout());
    }

    private ProfileResponseDTO mapToDTO(Profile p) {
        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setId(p.getId());
        dto.setUserId(p.getUser().getId());
        dto.setUserName(p.getUser().getFullName());

        if (p.getReligion() != null) {
            dto.setReligionId(p.getReligion().getId());
            dto.setReligionName(p.getReligion().getName());
        }

        if (p.getCity() != null) {
            dto.setCityId(p.getCity().getId());
            dto.setCityName(p.getCity().getName());
        }

        dto.setAbout(p.getAbout());
        dto.setIsActive(p.getIsActive());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        return dto;
    }
    public Page<ProfileResponseDTO> searchProfiles(
            PartnerPreference pref,
            Pageable pageable
    ) {

        Specification<Profile> spec = ProfileSpecification.matchPreferences(pref);

        Page<Profile> page = repository.findAll(spec, pageable);

        return page.map(this::mapToDTO);
    }
}