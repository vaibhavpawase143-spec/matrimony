package com.example.serviceimpl;

import com.example.model.Shortlist;
import com.example.repository.ShortlistRepository;
import com.example.service.ShortlistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShortlistServiceImpl implements ShortlistService {

    private final ShortlistRepository repository;

    public ShortlistServiceImpl(ShortlistRepository repository) {
        this.repository = repository;
    }

    // ✅ Add to shortlist (prevent duplicate)
    @Override
    public Shortlist addToShortlist(Shortlist shortlist) {

        Long userId = shortlist.getUser().getId();
        Long profileId = shortlist.getProfile().getId();

        if (repository.existsByUser_IdAndProfile_Id(userId, profileId)) {
            throw new RuntimeException("Profile already shortlisted!");
        }

        return repository.save(shortlist);
    }

    // 🔍 Get by ID
    @Override
    public Optional<Shortlist> getById(Long id) {
        return repository.findById(id);
    }

    // 🔍 Get specific shortlist
    @Override
    public Optional<Shortlist> getByUserAndProfile(Long userId, Long profileId) {
        return repository.findByUser_IdAndProfile_Id(userId, profileId);
    }

    // 🔍 Get all shortlisted profiles by user
    @Override
    public List<Shortlist> getByUser(Long userId) {
        return repository.findByUser_Id(userId);
    }

    // 🔥 Who shortlisted a profile
    @Override
    public List<Shortlist> getByProfile(Long profileId) {
        return repository.findByProfile_Id(profileId);
    }

    // ❌ Remove from shortlist
    @Override
    public void removeFromShortlist(Long userId, Long profileId) {
        repository.deleteByUser_IdAndProfile_Id(userId, profileId);
    }

    // 🔍 Get all
    @Override
    public List<Shortlist> getAll() {
        return repository.findAll();
    }
}