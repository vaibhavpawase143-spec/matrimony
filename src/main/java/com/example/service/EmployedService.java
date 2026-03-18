package com.example.service;

import com.example.model.Employed;
import java.util.List;

public interface EmployedService {

    List<Employed> getAll();

    List<Employed> getActive();

    Employed getById(Long id);

    Employed create(Employed emp);

    Employed update(Long id, Employed updated);

    void delete(Long id);

    List<Employed> getByAdmin(Long adminId);

    List<Employed> getActiveByAdmin(Long adminId);

    List<Employed> search(String keyword);
}