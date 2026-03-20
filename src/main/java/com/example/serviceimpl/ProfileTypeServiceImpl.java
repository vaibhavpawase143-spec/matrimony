package com.example.serviceimpl;

import com.example.model.ProfileType;
import com.example.repository.ProfileTypeRepository;
import com.example.service.ProfileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileTypeServiceImpl implements ProfileTypeService {

    @Autowired
    private ProfileTypeRepository repo;

    // ✅ Create
    @Override
    public ProfileType create(ProfileType profileType) {
        profileType.setCreatedAt(LocalDateTime.now());
        profileType.setUpdatedAt(LocalDateTime.now());
        profileType.setIsActive(true);
        return repo.save(profileType);
    }

    // ✅ Get all
    @Override
    public List<ProfileType> getAll() {
        return repo.findAll();
    }

    // ✅ Get by ID
    @Override
    public Optional<ProfileType> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public ProfileType update(Long id, ProfileType profileType) {
        ProfileType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProfileType not found with id: " + id));

        existing.setName(profileType.getName());
        existing.setIsActive(profileType.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        ProfileType existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProfileType not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Get by admin
    @Override
    public List<ProfileType> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Search
    @Override
    public List<ProfileType> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}