package com.example.serviceimpl;

import com.example.model.Profile;
import com.example.repository.ProfileRepository;
import com.example.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    // ✅ Save profile (One profile per user)
    @Override
    public Profile saveProfile(Profile profile) {

        Long userId = profile.getUser().getId();

        if (repository.existsByUserId(userId)) {
            throw new RuntimeException("Profile already exists for this user!");
        }

        return repository.save(profile);
    }

    // ✅ Get by ID
    @Override
    public Optional<Profile> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get by userId
    @Override
    public Optional<Profile> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    // 🔍 Get all
    @Override
    public List<Profile> getAll() {
        return repository.findAll();
    }

    // 🔥 Active profiles
    @Override
    public List<Profile> getActiveProfiles() {
        return repository.findByIsActiveTrue();
    }

    // 🔍 Filters

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

    // 🔥 Advanced filters

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

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}