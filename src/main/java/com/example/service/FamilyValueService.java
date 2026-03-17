package com.example.service;

import com.example.model.FamilyValue;

import java.util.List;
import java.util.Optional;

public interface FamilyValueService {

    FamilyValue save(FamilyValue familyValue);

    Optional<FamilyValue> getById(Long id);

    Optional<FamilyValue> getByName(String name);

    List<FamilyValue> getAll();

    List<FamilyValue> getAllActive();

    List<FamilyValue> getAllInactive();

    List<FamilyValue> getByAdmin(Long adminId);

    List<FamilyValue> getActiveByAdmin(Long adminId);

    List<FamilyValue> searchByName(String keyword);

    FamilyValue update(Long id, FamilyValue updated);

    void delete(Long id); // soft delete
}