package com.example.serviceimpl;

import com.example.model.Income;
import com.example.repository.IncomeRepository;
import com.example.service.IncomeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository repo;

    public IncomeServiceImpl(IncomeRepository repo) {
        this.repo = repo;
    }

    @Override
    public Income save(Income income) {
        income.setisActive(true); // default active
        return repo.save(income);
    }

    @Override
    public Optional<Income> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Income> getByRange(String range) {
        return repo.findByRangeIgnoreCase(range);
    }

    @Override
    public List<Income> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Income> getAllActive() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public List<Income> getAllInactive() {
        return repo.findByIsActiveFalse();
    }

    @Override
    public List<Income> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }
    @Override
    public List<Income> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Income> searchByRange(String keyword) {
        return repo.findByRangeContainingIgnoreCase(keyword);
    }

    @Override
    public Income update(Long id, Income updated) {
        Income existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        existing.setRange(updated.getRange());
        existing.setisActive(updated.getisActive());

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Income existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        existing.setisActive(false); // soft delete
        repo.save(existing);
    }
}