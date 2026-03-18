package com.example.service;

import com.example.model.Caste;
import java.util.List;

public interface CasteService {

    // ✅ Get all castes
    List<Caste> getAll();

    // ✅ Get active castes
    List<Caste> getActive();

    // ✅ Get by ID
    Caste getById(Long id);

    // ✅ Create caste
    Caste create(Caste caste);

    // ✅ Update caste
    Caste update(Long id, Caste caste);

    // ✅ Delete caste
    void delete(Long id);

    // ✅ Get by religion
    List<Caste> getByReligion(Long religionId);

    // ✅ Get active by religion
    List<Caste> getActiveByReligion(Long religionId);

    // ✅ Get by admin
    List<Caste> getByAdmin(Long adminId);

    // ✅ Search caste
    List<Caste> search(String keyword);
}