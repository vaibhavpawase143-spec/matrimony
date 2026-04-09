package com.example.serviceimpl;

import com.example.dto.request.InterestRequestDTO;
import com.example.dto.response.InterestResponseDTO;
import com.example.model.Interest;
import com.example.model.Match;
import com.example.model.User;
import com.example.repository.InterestRepository;
import com.example.repository.MatchRepository;
import com.example.repository.UserRepository;
import com.example.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    // ✅ Send Interest
    @Override
    @Transactional
    public InterestResponseDTO sendInterest(InterestRequestDTO request) {

        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("You cannot send interest to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (!sender.getIsActive()) {
            throw new RuntimeException("Sender is inactive");
        }

        if (!receiver.getIsActive()) {
            throw new RuntimeException("Cannot send interest to inactive user");
        }

        Interest existing = interestRepository
                .findBySender_IdAndReceiver_Id(senderId, receiverId)
                .orElse(null);

        if (existing != null) {

            if (existing.getStatus().equalsIgnoreCase("PENDING")) {
                throw new RuntimeException("Interest already sent and pending");
            }

            if (existing.getStatus().equalsIgnoreCase("ACCEPTED")) {
                throw new RuntimeException("You are already connected");
            }

            if (existing.getStatus().equalsIgnoreCase("REJECTED")) {
                existing.setStatus("PENDING");
                existing.setIsActive(true);
                return mapToDTO(existing);
            }
        }

        Interest interest = new Interest();
        interest.setSender(sender);
        interest.setReceiver(receiver);
        interest.setStatus("PENDING");
        interest.setIsActive(true);

        return mapToDTO(interestRepository.save(interest));
    }

    // 🔄 Accept / Reject + Match
    @Override
    @Transactional
    public InterestResponseDTO updateStatus(Long id, String status) {

        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        existing.setStatus(status);
        existing.setIsActive(false);

        if (status.equalsIgnoreCase("ACCEPTED")) {

            User sender = existing.getSender();
            User receiver = existing.getReceiver();

            Long u1 = Math.min(sender.getId(), receiver.getId());
            Long u2 = Math.max(sender.getId(), receiver.getId());

            boolean exists = matchRepository
                    .findByUser1_IdAndUser2_Id(u1, u2)
                    .isPresent();

            if (!exists) {
                Match match = new Match();
                match.setUsers(sender, receiver);
                matchRepository.save(match);
            }
        }

        return mapToDTO(existing);
    }

    // ❌ Delete
    @Override
    public void delete(Long id) {
        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        interestRepository.delete(existing);
    }

    // 📥 Get By ID
    @Override
    public InterestResponseDTO getById(Long id) {
        return interestRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Interest not found"));
    }

    // 📤 Get By Sender
    @Override
    public List<InterestResponseDTO> getBySender(Long senderId) {
        return interestRepository.findBySender_Id(senderId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterestResponseDTO> getBySenderAndStatus(Long senderId, String status) {
        return interestRepository.findBySender_IdAndStatusIgnoreCase(senderId, status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 📥 Get By Receiver
    @Override
    public List<InterestResponseDTO> getByReceiver(Long receiverId) {
        return interestRepository.findByReceiver_Id(receiverId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterestResponseDTO> getByReceiverAndStatus(Long receiverId, String status) {
        return interestRepository.findByReceiver_IdAndStatusIgnoreCase(receiverId, status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Sender + Receiver
    @Override
    public InterestResponseDTO getBySenderAndReceiver(Long senderId, Long receiverId) {
        return interestRepository.findBySender_IdAndReceiver_Id(senderId, receiverId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Interest not found"));
    }

    // 📊 Status
    @Override
    public List<InterestResponseDTO> getByStatus(String status) {
        return interestRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 🔁 SAFE MAPPER
    private InterestResponseDTO mapToDTO(Interest interest) {

        InterestResponseDTO dto = new InterestResponseDTO();

        dto.setId(interest.getId());
        dto.setSenderId(interest.getSender() != null ? interest.getSender().getId() : null);
        dto.setReceiverId(interest.getReceiver() != null ? interest.getReceiver().getId() : null);
        dto.setStatus(interest.getStatus());
        dto.setIsActive(interest.getIsActive());
        dto.setCreatedAt(interest.getCreatedAt());
        dto.setUpdatedAt(interest.getUpdatedAt());

        return dto;
    }
}