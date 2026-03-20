package com.example.serviceimpl;

import com.example.model.State;
import com.example.repository.StateRepository;
import com.example.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    @Autowired
    private StateRepository repo;

    // ✅ Create
    @Override
    public State create(State state) {
        state.setCreatedAt(LocalDateTime.now());
        state.setUpdatedAt(LocalDateTime.now());
        state.setIsActive(true);
        return repo.save(state);
    }

    // ✅ Get all
    @Override
    public List<State> getAll() {
        return repo.findAll();
    }

    // ✅ Get all active
    @Override
    public List<State> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    // ✅ Get by ID
    @Override
    public Optional<State> getById(Long id) {
        return repo.findById(id);
    }

    // ✅ Update
    @Override
    public State update(Long id, State state) {
        State existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found with id: " + id));

        existing.setName(state.getName());
        existing.setCountry(state.getCountry());
        existing.setIsActive(state.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    // ✅ Delete (Soft Delete)
    @Override
    public void delete(Long id) {
        State existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found with id: " + id));

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        repo.save(existing);
    }

    // ✅ Search
    @Override
    public List<State> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ✅ Get by country
    // ✅ Get by country
    @Override
    public List<State> getByCountry(Long countryId) {
        return repo.findByCountry_Id(countryId);
    }

    // ✅ Get active by country
    @Override
    public List<State> getActiveByCountry(Long countryId) {
        return repo.findByCountry_IdAndIsActiveTrue(countryId);
    }

    // ✅ Get by admin
    @Override
    public List<State> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }
}