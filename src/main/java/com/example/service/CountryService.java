package com.example.service;

import com.example.model.Country;

import java.util.List;

public interface CountryService {

    List<Country> getAll();

    List<Country> getActive();

    Country getById(Long id);

    Country create(Country country);

    Country update(Long id, Country country);

    void delete(Long id);

    List<Country> getByAdmin(Long adminId);

    List<Country> getActiveByAdmin(Long adminId);

    List<Country> search(String keyword);
}