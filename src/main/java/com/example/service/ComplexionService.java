package com.example.service;

import com.example.model.Complexion;

import java.util.List;

public interface ComplexionService {

    // =========================================
    // CRUD
    // =========================================

    Complexion create(Complexion complexion);

    Complexion getById(Long id);

    List<Complexion> getAll();

    List<Complexion> getActive();

    Complexion update(Long id, Complexion complexion);

    void delete(Long id, Long deletedBy);

    // =========================================
    // ADMIN
    // =========================================

    List<Complexion> getByAdmin(Long adminId);

    List<Complexion> getActiveByAdmin(Long adminId);

    // =========================================
    // SEARCH
    // =========================================

    List<Complexion> search(String keyword);

    // =========================================
    // SOFT DELETE
    // =========================================

    List<Complexion> getDeleted();

    Complexion restore(Long id);

    void hardDelete(Long id);
}