package com.example.serviceimpl;

import com.example.model.Shortlist;
import com.example.model.User;
import com.example.model.Profile;
import com.example.repository.ShortlistRepository;
import com.example.repository.UserRepository;
import com.example.repository.ProfileRepository;
import com.example.service.ShortlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortlistServiceImpl implements ShortlistService {

    @Autowired
    private ShortlistRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProfileRepository profileRepo;

    // ✅ Add to shortlist
    @Override
    public Shortlist addToShortlist(Long userId, Long profileId) {
        if (repo.existsByUser_IdAndProfile_Id(userId, profileId)) {
            throw new RuntimeException("Already shortlisted");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepo.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Shortlist shortlist = new Shortlist();
        shortlist.setUser(user);
        shortlist.setProfile(profile);
        shortlist.setCreatedAt(LocalDateTime.now());

        return repo.save(shortlist);
    }

    // ✅ Remove from shortlist
    @Override
    public void removeFromShortlist(Long userId, Long profileId) {
        Shortlist shortlist = repo.findByUser_IdAndProfile_Id(userId, profileId)
                .orElseThrow(() -> new RuntimeException("Shortlist not found"));

        repo.delete(shortlist);
    }

    // ✅ Get user shortlist
    @Override
    public List<Shortlist> getUserShortlist(Long userId) {
        return repo.findByUser_Id(userId);
    }

    // ✅ Check if shortlisted
    @Override
    public boolean isShortlisted(Long userId, Long profileId) {
        return repo.existsByUser_IdAndProfile_Id(userId, profileId);
    }
}