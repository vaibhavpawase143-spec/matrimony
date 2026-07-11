package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnlineStatusScheduler {

    private final UserRepository userRepository;
    private final OnlineUserService onlineUserService;

    @Scheduled(fixedRate = 10000)
    public void checkOfflineUsers() {

        LocalDateTime expiry =
                LocalDateTime.now().minusSeconds(20);

        List<User> users =
                userRepository.findExpiredUsers(expiry);

        for (User user : users) {

            onlineUserService.makeOffline(user);

        }

    }

}