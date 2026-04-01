package com.example.serviceimpl;

import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.service.ProfileService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final UserRepository userRepository;

    // ================= CURRENT USER =================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailIgnoreCase(email) // ✅ FIXED
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= SAVE PROFILE =================
    @Override
    public Profile saveProfile(Profile profile) {

        User currentUser = getCurrentUser();

        if (repository.existsByUserId(currentUser.getId())) {
            throw new RuntimeException("Profile already exists for this user!");
        }

        profile.setUser(currentUser); // 🔥 FORCE ownership

        return repository.save(profile);
    }

    // ================= GET BY ID =================
    @Override
    public Optional<Profile> getById(Long id) {

        Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        User currentUser = getCurrentUser();

        boolean isOwner = profile.getUser().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return Optional.of(profile);
    }

    // ================= GET MY PROFILE =================
    @Override
    public Optional<Profile> getByUserId(Long userId) {
        User currentUser = getCurrentUser();
        return repository.findByUserId(currentUser.getId());
    }

    // ================= GET ALL =================
    @Override
    public List<Profile> getAll() {

        User currentUser = getCurrentUser();

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return repository.findAll();
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {

        Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        User currentUser = getCurrentUser();

        boolean isOwner = profile.getUser().getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        repository.delete(profile);
    }

    // ================= FILTER METHODS =================

    @Override
    public List<Profile> getActiveProfiles() {
        return repository.findByIsActiveTrue();
    }

    @Override
    public List<Profile> getByReligion(Long religionId) {
        return repository.findByReligionId(religionId);
    }

    @Override
    public List<Profile> getByCaste(Long casteId) {
        return repository.findByCasteId(casteId);
    }

    @Override
    public List<Profile> getByCity(Long cityId) {
        return repository.findByCityId(cityId);
    }

    @Override
    public List<Profile> getByEducation(Long educationLevelId) {
        return repository.findByEducationLevelId(educationLevelId);
    }

    @Override
    public List<Profile> getByOccupation(Long occupationId) {
        return repository.findByOccupationId(occupationId);
    }

    @Override
    public List<Profile> getByReligionAndCaste(Long religionId, Long casteId) {
        return repository.findByReligionIdAndCasteId(religionId, casteId);
    }

    @Override
    public List<Profile> getByCityAndEducation(Long cityId, Long educationLevelId) {
        return repository.findByCityIdAndEducationLevelId(cityId, educationLevelId);
    }

    @Override
    public List<Profile> getByOccupationAndCity(Long occupationId, Long cityId) {
        return repository.findByOccupationIdAndCityId(occupationId, cityId);
    }

    @Override
    public List<Profile> getActiveByReligionAndCity(Long religionId, Long cityId) {
        return repository.findByReligionIdAndCityIdAndIsActiveTrue(religionId, cityId);
    }
}