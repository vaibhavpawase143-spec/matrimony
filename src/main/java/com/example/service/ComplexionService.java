package com.example.service;

import com.example.model.Complexion;
import java.util.List;

public interface ComplexionService {

    // ✅ Get all
    List<Complexion> getAll();

    // ✅ Get active
    List<Complexion> getActive();

    // ✅ Get by ID
    Complexion getById(Long id);

    // ✅ Create
    Complexion create(Complexion complexion);

    // ✅ Update
    Complexion update(Long id, Complexion complexion);

    // ✅ Delete
    void delete(Long id);

    // ✅ Get by admin
    List<Complexion> getByAdmin(Long adminId);

    // ✅ Get active by admin
    List<Complexion> getActiveByAdmin(Long adminId);

    // ✅ Search
    List<Complexion> search(String keyword);
}