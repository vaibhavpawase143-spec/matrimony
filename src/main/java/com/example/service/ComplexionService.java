package com.example.service;

import com.example.model.Complexion;
import java.util.List;

public interface ComplexionService {

    List<Complexion> getAll();

    List<Complexion> getActive();

    Complexion getById(Long id);

    Complexion create(Complexion complexion);

    Complexion update(Long id, Complexion complexion);

    void delete(Long id);

    List<Complexion> getByAdmin(Long adminId);

    List<Complexion> getActiveByAdmin(Long adminId);

    List<Complexion> search(String keyword);
}