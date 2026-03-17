package com.example.service;

import com.example.model.FamilyType;

import java.util.List;
import java.util.Optional;

public interface FamilyTypeService {

    FamilyType save(FamilyType familyType);

    Optional<FamilyType> getById(Long id);

    Optional<FamilyType> getByName(String name);

    List<FamilyType> getAll();

    List<FamilyType> getAllActive();

    List<FamilyType> getAllInactive();

    List<FamilyType> getByAdmin(Long adminId);

    List<FamilyType> getActiveByAdmin(Long adminId);

    List<FamilyType> searchByName(String keyword);

    FamilyType update(Long id, FamilyType updated);

    void delete(Long id); // soft delete
}