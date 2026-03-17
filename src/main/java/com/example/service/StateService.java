package com.example.service;

import com.example.model.State;

import java.util.List;
import java.util.Optional;

public interface StateService {

    State create(State state);

    List<State> getAll();

    List<State> getAllActive();

    Optional<State> getById(Long id);

    State update(Long id, State state);

    void delete(Long id);

    List<State> search(String keyword);

    List<State> getByCountry(Long countryId);

    List<State> getActiveByCountry(Long countryId);

    List<State> getByAdmin(Long adminId);
}