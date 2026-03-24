package com.example.serviceimpl;

import com.example.model.Country;
import com.example.repository.CountryRepository;
import com.example.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // ✅ Create
    @Override
    public Country create(Country country) {

        // 🔥 Duplicate check
        if (countryRepository.existsByNameIgnoreCase(country.getName())) {
            throw new RuntimeException("Country already exists: " + country.getName());
        }

        return countryRepository.save(country);
    }

    // 🔄 Update
    @Override
    public Country update(Long id, Country country) {

        Country existing = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        // 🔥 Duplicate check (exclude same record)
        countryRepository.findByNameIgnoreCase(country.getName())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new RuntimeException("Country already exists: " + country.getName());
                    }
                });

        // ✏️ Update fields
        existing.setName(country.getName());
        existing.setIsActive(country.getIsActive());

        return countryRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Country existing = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        countryRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Country> getById(Long id) {
        return countryRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    // 🔍 Find by name
    @Override
    public Optional<Country> getByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    public Optional<Country> getByNameIgnoreCase(String name) {
        return countryRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return countryRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Country> getActive() {
        return countryRepository.findByIsActiveTrue();
    }

    @Override
    public List<Country> getInactive() {
        return countryRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Country> getByAdmin(Long adminId) {
        return countryRepository.findByAdminId(adminId);
    }

    @Override
    public List<Country> getActiveByAdmin(Long adminId) {
        return countryRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Country> search(String keyword) {
        return countryRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Country> searchByAdmin(Long adminId, String keyword) {
        return countryRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}