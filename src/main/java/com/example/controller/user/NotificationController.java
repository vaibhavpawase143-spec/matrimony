package com.example.controller.user;

import com.example.model.Notification;
import com.example.model.NotificationType;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.SecurityUtils;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final UserRepository userRepository;

    private Long resolveEffectiveUserId(Long requestedUserId) {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        User currentUser = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().contains("ADMIN"));

        if (requestedUserId != null && !requestedUserId.equals(currentUser.getId()) && !isAdmin) {
            throw new AccessDeniedException("Access denied: You cannot access notifications of another user");
        }
        return (requestedUserId != null) ? requestedUserId : currentUser.getId();
    }

    // ✅ 1. GET ALL NOTIFICATIONS
    @GetMapping
    public List<Notification> getAll(@RequestParam(required = false) Long userId) {
        Long targetId = resolveEffectiveUserId(userId);
        return service.getAll(targetId);
    }

    // ✅ 2. UNREAD COUNT
    @GetMapping("/unread")
    public long unread(@RequestParam(required = false) Long userId) {
        Long targetId = resolveEffectiveUserId(userId);
        return service.unreadCount(targetId);
    }

    // ✅ 3. MARK AS READ
    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
        service.markRead(id);
        return "Notification marked as read";
    }

    @PutMapping("/read-all/{userId}")
    public String markAllRead(@PathVariable Long userId) {
        resolveEffectiveUserId(userId);
        service.markAllRead(userId);
        return "All notifications marked as read";
    }

    // ✅ 4. DELETE (SOFT DELETE)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Notification deleted";
    }

    // 🧪 TEST API (optional)
    @PostMapping("/test")
    public String testCreate() {
        service.create(1L, 2L, NotificationType.REQUEST);
        return "Test notification created";
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable Long id) {
        return service.getById(id);
    }
}