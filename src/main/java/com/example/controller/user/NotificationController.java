package com.example.controller.user;

import com.example.model.Notification;
import com.example.model.NotificationType;
import com.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    // ✅ 1. GET ALL NOTIFICATIONS
    @GetMapping
    public List<Notification> getAll(@RequestParam Long userId) {
        return service.getAll(userId);
    }

    // ✅ 2. UNREAD COUNT
    @GetMapping("/unread")
    public long unread(@RequestParam Long userId) {
        return service.unreadCount(userId);
    }

    // ✅ 3. MARK AS READ
    @PutMapping("/read/{id}")
    public String markRead(@PathVariable Long id) {
        service.markRead(id);
        return "Notification marked as read";
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
}