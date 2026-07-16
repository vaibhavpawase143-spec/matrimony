package com.example.serviceimpl;

import com.example.model.Complexion;
import com.example.repository.ComplexionRepository;
import com.example.service.ComplexionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplexionServiceImpl implements ComplexionService {

    private final ComplexionRepository complexionRepository;

    public ComplexionServiceImpl(ComplexionRepository complexionRepository) {
        this.complexionRepository = complexionRepository;
    }

    // 🔍 Get all
    @Override
    public List<Complexion> getAll() {
        return complexionRepository.findAll();
    }

    // 🔍 Get active
    @Override
    public List<Complexion> getActive() {
        return complexionRepository.findByIsActiveTrue();
    }

    // 🔍 Get by ID
    @Override
    public Complexion getById(Long id) {
        return complexionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complexion not found with id: " + id));
    }

    // ✅ Create
    @Override
    public Complexion create(Complexion complexion) {

        // 🔥 Duplicate check (important)
        if (complexionRepository.existsByValueIgnoreCase(complexion.getValue())) {
            throw new RuntimeException("Complexion already exists: " + complexion.getValue());
        }

        return complexionRepository.save(complexion);
    }

    // 🔄 Update
    @Override
    public Complexion update(Long id, Complexion complexion) {

        Complexion existing = getById(id);

        // 🔥 Duplicate check (exclude same record)
        complexionRepository.findByValueIgnoreCase(complexion.getValue())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new RuntimeException("Complexion already exists: " + complexion.getValue());
                    }
                });

        // ✏️ Update fields
        existing.setValue(complexion.getValue());
        existing.setIsActive(complexion.getIsActive());

        return complexionRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Complexion existing = getById(id);
        complexionRepository.delete(existing);
    }

    // 🔍 Admin-based
    @Override
    public List<Complexion> getByAdmin(Long adminId) {
        return complexionRepository.findByAdminId(adminId);
    }

    @Override
    public List<Complexion> getActiveByAdmin(Long adminId) {
        return complexionRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    // 🔍 Search
    @Override
    public List<Complexion> search(String keyword) {
        return complexionRepository.findByValueContainingIgnoreCase(keyword);
    }
}