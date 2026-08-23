package com.example.serviceimpl;

import com.example.dto.request.AdminNotificationRequestDTO;
import com.example.dto.response.AdminNotificationResponse;
import com.example.dto.response.NotificationResponse;
import com.example.model.*;
import com.example.repository.AdminNotificationRepository;
import com.example.repository.AdminRepository;
import com.example.repository.NotificationRepository;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.queue.NotificationProducer;
import com.example.service.EmailService;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AdminRepository adminRepository;
    private final AdminNotificationRepository adminNotificationRepository;
    private final EmailService emailService;
    private final NotificationProducer notificationProducer;

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            return null;
        }
        return userRepository.findByEmailWithRoles(email).orElse(null);
    }

    private void validateNotificationAccess(Long targetUserId, String actionDescription) {
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
    // ✅ CREATE + REAL-TIME PUSH
    @Override
    public void create(Long senderId, Long receiverId, NotificationType type) {

        User sender = userRepository.findById(senderId)
                .orElse(null);

        String senderName =
                sender != null
                        ? sender.getFullName().trim()
                        : "Someone";

        if (type == NotificationType.MESSAGE && senderId != null) {
            boolean existsUnreadMessage = repo.existsBySenderIdAndReceiverIdAndTypeAndReadFalseAndDeletedFalse(
                    senderId, receiverId, NotificationType.MESSAGE
            );
            if (existsUnreadMessage) {
                return;
            }
        }

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
    // 🔥 Dynamic Message Generator
    private String generateMessage(
            String senderName,
            NotificationType type
    ) {

        return switch (type) {

            // ================= USER NOTIFICATIONS =================

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

            // ================= ADMIN NOTIFICATIONS =================

            case REPORT ->
                    "🚨 New profile report received";

            case SUPPORT ->
                    "🎫 New support ticket received";

            case NEW_USER ->
                    "👤 A new user has registered";

            case ADMIN ->
                    "🔐 Administrator activity detected";

            case ANNOUNCEMENT ->
                    "📢 " + senderName + " posted an announcement";

            case SYSTEM ->
                    "⚙️ System notification";

            case MAINTENANCE ->
                    "🛠️ Scheduled maintenance notification";

            case SUBSCRIPTION ->
                    "💳 Premium subscription purchased";

            case WARNING ->
                    "⚠️ Warning notification";
        };
    }
    // 📥 GET ALL
    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAll(Long userId) {
        if (userId != null) {
            validateNotificationAccess(userId, "You can only view your own notifications");
        }
        return repo.findByReceiverIdAndDeletedFalseOrderByCreatedAtDesc(userId);
    }

    // 🔔 UNREAD COUNT
    @Override
    @Transactional(readOnly = true)
    public long unreadCount(Long userId) {
        if (userId != null) {
            validateNotificationAccess(userId, "You can only view your own unread count");
        }
        return repo.countByReceiverIdAndReadFalseAndDeletedFalse(userId);
    }

    // ✅ MARK READ
    @Override
    public void markRead(Long id) {
        Notification n = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (n.getReceiverId() != null) {
            validateNotificationAccess(n.getReceiverId(), "You cannot modify another user's notification");
        }

        n.setRead(true);
        repo.save(n);
    }

    @Override
    public void markAllRead(Long userId) {
        if (userId != null) {
            validateNotificationAccess(userId, "You cannot modify another user's notifications");
        }

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

        if (n.getReceiverId() != null) {
            validateNotificationAccess(n.getReceiverId(), "You cannot delete another user's notification");
        }

        n.setDeleted(true);
        repo.save(n);
    }

    // ==========================
// MATCH NOTIFICATIONS
// ==========================

    @Override
    public void createMatchRecommendation(
            Long receiverId,
            Long matchedUserId,
            Integer matchPercentage
    ) {

        if (repo.existsByReceiverIdAndMatchedUserIdAndTypeAndDeletedFalse(
                receiverId,
                matchedUserId,
                NotificationType.MATCH
        )) {
            return;
        }

        User matchedUser = userRepository.findById(matchedUserId)
                .orElseThrow(() -> new RuntimeException("Matched user not found"));

        Notification notification = new Notification();

        notification.setSenderId(matchedUserId);
        notification.setReceiverId(receiverId);
        notification.setMatchedUserId(matchedUserId);
        notification.setMatchPercentage(matchPercentage);
        notification.setType(NotificationType.MATCH);

        notification.setMessage(
                "🎯 " + matchedUser.getFullName()
                        + " matches "
                        + matchPercentage
                        + "% with your partner preferences."
        );

        notification.setRead(false);
        notification.setDeleted(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = repo.save(notification);

        NotificationResponse response = new NotificationResponse();
        response.setId(saved.getId());
        response.setSenderId(matchedUserId);
        response.setReceiverId(receiverId);
        response.setSenderName(matchedUser.getFullName());
        response.setMessage(saved.getMessage());
        response.setType(saved.getType().name());
        response.setRead(saved.isRead());
        response.setCreatedAt(saved.getCreatedAt());

        // हे fields DTO मध्ये असतील तरच ठेवा
        response.setMatchedUserId(saved.getMatchedUserId());
        response.setMatchPercentage(saved.getMatchPercentage());

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiverId,
                response
        );
    }
    // ==========================
    // ADMIN NOTIFICATIONS (ASYNC QUEUE)
    // ==========================

    @Override
    public void sendNotification(AdminNotificationRequestDTO request) {

        if (request.getReceiverIds() == null || request.getReceiverIds().isEmpty()) {
            broadcastNotification(request);
            return;
        }

        notificationProducer.enqueueBulkNotifications(
                request.getReceiverIds(),
                request.getTitle(),
                request.getMessage(),
                request.getType()
        );
    }

    @Override
    public void broadcastNotification(AdminNotificationRequestDTO request) {

        notificationProducer.enqueueBulkNotifications(
                null,
                request.getTitle(),
                request.getMessage(),
                request.getType()
        );
    }
    @Override
    public void createAdminNotification(
            String title,
            String message,
            NotificationType type
    ) {

        List<Admin> admins = adminRepository.findAllActiveAdmins();

        for (Admin admin : admins) {

            AdminNotification notification = AdminNotification.builder()
                    .admin(admin)
                    .title(title)
                    .message(message)
                    .type(type)
                    .read(false)
                    .deleted(false)
                    .build();

            AdminNotification saved =
                    adminNotificationRepository.save(notification);

            AdminNotificationResponse response =
                    AdminNotificationResponse.builder()
                            .id(saved.getId())
                            .adminId(admin.getId())
                            .title(saved.getTitle())
                            .message(saved.getMessage())
                            .type(saved.getType())
                            .read(saved.getRead())
                            .deleted(saved.getDeleted())
                            .createdAt(saved.getCreatedAt())
                            .build();

            messagingTemplate.convertAndSend(
                    "/topic/admin-notifications/" + admin.getId(),
                    response
            );
        }
    }
    @Override
    public void createSubscriptionReminder(
            Long receiverId,
            Long subscriptionId,
            String title,
            String message
    ) {

        Notification notification = new Notification();

        notification.setSenderId(null);
        notification.setReceiverId(receiverId);
        notification.setSubscriptionId(subscriptionId);

        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.SUBSCRIPTION);

        notification.setRead(false);
        notification.setDeleted(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = repo.save(notification);

        NotificationResponse response = new NotificationResponse();

        response.setId(saved.getId());
        response.setSenderId(null);
        response.setReceiverId(receiverId);
        response.setSenderName("System");
        response.setMessage(saved.getMessage());
        response.setType(saved.getType().name());
        response.setRead(saved.isRead());
        response.setCreatedAt(saved.getCreatedAt());

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiverId,
                response
        );
        User user = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int daysRemaining;

        switch (title) {

            case "PREMIUM_REMINDER_7":
                daysRemaining = 7;
                break;

            case "PREMIUM_REMINDER_5":
                daysRemaining = 5;
                break;

            case "PREMIUM_REMINDER_3":
                daysRemaining = 3;
                break;

            case "PREMIUM_REMINDER_2":
                daysRemaining = 2;
                break;

            case "PREMIUM_REMINDER_1":
                daysRemaining = 1;
                break;

            default:
                return;
        }

        emailService.sendPremiumReminderEmail(
                user.getEmail(),
                user.getFirstName(),
                daysRemaining
        );
    }
    @Override
    public void createSubscriptionExpiredNotification(
            Long receiverId,
            Long subscriptionId
    ) {

        Notification notification = new Notification();

        notification.setSenderId(null);
        notification.setReceiverId(receiverId);
        notification.setSubscriptionId(subscriptionId);

        notification.setTitle("PREMIUM_EXPIRED");
        notification.setMessage(
                "Your Premium Membership has expired. Renew now to continue enjoying premium benefits."
        );
        notification.setType(NotificationType.SUBSCRIPTION);

        notification.setRead(false);
        notification.setDeleted(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = repo.save(notification);

        NotificationResponse response = new NotificationResponse();

        response.setId(saved.getId());
        response.setSenderId(null);
        response.setReceiverId(receiverId);
        response.setSenderName("System");
        response.setMessage(saved.getMessage());
        response.setType(saved.getType().name());
        response.setRead(saved.isRead());
        response.setCreatedAt(saved.getCreatedAt());

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiverId,
                response
        );
    }
    @Override
    public Page<NotificationResponse> getNotificationHistory(Pageable pageable) {
        return Page.empty(pageable);
    }
    @Override
    public Notification getById(Long id) {
        Notification n = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        User currentUser = getAuthenticatedUser();
        if (currentUser != null) {
            boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName().contains("ADMIN"));
            boolean isParticipant = (n.getReceiverId() != null && currentUser.getId().equals(n.getReceiverId())) ||
                                    (n.getSenderId() != null && currentUser.getId().equals(n.getSenderId()));
            if (!isAdmin && !isParticipant) {
                throw new AccessDeniedException("Access denied: You cannot view another user's notification");
            }
        }

        return n;
    }
}