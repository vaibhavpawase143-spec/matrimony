package com.example.serviceimpl;

import com.example.model.Qualification;
import com.example.repository.QualificationRepository;
import com.example.service.QualificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository repository;

    public QualificationServiceImpl(QualificationRepository repository) {
        this.repository = repository;
    }

    // ✅ Save (admin-wise duplicate check)
    @Override
    public Qualification save(Qualification qualification) {

        String name = qualification.getName();
        Long adminId = qualification.getAdmin().getId();

        Optional<Qualification> existing =
                repository.findByNameIgnoreCaseAndAdminId(name, adminId);

        if (existing.isPresent() &&
                !existing.get().getId().equals(qualification.getId())) {
            throw new RuntimeException("Qualification already exists for this admin!");
        }

        return repository.save(qualification);
    }

    // ✅ Get by ID
    @Override
    public Optional<Qualification> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Qualification> getAll() {
        return repository.findAll();
    }

    // 🔍 Get by admin
    @Override
    public List<Qualification> getByAdmin(Long adminId) {
        return repository.findByAdminId(adminId);
    }

    // 🔍 Active / Inactive
    @Override
    public List<Qualification> getActiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Qualification> getInactiveByAdmin(Long adminId) {
        return repository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Search
    @Override
    public List<Qualification> searchByAdmin(Long adminId, String keyword) {
        return repository.findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }

    // 🔍 Find by name
    @Override
    public Optional<Qualification> getByNameAndAdmin(String name, Long adminId) {
        return repository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Delete
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}