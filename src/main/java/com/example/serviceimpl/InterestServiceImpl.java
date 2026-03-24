package com.example.serviceimpl;

import com.example.model.Interest;
import com.example.repository.InterestRepository;
import com.example.service.InterestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    public InterestServiceImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    // ✅ Send Interest
    @Override
    public Interest sendInterest(Interest interest) {

        Long senderId = interest.getSender().getId();
        Long receiverId = interest.getReceiver().getId();

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("You cannot send interest to yourself");
        }

        if (interestRepository.findBySender_IdAndReceiver_Id(senderId, receiverId).isPresent()) {
            throw new RuntimeException("Interest already sent");
        }

        interest.setStatus("PENDING");

        return interestRepository.save(interest);
    }

    // 🔄 Accept / Reject
    @Override
    public Interest updateStatus(Long id, String status) {

        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found with id: " + id));

        existing.setStatus(status);
        existing.setIsActive(false);

        return interestRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interestRepository.delete(existing);
    }

    @Override
    public Optional<Interest> getById(Long id) {
        return interestRepository.findById(id);
    }

    @Override
    public List<Interest> getBySender(Long senderId) {
        return interestRepository.findBySender_Id(senderId);
    }

    @Override
    public List<Interest> getBySenderAndStatus(Long senderId, String status) {
        return interestRepository.findBySender_IdAndStatusIgnoreCase(senderId, status);
    }

    @Override
    public List<Interest> getByReceiver(Long receiverId) {
        return interestRepository.findByReceiver_Id(receiverId);
    }

    @Override
    public List<Interest> getByReceiverAndStatus(Long receiverId, String status) {
        return interestRepository.findByReceiver_IdAndStatusIgnoreCase(receiverId, status);
    }

    @Override
    public Optional<Interest> getBySenderAndReceiver(Long senderId, Long receiverId) {
        return interestRepository.findBySender_IdAndReceiver_Id(senderId, receiverId);
    }

    @Override
    public List<Interest> getByStatus(String status) {
        return interestRepository.findByStatusIgnoreCase(status);
    }
}