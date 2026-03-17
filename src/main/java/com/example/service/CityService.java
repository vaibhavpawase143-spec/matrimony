package com.example.service;

import com.example.model.City;
import com.example.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository repo;

    // ✅ Get all cities
    public List<City> getAll() {
        return repo.findAll();
    }

    // ✅ Get active cities
    public List<City> getActive() {
        return repo.findByStatusTrue();
    }

    // ✅ Get by ID
    public City getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
    }

    // ✅ Create city
    public City create(City city) {

        if (repo.existsByName(city.getName())) {
            throw new RuntimeException("City already exists");
        }

        city.setStatus(true);

        return repo.save(city);
    }

    // ✅ Update city
    public City update(Long id, City updated) {

        City existing = getById(id);

        // duplicate check if name changed
        if (!existing.getName().equals(updated.getName())
                && repo.existsByName(updated.getName())) {
            throw new RuntimeException("City already exists");
        }

        existing.setName(updated.getName());
        existing.setStatus(updated.getStatus());
        existing.setAdmin(updated.getAdmin());
        existing.setState(updated.getState());

        return repo.save(existing);
    }

    // ✅ Delete city
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Get cities by state
    public List<City> getByState(Long stateId) {
        return repo.findByStateId(stateId);
    }

    // ✅ Get active cities by state (dropdown use)
    public List<City> getActiveByState(Long stateId) {
        return repo.findByStateIdAndStatusTrue(stateId);
    }

    // ✅ Get by admin
    public List<City> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Search city (global)
    public List<City> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Search city within state
    public List<City> searchByState(Long stateId, String keyword) {
        return repo.findByStateIdAndNameContainingIgnoreCase(stateId, keyword);
    }
}