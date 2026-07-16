package com.example.service;

import com.example.model.Drinking;

import java.util.List;

public interface DrinkingService {

    // ==========================
    // CRUD
    // ==========================

    Drinking create(Drinking drinking);

    Drinking update(Long id, Drinking drinking);

    void delete(Long id, Long deletedBy);

    void hardDelete(Long id);

    Drinking restore(Long id);

    // ==========================
    // GET
    // ==========================

    Drinking getById(Long id);

    List<Drinking> getAll();

    List<Drinking> getDeleted();

    List<Drinking> getActive();

    List<Drinking> getInactive();

    // ==========================
    // ADMIN
    // ==========================

    List<Drinking> getByAdmin(Long adminId);

    List<Drinking> getActiveByAdmin(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    List<Drinking> search(String keyword);

    List<Drinking> searchByAdmin(Long adminId, String keyword);
}