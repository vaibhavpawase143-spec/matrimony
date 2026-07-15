package com.example.service;

import com.example.model.Caste;

import java.util.List;

public interface CasteService {

    // CRUD
    Caste create(Caste caste, Long adminId);

    Caste getById(Long id, Long adminId);

    List<Caste> getAll(Long adminId);

    List<Caste> getActive(Long adminId);

    Caste update(Long id, Caste caste, Long adminId);

    void delete(Long id, Long deletedBy);

    // Soft Delete
    void hardDelete(Long id);

    Caste restore(Long id);

    List<Caste> getDeleted(Long adminId);

    // Religion
    List<Caste> getByReligion(Long religionId, Long adminId);

    List<Caste> getActiveByReligion(Long religionId, Long adminId);

    // Search
    List<Caste> search(String keyword, Long adminId);
}