package com.example.service;

import com.example.model.Family;

import java.util.List;
import java.util.Optional;

public interface FamilyService {

    Family save(Family family);

    Optional<Family> getById(Long id);

    Optional<Family> getByName(String name);

    List<Family> getAll();

    List<Family> getAllActive();

    List<Family> getAllInactive();

    List<Family> getByAdmin(Long adminId);

    List<Family> getActiveByAdmin(Long adminId);

    List<Family> searchByName(String keyword);

    Family update(Long id, Family updated);

    void delete(Long id);
}