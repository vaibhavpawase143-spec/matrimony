package com.example.serviceimpl;

import com.example.model.Interest;
import com.example.model.User;
import com.example.repository.InterestRepository;
import com.example.repository.UserRepository;
import com.example.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository repo;

    @Autowired
    private UserRepository userRepo;

    // ✅ Create new interest
    @Override
    public Interest create(Long senderId, Long receiverId) {
        User senderUser = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found with id: " + senderId));

        User receiverUser = userRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found with id: " + receiverId));

        Interest interest = new Interest();
        interest.setSender(senderUser);
        interest.setReceiver(receiverUser);
        interest.setisActive("PENDING");
        interest.setCreatedAt(LocalDateTime.now());

        return repo.save(interest);
    }

    // ✅ Get all interests
    @Override
    public List<Interest> getAll() {
        return repo.findAll();
    }

    // ✅ Get by ID
    @Override
    public Interest getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found with id: " + id));
    }

    // ✅ Get by sender
    @Override
    public List<Interest> getBySender(Long senderId) {
        return repo.findBySender_Id(senderId);
    }

    // ✅ Get by receiver
    @Override
    public List<Interest> getByReceiver(Long receiverId) {
        return repo.findByReceiver_Id(receiverId);
    }

    // ✅ Update status
    @Override
    public Interest updateisActive(Long id, String isActive) {
        Interest existing = getById(id);
        existing.setisActive(isActive);
        return repo.save(existing);
    }

    // ✅ Delete interest
    @Override
    public void delete(Long id) {
        Interest existing = getById(id);
        repo.delete(existing);
    }

    // ✅ Send interest (alias)
    @Override
    public Interest sendInterest(Long senderId, Long receiverId) {
        return create(senderId, receiverId);
    }

    // ✅ Get sent interests
    @Override
    public List<Interest> getSentInterests(Long senderId) {
        return repo.findBySender_Id(senderId);
    }

    // ✅ Get received interests
    @Override
    public List<Interest> getReceivedInterests(Long receiverId) {
        return repo.findByReceiver_Id(receiverId);
    }

    // ✅ Get pending received interests
    @Override
    public List<Interest> getReceivedPending(Long receiverId) {
        return repo.findByReceiver_IdAndIsActiveIgnoreCase(receiverId, "PENDING");
    }

    // ✅ Accept interest
    @Override
    public Interest acceptInterest(Long senderId, Long receiverId) {
        Interest interest = repo.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interest.setisActive("ACCEPTED");
        return repo.save(interest);
    }

    // ✅ Reject interest
    @Override
    public Interest rejectInterest(Long senderId, Long receiverId) {
        Interest interest = repo.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interest.setisActive("REJECTED");
        return repo.save(interest);
    }

    // ✅ Cancel interest
    @Override
    public void cancelInterest(Long senderId, Long receiverId) {
        Interest interest = repo.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        repo.delete(interest);
    }
}