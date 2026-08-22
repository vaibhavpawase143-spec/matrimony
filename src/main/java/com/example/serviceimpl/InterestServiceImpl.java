package com.example.serviceimpl;

import com.example.dto.request.InterestRequestDTO;
import com.example.dto.response.InterestResponseDTO;
import com.example.model.Interest;
import com.example.model.Match;
import com.example.model.NotificationType;
import com.example.model.User;
import com.example.repository.InterestRepository;
import com.example.repository.MatchRepository;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.InterestService;
import com.example.service.NotificationService;
import com.example.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            return null;
        }
        return userRepository.findByEmailWithRoles(email).orElse(null);
    }

    private void validateUserAccess(Long targetUserId, String actionDescription) {
        User currentUser = getAuthenticatedUser();
        if (currentUser == null) {
            return;
        }
        boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));
        if (!isAdmin && (currentUser.getId() == null || !currentUser.getId().equals(targetUserId))) {
            throw new AccessDeniedException("Access denied: " + actionDescription);
        }
    }

    // ✅ Send Interest
    @Override
    @CacheEvict(
            value = {
                    "user:interest:sent",
                    "user:interest:received"
            },
            allEntries = true
    )
    @Transactional
    public InterestResponseDTO sendInterest(InterestRequestDTO request) {

        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();

        validateUserAccess(senderId, "You cannot send interests on behalf of another user");

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("You cannot send interest to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
// ================= DAILY LIMIT =================

        // ================= PREMIUM LIMIT =================

        boolean premium = subscriptionService.isCurrentUserPremium();

        if (!premium) {

            long interestCount =
                    interestRepository.countBySender_IdAndIsActiveTrue(senderId);

            if (interestCount >= 5) {

                throw new RuntimeException(
                        "You've reached your free interest limit. Upgrade to Premium to send unlimited interests."
                );

            }

        }

// ================= USER VALIDATION =================

        if (!sender.getIsActive()) {

            throw new RuntimeException("Sender is inactive");

        }

        if (!receiver.getIsActive()) {

            throw new RuntimeException("Cannot send interest to inactive user");

        }

// ================= DUPLICATE CHECK =================

        Interest existing =
                interestRepository
                        .findBySender_IdAndReceiver_Id(
                                senderId,
                                receiverId
                        )
                        .orElse(null);

        if (existing != null) {

            // Already pending
            if ("PENDING".equalsIgnoreCase(existing.getStatus())
                    && Boolean.TRUE.equals(existing.getIsActive())) {

                throw new RuntimeException(
                        "Interest already sent"
                );

            }

            // Already accepted
            if ("ACCEPTED".equalsIgnoreCase(existing.getStatus())) {

                throw new RuntimeException(
                        "You are already connected"
                );

            }

            // Reactivate old interest
            existing.setStatus("PENDING");
            existing.setIsActive(true);

            Interest updated =
                    interestRepository.save(existing);

            notificationService.create(
                    senderId,
                    receiverId,
                    NotificationType.REQUEST
            );

            return mapToDTO(updated);
        }

// ================= SAVE =================

        Interest interest = new Interest();

        interest.setSender(sender);
        interest.setReceiver(receiver);
        interest.setStatus("PENDING");
        interest.setIsActive(true);

        Interest saved =
                interestRepository.save(interest);

// ================= NOTIFICATION =================

        notificationService.create(
                senderId,
                receiverId,
                NotificationType.REQUEST
        );

        return mapToDTO(saved);    }

    // 🔄 Accept / Reject + Match
    @Override
    @CacheEvict(
            value = {
                    "user:interest:sent",
                    "user:interest:received",
                    "user:discover"
            },
            allEntries = true
    )
    @Transactional
    public InterestResponseDTO updateStatus(Long id, String status) {

        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        User currentUser = getAuthenticatedUser();
        if (currentUser != null) {
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            if (!isAdmin && (existing.getReceiver() == null || !currentUser.getId().equals(existing.getReceiver().getId()))) {
                throw new AccessDeniedException("Access denied: Only the receiver can accept or reject this interest");
            }
        }

        if (!"PENDING".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Cannot update status of interest that is already " + existing.getStatus());
        }

        existing.setStatus(status);
        existing.setIsActive(false);

        Interest updated = interestRepository.save(existing);

        // ✅ ACCEPT LOGIC ONLY
        // ✅ ACCEPT
        if (status.equalsIgnoreCase("ACCEPTED")) {

            User sender = existing.getSender();
            User receiver = existing.getReceiver();

            // 1. Sender ला: receiver ने request accept केली
            notificationService.create(
                    receiver.getId(),
                    sender.getId(),
                    NotificationType.ACCEPT
            );

            // 2. दोघांना match notification
            notificationService.create(
                    sender.getId(),
                    receiver.getId(),
                    NotificationType.MATCH
            );

            notificationService.create(
                    receiver.getId(),
                    sender.getId(),
                    NotificationType.MATCH
            );

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
// ❌ REJECT
        else if (status.equalsIgnoreCase("REJECTED")) {
            System.out.println("🔥 REJECT BLOCK ENTERED");
            notificationService.create(
                    existing.getReceiver().getId(),
                    existing.getSender().getId(),
                    NotificationType.REJECT
            );

        }

        return mapToDTO(updated);    }

    // ❌ Delete
    @Override
    @CacheEvict(
            value = {
                    "user:interest:sent",
                    "user:interest:received"
            },
            allEntries = true
    )
    @Transactional
    public void delete(Long id) {

        Interest existing =
                interestRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Interest not found"
                                )
                        );

        User currentUser = getAuthenticatedUser();
        if (currentUser != null) {
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            boolean isParticipant = (existing.getSender() != null && currentUser.getId().equals(existing.getSender().getId())) ||
                                    (existing.getReceiver() != null && currentUser.getId().equals(existing.getReceiver().getId()));
            if (!isAdmin && !isParticipant) {
                throw new AccessDeniedException("Access denied: You do not have permission to delete this interest");
            }
        }

        existing.setStatus(
                "DELETED"
        );

        existing.setIsActive(
                false
        );

        interestRepository.save(
                existing
        );

    }
    // 📥 Get By ID
    @Override
    public InterestResponseDTO getById(Long id) {
        Interest existing = interestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interest not found"));
        User currentUser = getAuthenticatedUser();
        if (currentUser != null) {
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            boolean isParticipant = (existing.getSender() != null && currentUser.getId().equals(existing.getSender().getId())) ||
                                    (existing.getReceiver() != null && currentUser.getId().equals(existing.getReceiver().getId()));
            if (!isAdmin && !isParticipant) {
                throw new AccessDeniedException("Access denied: You do not have permission to view this interest");
            }
        }
        return mapToDTO(existing);
    }

    // 📤 Get By Sender
    @Override
    @Cacheable(value = "user:interest:sent", key = "#senderId")
    public List<InterestResponseDTO> getBySender(Long senderId) {
        validateUserAccess(senderId, "You can only view your own sent interests");
        return interestRepository.findBySender_Id(senderId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterestResponseDTO> getBySenderAndStatus(Long senderId, String status) {
        validateUserAccess(senderId, "You can only view your own sent interests");
        return interestRepository.findBySender_IdAndStatusIgnoreCase(senderId, status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 📥 Get By Receiver
    @Override
    @Cacheable(value = "user:interest:received", key = "#receiverId")
    public List<InterestResponseDTO> getByReceiver(Long receiverId) {
        validateUserAccess(receiverId, "You can only view your own received interests");
        return interestRepository.findByReceiver_Id(receiverId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterestResponseDTO> getByReceiverAndStatus(Long receiverId, String status) {
        validateUserAccess(receiverId, "You can only view your own received interests");
        return interestRepository.findByReceiver_IdAndStatusIgnoreCase(receiverId, status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Sender + Receiver
    @Override
    public InterestResponseDTO getBySenderAndReceiver(Long senderId, Long receiverId) {
        User currentUser = getAuthenticatedUser();
        if (currentUser != null) {
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            boolean isParticipant = currentUser.getId().equals(senderId) || currentUser.getId().equals(receiverId);
            if (!isAdmin && !isParticipant) {
                throw new AccessDeniedException("Access denied: You can only view your own interest interactions");
            }
        }
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

    // =====================================================
// PAGINATION METHODS (1M+ USERS)
// =====================================================

    @Override
    public Page<InterestResponseDTO> getBySender(
            Long senderId,
            Pageable pageable
    ) {
        validateUserAccess(senderId, "You can only view your own sent interests");
        return interestRepository
                .findBySender_Id(senderId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<InterestResponseDTO> getByReceiver(
            Long receiverId,
            Pageable pageable
    ) {
        validateUserAccess(receiverId, "You can only view your own received interests");
        return interestRepository
                .findByReceiver_Id(receiverId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<InterestResponseDTO> getBySenderAndStatus(
            Long senderId,
            String status,
            Pageable pageable
    ) {
        validateUserAccess(senderId, "You can only view your own sent interests");
        return interestRepository
                .findBySender_IdAndStatusIgnoreCase(
                        senderId,
                        status,
                        pageable
                )
                .map(this::mapToDTO);
    }

    @Override
    public Page<InterestResponseDTO> getByReceiverAndStatus(
            Long receiverId,
            String status,
            Pageable pageable
    ) {
        validateUserAccess(receiverId, "You can only view your own received interests");
        return interestRepository
                .findByReceiver_IdAndStatusIgnoreCase(
                        receiverId,
                        status,
                        pageable
                )
                .map(this::mapToDTO);
    }

    @Override
    public Page<InterestResponseDTO> getByStatus(
            String status,
            Pageable pageable
    ) {
        return interestRepository
                .findByStatusIgnoreCase(
                        status,
                        pageable
                )
                .map(this::mapToDTO);
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