package com.example.service;

import com.example.model.FamilyStatus;
import java.util.List;
import java.util.Optional;

public interface FamilyStatusService {

    FamilyStatus save(FamilyStatus familyStatus);

    Optional<FamilyStatus> getById(Long id);

    Optional<FamilyStatus> getByName(String name);

    List<FamilyStatus> getAll();

    List<FamilyStatus> getAllActive();

    List<FamilyStatus> getAllInactive();

    List<FamilyStatus> getByAdmin(Long adminId);

    List<FamilyStatus> getActiveByAdmin(Long adminId);

    List<FamilyStatus> searchByName(String keyword);

    FamilyStatus update(Long id, FamilyStatus updated);

    void delete(Long id);
}