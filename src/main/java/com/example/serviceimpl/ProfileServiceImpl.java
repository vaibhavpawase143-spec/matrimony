package com.example.serviceimpl;

import com.example.model.Profile;
import com.example.repository.ProfileRepository;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository repo;

    // ✅ Create
    @Override
    public Profile create(Profile profile) {
        return repo.save(profile);
    }

    // ✅ Update by User ID
    @Override
    public Profile update(Long userId, Profile updated) {
        Profile existing = repo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));

        // Update fields
        existing.setAbout(updated.getAbout());
        existing.setReligion(updated.getReligion());
        existing.setCaste(updated.getCaste());
        existing.setEducationLevel(updated.getEducationLevel());
        existing.setOccupation(updated.getOccupation());
        existing.setHeight(updated.getHeight());
        existing.setWeight(updated.getWeight());
        existing.setCity(updated.getCity());
        existing.setUser(updated.getUser()); // update linked user if needed

        return repo.save(existing);
    }

    // ✅ Get by User ID
    @Override
    public Optional<Profile> getByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    // ✅ Delete by User ID
    @Override
    public void delete(Long userId) {
        Profile existing = repo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));
        repo.delete(existing);
    }

    // ✅ Get all
    @Override
    public List<Profile> getAll() {
        return repo.findAll();
    }

    // ✅ Get by Religion
    @Override
    public List<Profile> getByReligion(Long religionId) {
        return repo.findByReligion_Id(religionId);
    }

    // ✅ Get by Caste
    @Override
    public List<Profile> getByCaste(Long casteId) {
        return repo.findByCaste_Id(casteId);
    }

    // ✅ Get by City
    @Override
    public List<Profile> getByCity(Long cityId) {
        return repo.findByCity_Id(cityId);
    }

    // ✅ Get by Education Level
    @Override
    public List<Profile> getByEducation(Long educationLevelId) {
        return repo.findByEducationLevel_Id(educationLevelId);
    }

    // ✅ Get by Occupation
    @Override
    public List<Profile> getByOccupation(Long occupationId) {
        return repo.findByOccupation_Id(occupationId);
    }
}