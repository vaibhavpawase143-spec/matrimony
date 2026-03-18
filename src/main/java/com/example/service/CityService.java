package com.example.service;

import com.example.model.City;
import java.util.List;

public interface CityService {

    // ✅ Get all cities
    List<City> getAll();

    // ✅ Get active cities
    List<City> getActive();

    // ✅ Get by ID
    City getById(Long id);

    // ✅ Create city
    City create(City city);

    // ✅ Update city
    City update(Long id, City city);

    // ✅ Delete city
    void delete(Long id);

    // ✅ Get cities by state
    List<City> getByState(Long stateId);

    // ✅ Get active cities by state
    List<City> getActiveByState(Long stateId);

    // ✅ Get by admin
    List<City> getByAdmin(Long adminId);

    // ✅ Search city (global)
    List<City> search(String keyword);

    // ✅ Search city within state
    List<City> searchByState(Long stateId, String keyword);
}