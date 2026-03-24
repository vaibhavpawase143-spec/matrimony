package com.example.serviceimpl;

import com.example.model.ProfileType;
import com.example.repository.ProfileTypeRepository;
import com.example.service.ProfileTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileTypeServiceImpl implements ProfileTypeService {

    private final ProfileTypeRepository repository;

    public ProfileTypeServiceImpl(ProfileTypeRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public ProfileType save(ProfileType profileType) {

        String name = profileType.getName();
        Long adminId = profileType.getAdmin().getId();

        Optional<ProfileType> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(profileType.getId())) {
            throw new RuntimeException("ProfileType already exists for this admin!");
        }

        return repository.save(profileType);
    }

    // ✅ Get by ID
    @Override
    public Optional<ProfileType> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<ProfileType> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<ProfileType> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<ProfileType> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<ProfileType> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<ProfileType> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<ProfileType> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}