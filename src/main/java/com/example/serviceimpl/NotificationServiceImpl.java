package com.example.serviceimpl;

import com.example.dto.response.NotificationResponse;
import com.example.model.Notification;
import com.example.model.NotificationType;
import com.example.model.User;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // ✅ CREATE + REAL-TIME PUSH
    @Override
    public void create(Long senderId, Long receiverId, NotificationType type) {

        User sender = userRepository.findById(senderId)
                .orElse(null);

        String senderName =
                sender != null
                        ? sender.getFullName().trim()
                        : "Someone";

        String message = generateMessage(senderName, type);

        Notification n = new Notification();
        n.setSenderId(senderId);
        n.setReceiverId(receiverId);
        n.setMessage(message);
        n.setType(type);
        n.setRead(false);
        n.setDeleted(false);
        n.setCreatedAt(LocalDateTime.now());

        Notification saved = repo.save(n);

        System.out.println("========== PUSHING NOTIFICATION ==========");
        System.out.println("Receiver ID: " + receiverId);
        System.out.println("Destination: /topic/notifications/" + receiverId);
        System.out.println("Notification Message: " + saved.getMessage());
        System.out.println("==========================================");

        NotificationResponse response = new NotificationResponse();

        response.setId(saved.getId());

        response.setSenderId(senderId);

        response.setReceiverId(receiverId);

        response.setSenderName(senderName);

        response.setMessage(saved.getMessage());

        response.setType(saved.getType().name());

        response.setRead(saved.isRead());

        response.setCreatedAt(saved.getCreatedAt());

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiverId,
                response
        );

        System.out.println("WEBSOCKET MESSAGE SENT");
    }

    // 🔥 Dynamic Message Generator
    private String generateMessage(
            String senderName,
            NotificationType type
    ) {

        return switch (type) {

            case REQUEST ->
                    "💌 " + senderName + " sent you an interest request";

            case ACCEPT ->
                    "✅ " + senderName + " accepted your interest request";

            case REJECT ->
                    "❌ " + senderName + " declined your interest request";

            case MATCH ->
                    "🎉 You matched with " + senderName;

            case VIEW ->
                    "👀 " + senderName + " viewed your profile";

            case SHORTLIST ->
                    "⭐ " + senderName + " added you to their shortlist";

            case MESSAGE ->
                    "💬 New message from " + senderName;

            case LIKE ->
                    "❤️ " + senderName + " liked your profile";
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

    @Override
    public void markAllRead(Long userId) {

        List<Notification> notifications =
                repo.findByReceiverIdAndReadFalseAndDeletedFalse(userId);

        notifications.forEach(n -> n.setRead(true));

        repo.saveAll(notifications);
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