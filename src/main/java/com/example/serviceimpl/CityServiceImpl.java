package com.example.service.impl;

import com.example.model.City;
import com.example.repository.CityRepository;
import com.example.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    // 🔍 Basic CRUD
    @Override
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    // 🔍 Find by name
    @Override
    public Optional<City> getByName(String name) {
        return cityRepository.findByName(name);
    }

    @Override
    public Optional<City> getByNameIgnoreCase(String name) {
        return cityRepository.findByNameIgnoreCase(name);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByName(String name) {
        return cityRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return cityRepository.existsByNameIgnoreCase(name);
    }

    // 🔍 Active / Inactive
    @Override
    public List<City> getActiveCities() {
        return cityRepository.findByIsActiveTrue();
    }

    @Override
    public List<City> getInactiveCities() {
        return cityRepository.findByIsActiveFalse();
    }

    // 🔍 State-based
    @Override
    public List<City> getCitiesByState(Long stateId) {
        return cityRepository.findByState_Id(stateId);
    }

    @Override
    public List<City> getActiveCitiesByState(Long stateId) {
        return cityRepository.findByState_IdAndIsActiveTrue(stateId);
    }

    @Override
    public Optional<City> getByStateAndName(Long stateId, String name) {
        return cityRepository.findByState_IdAndNameIgnoreCase(stateId, name);
    }

    // 🔍 Admin-based
    @Override
    public List<City> getCitiesByAdmin(Long adminId) {
        return cityRepository.findByAdminId(adminId);
    }

    @Override
    public List<City> getActiveCitiesByAdmin(Long adminId) {
        return cityRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<City> searchByName(String keyword) {
        return cityRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<City> searchByState(Long stateId, String keyword) {
        return cityRepository.findByState_IdAndNameContainingIgnoreCase(stateId, keyword);
    }

    @Override
    public List<City> searchByAdmin(Long adminId, String keyword) {
        return cityRepository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}