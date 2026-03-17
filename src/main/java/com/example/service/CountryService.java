package com.example.service;

import com.example.model.Country;
import com.example.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository repo;

    // ✅ Get all countries
    public List<Country> getAll() {
        return repo.findAll();
    }

    // ✅ Get active countries
    public List<Country> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public Country getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    // ✅ Create country
    public Country create(Country country) {

        if (repo.existsByName(country.getName())) {
            throw new RuntimeException("Country already exists");
        }

        country.setStatus(true);

        return repo.save(country);
    }

    // ✅ Update country
    public Country update(Long id, Country updated) {

        Country existing = getById(id);

        // duplicate check if name changed
        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("Country already exists");
        }

        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setStates(updated.getStates());
        existing.setUpdatedAt(updated.getUpdatedAt());

        return repo.save(existing);
    }

    // ✅ Delete country
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get by admin
    public List<Country> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    public List<Country> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndStatusTrue(adminId);
    }

    // ✅ Search country
    public List<Country> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}