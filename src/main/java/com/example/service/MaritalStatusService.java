package com.example.service;

import com.example.model.MaritalStatus;

import java.util.List;
import java.util.Optional;

public interface MaritalStatusService {

    MaritalStatus save(MaritalStatus maritalStatus);

    Optional<MaritalStatus> getById(Long id);

    Optional<MaritalStatus> getByName(String name);

    List<MaritalStatus> getAll();

    List<MaritalStatus> getAllActive();

    List<MaritalStatus> getAllInactive();

    List<MaritalStatus> getByAdmin(Long adminId);

    List<MaritalStatus> getActiveByAdmin(Long adminId);

    List<MaritalStatus> searchByName(String keyword);

    MaritalStatus update(Long id, MaritalStatus updated);

    void delete(Long id); // soft delete
}
