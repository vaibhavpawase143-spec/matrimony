package com.example.serviceimpl;

import com.example.model.Occupation;
import com.example.repository.OccupationRepository;
import com.example.service.OccupationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OccupationServiceImpl implements OccupationService {

    private final OccupationRepository occupationRepository;

    public OccupationServiceImpl(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    // ✅ Create
    @Override
    public Occupation create(Occupation occupation) {

        Long adminId = occupation.getAdmin().getId();

        if (occupationRepository.existsByNameIgnoreCaseAndAdminId(
                occupation.getName(), adminId)) {
            throw new RuntimeException(
                    "Occupation already exists for this admin: " + occupation.getName()
            );
        }

        return occupationRepository.save(occupation);
    }

    // 🔄 Update
    @Override
    public Occupation update(Long id, Occupation occupation) {

        Occupation existing = occupationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occupation not found with id: " + id));

        Long adminId = existing.getAdmin().getId();

        occupationRepository.findByNameIgnoreCaseAndAdminId(
                occupation.getName(), adminId
        ).ifPresent(o -> {
            if (!o.getId().equals(id)) {
                throw new RuntimeException(
                        "Occupation already exists for this admin: " + occupation.getName()
                );
            }
        });

        existing.setName(occupation.getName());
        existing.setIsActive(occupation.getIsActive());

        return occupationRepository.save(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Occupation existing = occupationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occupation not found with id: " + id));

        occupationRepository.delete(existing);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Occupation> getById(Long id) {
        return occupationRepository.findById(id);
    }

    // 🔍 Get all
    @Override
    public List<Occupation> getAll() {
        return occupationRepository.findAll();
    }

    // 🔍 Admin-based
    @Override
    public List<Occupation> getByAdmin(Long adminId) {
        return occupationRepository.findByAdminId(adminId);
    }

    @Override
    public List<Occupation> getActiveByAdmin(Long adminId) {
        return occupationRepository.findByAdminIdAndIsActiveTrue(adminId);
    }

    @Override
    public List<Occupation> getInactiveByAdmin(Long adminId) {
        return occupationRepository.findByAdminIdAndIsActiveFalse(adminId);
    }

    // 🔍 Find by name
    @Override
    public Optional<Occupation> getByNameAndAdmin(String name, Long adminId) {
        return occupationRepository.findByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // ✅ Exists
    @Override
    public boolean existsByNameAndAdmin(String name, Long adminId) {
        return occupationRepository.existsByNameIgnoreCaseAndAdminId(name, adminId);
    }

    // 🔍 Search
    @Override
    public List<Occupation> searchByAdmin(Long adminId, String keyword) {
        return occupationRepository
                .findByAdminIdAndNameContainingIgnoreCase(adminId, keyword);
    }
}