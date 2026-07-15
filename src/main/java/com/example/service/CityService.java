package com.example.service;

import com.example.model.City;

import java.util.List;

public interface CityService {

    // ==========================
    // CRUD
    // ==========================

    City create(City city);

    City getById(Long id);

    List<City> getAll();

    List<City> getActive();

    City update(Long id, City city);

    void delete(Long id, Long deletedBy);

    void hardDelete(Long id);

    City restore(Long id);

    List<City> getDeleted();

    // ==========================
    // FIND
    // ==========================

    List<City> getByState(Long stateId);

    List<City> getActiveByState(Long stateId);

    List<City> getByAdmin(Long adminId);

    List<City> getActiveByAdmin(Long adminId);

    // ==========================
    // SEARCH
    // (Keep because frontend uses it)
    // ==========================

    List<City> searchByName(String keyword);

    List<City> searchByState(Long stateId, String keyword);

    List<City> searchByAdmin(Long adminId, String keyword);
}