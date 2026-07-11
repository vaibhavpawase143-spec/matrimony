package com.example.service;

import com.example.model.Family;

import java.util.List;
import java.util.Optional;

public interface FamilyService {

    Family create(Family family);

    Family update(Long id, Family family);

    void delete(Long id);

    Optional<Family> getById(Long id);

    List<Family> getAll();

    Optional<Family> getByName(String name);
    Optional<Family> getByNameIgnoreCase(String name);

    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);

    List<Family> getActive();
    List<Family> getInactive();

    List<Family> getByAdmin(Long adminId);
    List<Family> getActiveByAdmin(Long adminId);

    List<Family> search(String keyword);
    List<Family> searchByAdmin(Long adminId, String keyword);
}