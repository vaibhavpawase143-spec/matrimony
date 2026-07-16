package com.example.serviceimpl;

import com.example.model.Income;
import com.example.repository.IncomeRepository;
import com.example.service.IncomeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeServiceImpl(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    // ✅ Create
    @Override
    public Income create(Income income) {

        if (incomeRepository.existsByRangeIgnoreCase(income.getRange())) {
            throw new RuntimeException("Income already exists: " + income.getRange());
        }

        return incomeRepository.save(income);
    }

    // 🔄 Update
    @Override
    public Income update(Long id, Income income) {

        Income existing = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        incomeRepository.findByRangeIgnoreCase(income.getRange())
                .ifPresent(i -> {
                    if (!i.getId().equals(id)) {
                        throw new RuntimeException("Income already exists: " + income.getRange());
                    }
                });

        // ✏️ Update fields
        existing.setRange(income.getRange());
        existing.setIsActive(income.getIsActive());

        return incomeRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Income existing = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found with id: " + id));

        incomeRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Income> getById(Long id) {
        return incomeRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Income> getAll() {
        return incomeRepository.findAll();
    }

    // 🔍 Find by range
    @Override
    public Optional<Income> getByRange(String range) {
        return incomeRepository.findByRange(range);
    }

    @Override
    public Optional<Income> getByRangeIgnoreCase(String range) {
        return incomeRepository.findByRangeIgnoreCase(range);
    }

    // ✅ Duplicate check
    @Override
    public boolean existsByRange(String range) {
        return incomeRepository.existsByRange(range);
    }

    @Override
    public boolean existsByRangeIgnoreCase(String range) {
        return incomeRepository.existsByRangeIgnoreCase(range);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Income> getActive() {
        return incomeRepository.findByIsActiveTrue();
    }

    @Override
    public List<Income> getInactive() {
        return incomeRepository.findByIsActiveFalse();
    }

    // 🔍 Admin-based
    @Override
    public List<Income> getByAdmin(Long adminId) {
        return incomeRepository.findByAdminId(adminId);
    }

    @Override
    public List<Income> getActiveByAdmin(Long adminId) {
        return incomeRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Income> search(String keyword) {
        return incomeRepository.findByRangeContainingIgnoreCase(keyword);
    }

    @Override
    public List<Income> searchByAdmin(Long adminId, String keyword) {
        return incomeRepository.findByAdminIdAndRangeContainingIgnoreCase(adminId, keyword);
    }
}