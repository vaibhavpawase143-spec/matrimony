package com.example.serviceimpl;

import com.example.model.Country;
import com.example.repository.CountryRepository;
import com.example.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository repo;

    // ✅ Get all countries
    @Override
    public List<Country> getAll() {
        return repo.findAll();
    }

    // ✅ Get active countries
    @Override
    public List<Country> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public Country getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
    }

    // ✅ Create country
    @Override
    public Country create(Country country) {
        country.setCreatedAt(LocalDateTime.now());
        country.setUpdatedAt(LocalDateTime.now());
        country.setIsActive(true);
        return repo.save(country);
    }

    // ✅ Update country
    @Override
    public Country update(Long id, Country country) {
        Country existing = getById(id);

        existing.setName(country.getName());
        existing.setIsActive(country.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete country
    @Override
    public void delete(Long id) {
        Country existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Get by admin
    @Override
    public List<Country> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Get active by admin
    @Override
    public List<Country> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    // ✅ Search country
    @Override
    public List<Country> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}