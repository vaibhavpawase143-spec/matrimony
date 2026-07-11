package com.example.service;

import com.example.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService {

    // 🔍 Basic CRUD
    City saveCity(City city);

    Optional<City> getCityById(Long id);

    List<City> getAllCities();

    void deleteCity(Long id);

    // 🔍 Find by name
    Optional<City> getByName(String name);

    Optional<City> getByNameIgnoreCase(String name);

    // ✅ Duplicate check
    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    // 🔍 Active / Inactive
    List<City> getActiveCities();

    List<City> getInactiveCities();

    // 🔍 State-based
    List<City> getCitiesByState(Long stateId);

    List<City> getActiveCitiesByState(Long stateId);

    Optional<City> getByStateAndName(Long stateId, String name);

    // 🔍 Admin-based
    List<City> getCitiesByAdmin(Long adminId);

    List<City> getActiveCitiesByAdmin(Long adminId);

    // 🔍 Search
    List<City> searchByName(String keyword);

    List<City> searchByState(Long stateId, String keyword);

    List<City> searchByAdmin(Long adminId, String keyword);
}