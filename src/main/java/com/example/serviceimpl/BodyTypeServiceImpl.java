package com.example.serviceimpl;

import com.example.model.Admin;
import com.example.model.BodyType;
import com.example.repository.AdminRepository;
import com.example.repository.BodyTypeRepository;
import com.example.service.BodyTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyTypeServiceImpl implements BodyTypeService {

    private final BodyTypeRepository bodyTypeRepository;
    private final AdminRepository adminRepository;

    // ✅ Create
    @Override
    public BodyType create(BodyType bodyType, Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 🔍 Duplicate check
        boolean exists = bodyTypeRepository.findAll().stream()
                .anyMatch(bt -> bt.getValue().equalsIgnoreCase(bodyType.getValue())
                        && bt.getAdmin().getId().equals(adminId));

        if (exists) {
            throw new RuntimeException("Body type already exists");
        }

        bodyType.setAdmin(admin);

        return bodyTypeRepository.save(bodyType);
    }

    // ✅ Get by ID
    @Override
    public BodyType getById(Long id, Long adminId) {

        BodyType bt = bodyTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Body type not found"));

        if (!bt.getAdmin().getId().equals(adminId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return bt;
    }

    // ✅ Get all
    @Override
    public List<BodyType> getAll(Long adminId) {

        return bodyTypeRepository.findAll()
                .stream()
                .filter(bt -> bt.getAdmin().getId().equals(adminId))
                .toList();
    }

    // ✅ Get active
    @Override
    public List<BodyType> getActive(Long adminId) {

        return bodyTypeRepository.findAll()
                .stream()
                .filter(bt -> bt.getAdmin().getId().equals(adminId)
                        && Boolean.TRUE.equals(bt.getIsActive()))
                .toList();
    }

    // ✅ Get inactive
    @Override
    public List<BodyType> getInactive(Long adminId) {

        return bodyTypeRepository.findAll()
                .stream()
                .filter(bt -> bt.getAdmin().getId().equals(adminId)
                        && Boolean.FALSE.equals(bt.getIsActive()))
                .toList();
    }

    // ✅ Update
    @Override
    public BodyType update(Long id, BodyType updated, Long adminId) {

        BodyType existing = getById(id, adminId);

        // 🔍 Duplicate check
        boolean exists = bodyTypeRepository.findAll().stream()
                .anyMatch(bt -> bt.getValue().equalsIgnoreCase(updated.getValue())
                        && bt.getAdmin().getId().equals(adminId)
                        && !bt.getId().equals(id));

        if (exists) {
            throw new RuntimeException("Body type already exists");
        }

        existing.setValue(updated.getValue());
        existing.setIsActive(updated.getIsActive());

        return bodyTypeRepository.save(existing);
    }

    // ✅ Delete
    @Override
    public void delete(Long id, Long adminId) {

        BodyType bt = getById(id, adminId);
        bodyTypeRepository.delete(bt);
    }
}