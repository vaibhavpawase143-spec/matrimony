package com.example.serviceimpl;

import com.example.model.Family;
import com.example.repository.FamilyRepository;
import com.example.service.FamilyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;

    public FamilyServiceImpl(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    @Override
    public Family create(Family family) {
        if (familyRepository.existsByNameIgnoreCase(family.getName())) {
            throw new RuntimeException("Family already exists: " + family.getName());
        }
        return familyRepository.save(family);
    }

    @Override
    public Family update(Long id, Family family) {
        Family existing = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found with id: " + id));

        familyRepository.findByNameIgnoreCase(family.getName())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new RuntimeException("Family already exists: " + family.getName());
                    }
                });

        existing.setName(family.getName());
        existing.setIsActive(family.getIsActive());

        return familyRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Family existing = familyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family not found with id: " + id));

        familyRepository.delete(existing);
    }

    @Override
    public Optional<Family> getById(Long id) {
        return familyRepository.findById(id);
    }

    @Override
    public List<Family> getAll() {
        return familyRepository.findAll();
    }

    @Override
    public Optional<Family> getByName(String name) {
        return familyRepository.findByName(name);
    }

    @Override
    public Optional<Family> getByNameIgnoreCase(String name) {
        return familyRepository.findByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByName(String name) {
        return familyRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return familyRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public List<Family> getActive() {
        return familyRepository.findByIsActiveTrue();
    }

    @Override
    public List<Family> getInactive() {
        return familyRepository.findByIsActiveFalse();
    }

    @Override
    public List<Family> getByAdmin(Long adminId) {
        return familyRepository.findByAdminId(adminId);
    }

    @Override
    public List<Family> getActiveByAdmin(Long adminId) {
        return familyRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Family> search(String keyword) {
        return familyRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Family> searchByAdmin(Long adminId, String keyword) {
        return familyRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}