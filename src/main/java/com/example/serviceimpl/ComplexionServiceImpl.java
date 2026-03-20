package com.example.serviceimpl;

import com.example.model.Complexion;
import com.example.repository.ComplexionRepository;
import com.example.service.ComplexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplexionServiceImpl implements ComplexionService {

    @Autowired
    private ComplexionRepository repo;

    @Override
    public List<Complexion> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Complexion> getActive() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public Complexion getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Complexion not found with id: " + id));
    }

    @Override
    public Complexion create(Complexion complexion) {
        complexion.setCreatedAt(LocalDateTime.now());
        complexion.setUpdatedAt(LocalDateTime.now());
        complexion.setIsActive(true);
        return repo.save(complexion);
    }

    @Override
    public Complexion update(Long id, Complexion complexion) {
        Complexion existing = getById(id);

        // ✅ Use value instead of name
        existing.setValue(complexion.getValue());
        existing.setIsActive(complexion.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Complexion existing = getById(id);
        repo.delete(existing);
    }

    @Override
    public List<Complexion> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    @Override
    public List<Complexion> getActiveByAdmin(Long adminId) {
        return repo.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Complexion> search(String keyword) {
        return repo.findByValueContainingIgnoreCase(keyword);
    }
}