package com.example.serviceimpl;

import com.example.model.State;
import com.example.repository.StateRepository;
import com.example.service.StateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository repository;

    public StateServiceImpl(StateRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public State save(State state) {

        String name = state.getName();
        Long adminId = state.getAdmin().getId();

        Optional<State> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(state.getId())) {
            throw new RuntimeException("State already exists for this admin!");
        }

        return repository.save(state);
    }

    // ✅ Get by ID
    @Override
    public Optional<State> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<State> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<State> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active states
    @Override
    public List<State> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Country + admin
    @Override
    public List<State> getByCountryAndAdmin(Long countryId, Long adminId) {
        return repository.findByCountry_IdAndAdminId(countryId, adminId);
    }

    // 🔍 Active by country + admin
    @Override
    public List<State> getActiveByCountryAndAdmin(Long countryId, Long adminId) {
        return repository.findByCountry_IdAndAdminIdAndIsActiveTrue(countryId, adminId);
    }

    // 🔍 Search
    @Override
    public List<State> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<State> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}