package com.example.service;

import com.example.model.Country;
import java.util.List;

public interface CountryService {

    // ✅ Get all countries
    List<Country> getAll();

    // ✅ Get active countries
    List<Country> getActive();

    // ✅ Get by ID
    Country getById(Long id);

    // ✅ Create country
    Country create(Country country);

    // ✅ Update country
    Country update(Long id, Country country);

    // ✅ Delete country
    void delete(Long id);

    // ✅ Get by admin
    List<Country> getByAdmin(Long adminId);

    // ✅ Get active by admin
    List<Country> getActiveByAdmin(Long adminId);

    // ✅ Search country
    List<Country> search(String keyword);
}