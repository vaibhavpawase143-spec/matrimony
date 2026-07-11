package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OnlineUserService(
            UserRepository userRepository,
            @Lazy SimpMessagingTemplate messagingTemplate
    ) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    // ================= USER ONLINE =================

    public void userOnline(String email) {

        onlineUsers.add(email);

        System.out.println("🟢 USER ONLINE (MEMORY): " + email);
    }

    // ================= USER OFFLINE =================

    public void userOffline(String email) {

        onlineUsers.remove(email);

        System.out.println("🔴 USER OFFLINE (MEMORY): " + email);
    }

    // ================= CHECK ONLINE =================

    public boolean isOnline(String email) {

        return onlineUsers.contains(email);
    }

    // ================= HEARTBEAT =================

    public void heartbeat(String email) {

        userRepository.updateHeartbeat(
                email,
                LocalDateTime.now()
        );
    }

    // ================= MAKE USER OFFLINE =================

    public void makeOffline(User user) {

        LocalDateTime now = LocalDateTime.now();

        userRepository.updateUserStatus(
                user.getEmail(),
                false,
                now
        );

        onlineUsers.remove(user.getEmail());

        // 🔥 Broadcast status to all connected users
        messagingTemplate.convertAndSend(
                "/topic/status",
                Map.of(
                        "email", user.getEmail(),
                        "online", false,
                        "lastSeen", now.toString()
                )
        );

        System.out.println("🔴 USER OFFLINE (DATABASE): " + user.getEmail());
    }

    // ================= ALL ONLINE USERS =================

    public Set<String> getAllOnlineUsers() {

        return Set.copyOf(onlineUsers);
    }

    // ================= CLEAR =================

    public void clearAll() {

        onlineUsers.clear();

        System.out.println("🧹 ONLINE USER CACHE CLEARED");
    }
}