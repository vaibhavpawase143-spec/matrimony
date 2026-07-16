package com.example.serviceimpl;

import com.example.model.Notification;
import com.example.model.NotificationType;
import com.example.repository.NotificationRepository;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;

    // 🔥 WebSocket Sender (IMPORTANT)
    private final SimpMessagingTemplate messagingTemplate;

    // ✅ CREATE + REAL-TIME PUSH
    @Override
    public void create(Long senderId, Long receiverId, NotificationType type) {

        String message = generateMessage(senderId, type);

        Notification n = new Notification();
        n.setSenderId(senderId);
        n.setReceiverId(receiverId);
        n.setMessage(message);
        n.setType(type);
        n.setRead(false);
        n.setDeleted(false);
        n.setCreatedAt(LocalDateTime.now());

        Notification saved = repo.save(n);

        // 🔥🔥 REAL-TIME (PRIVATE USER NOTIFICATION)
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),              // user
                "/queue/notifications",             // destination
                saved                              // payload
        );
    }

    // 🔥 Dynamic Message Generator
    private String generateMessage(Long senderId, NotificationType type) {

        return switch (type) {
            case REQUEST -> "User " + senderId + " sent you a request";
            case VIEW -> "User " + senderId + " viewed your profile";
            case MESSAGE -> "User " + senderId + " sent you a message";
            case ACCEPT -> "User " + senderId + " accepted your request";
            case REJECT -> "User " + senderId + " rejected your request";
            case MATCH -> "🎉 You have a new match with User " + senderId;
        };
    }

    // 📥 GET ALL
    @Override
    public List<Notification> getAll(Long userId) {
        return repo.findByReceiverIdAndDeletedFalseOrderByCreatedAtDesc(userId);
    }

    // 🔔 UNREAD COUNT
    @Override
    public long unreadCount(Long userId) {
        return repo.countByReceiverIdAndReadFalseAndDeletedFalse(userId);
    }

    // ✅ MARK READ
    @Override
    public void markRead(Long id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        repo.save(n);
    }

    // ❌ SOFT DELETE
    @Override
    public void delete(Long id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setDeleted(true);
        repo.save(n);
    }
}