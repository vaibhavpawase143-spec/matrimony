package com.example.serviceimpl;

import com.example.model.PartnerPreference;
import com.example.repository.PartnerPreferenceRepository;
import com.example.service.PartnerPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerPreferenceServiceImpl implements PartnerPreferenceService {

    @Autowired
    private PartnerPreferenceRepository repo;

    // ✅ Create
    @Override
    public PartnerPreference create(PartnerPreference preference) {
        Long userId = preference.getUser().getId();

        if (repo.existsByUser_Id(userId)) {
            throw new RuntimeException("PartnerPreference already exists for userId: " + userId);
        }

        return repo.save(preference);
    }

    // ✅ Update by userId
    @Override
    public PartnerPreference update(Long userId, PartnerPreference preference) {
        PartnerPreference existing = repo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("PartnerPreference not found for userId: " + userId));

        existing.setReligion(preference.getReligion());
        existing.setCaste(preference.getCaste());
        existing.setCity(preference.getCity());
        existing.setMinAge(preference.getMinAge());
        existing.setMaxAge(preference.getMaxAge());
        existing.setMinHeight(preference.getMinHeight());
        existing.setMaxHeight(preference.getMaxHeight());

        return repo.save(existing);
    }

    // ✅ Get by userId
    @Override
    public Optional<PartnerPreference> getByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    // ✅ Delete by userId
    @Override
    public void delete(Long userId) {
        PartnerPreference existing = repo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("PartnerPreference not found for userId: " + userId));

        repo.delete(existing);
    }

    // ✅ Filter by religion
    @Override
    public List<PartnerPreference> getByReligion(Long religionId) {
        return repo.findByReligion_Id(religionId);
    }

    // ✅ Filter by caste
    @Override
    public List<PartnerPreference> getByCaste(Long casteId) {
        return repo.findByCaste_Id(casteId);
    }

    // ✅ Filter by city
    @Override
    public List<PartnerPreference> getByCity(Long cityId) {
        return repo.findByCity_Id(cityId);
    }
}