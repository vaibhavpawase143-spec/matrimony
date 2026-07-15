package com.example.service;

import com.example.model.Country;

import java.util.List;

public interface CountryService {

    // =========================================
    // CRUD
    // =========================================

    Country create(Country country);

    Country getById(Long id);

    List<Country> getAll();

    List<Country> getActive();

    Country update(Long id, Country country);

    void delete(Long id, Long deletedBy);

    // =========================================
    // ADMIN
    // =========================================

    List<Country> getByAdmin(Long adminId);

    List<Country> getActiveByAdmin(Long adminId);

    // =========================================
    // SEARCH
    // =========================================

    List<Country> search(String keyword);

    List<Country> searchByAdmin(Long adminId, String keyword);

    // =========================================
    // SOFT DELETE
    // =========================================

    List<Country> getDeleted();

    Country restore(Long id);

    void hardDelete(Long id);
}