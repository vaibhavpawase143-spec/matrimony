package com.example.service;

import com.example.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService {

    // ✅ Create / Save
    State save(State state);

    // ✅ Get by ID
    Optional<State> getById(Long id);

    // 🔍 Get all
    List<State> getAll();

    // 🔍 Get by admin
    List<State> getByAdmin(Long adminId);

    // 🔍 Active states by admin
    List<State> getActiveByAdmin(Long adminId);

    // 🔍 Get by country + admin
    List<State> getByCountryAndAdmin(Long countryId, Long adminId);

    // 🔍 Active by country + admin
    List<State> getActiveByCountryAndAdmin(Long countryId, Long adminId);

    // 🔍 Search
    List<State> searchByAdmin(Long adminId, String keyword);

    // 🔍 Find by name (admin-specific)
    Optional<State> getByNameAndAdmin(String name, Long adminId);

    // ✅ Delete
    void delete(Long id);
}