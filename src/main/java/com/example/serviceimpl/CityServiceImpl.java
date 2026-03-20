package com.example.serviceimpl;

import com.example.model.City;
import com.example.repository.CityRepository;
import com.example.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository repo;

    // ✅ Get all
    @Override
    public List<City> getAll() {
        return repo.findAll();
    }

    // ✅ Get active
    @Override
    public List<City> getActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public City getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

    // ✅ Create
    @Override
    public City create(City city) {
        city.setIsActive(true);
        return repo.save(city);
    }

    // ✅ Update
    @Override
    public City update(Long id, City city) {
        City existing = getById(id);

        existing.setName(city.getName());
        existing.setState(city.getState());
        existing.setIsActive(city.getIsActive());

        return repo.save(existing);
    }

    // ✅ Delete (Soft delete)
    @Override
    public void delete(Long id) {
        City existing = getById(id);
        existing.setIsActive(false);
        repo.save(existing);
    }

    // ✅ By state
    @Override
    public List<City> getByState(Long stateId) {
        return repo.findByState_Id(stateId);
    }

    @Override
    public List<City> getActiveByState(Long stateId) {
        return repo.findByState_IdAndIsActiveTrue(stateId);
    }

    // ✅ By admin
    @Override
    public List<City> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Search
    @Override
    public List<City> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<City> searchByState(Long stateId, String keyword) {
        return repo.findByState_IdAndNameContainingIgnoreCase(stateId, keyword);
    }
}