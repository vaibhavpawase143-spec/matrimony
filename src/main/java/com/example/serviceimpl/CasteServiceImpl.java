package com.example.serviceimpl;

import com.example.model.Caste;
import com.example.repository.CasteRepository;
import com.example.service.CasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasteServiceImpl implements CasteService {

    @Autowired
    private CasteRepository repo;

    // ✅ Get all castes
    @Override
    public List<Caste> getAll() {
        return repo.findAll();
    }

    // ✅ Get active castes
    @Override
    public List<Caste> getActive() {
        return repo.findByIsActiveTrue();   // ✅ fixed
    }

    // ✅ Get by ID
    @Override
    public Caste getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caste not found with id: " + id));
    }

    // ✅ Create caste
    @Override
    public Caste create(Caste caste) {
        caste.setisActive(true);   // default active
        return repo.save(caste);
    }

    // ✅ Update caste
    @Override
    public Caste update(Long id, Caste caste) {
        Caste existing = getById(id);

        existing.setName(caste.getName());
        existing.setReligion(caste.getReligion());   // ✅ use relation object
        existing.setisActive(caste.getisActive());

        return repo.save(existing);
    }

    // ✅ Delete caste
    @Override
    public void delete(Long id) {
        Caste existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Get by religion
    @Override
    public List<Caste> getByReligion(Long religionId) {
        return repo.findByReligionId(religionId);
    }

    // ✅ Get active by religion
    @Override
    public List<Caste> getActiveByReligion(Long religionId) {
        return repo.findByReligionId(religionId);   // ✅ fixed
    }

    // ✅ Get by admin
    @Override
    public List<Caste> getByAdmin(Long adminId) {
        return repo.findByAdminId(adminId);
    }

    // ✅ Search caste
    @Override
    public List<Caste> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }
}