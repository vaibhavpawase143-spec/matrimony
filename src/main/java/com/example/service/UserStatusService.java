package com.example.service;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    @Transactional
    public void setOnline(String email) {

        userRepository.updateUserStatus(
                email,
                true,
                null
        );

        System.out.println("🟢 DB ONLINE : " + email);
    }

    @Transactional
    public void setOffline(String email) {

        userRepository.updateUserStatus(
                email,
                false,
                LocalDateTime.now()
        );

        System.out.println("🔴 DB OFFLINE : " + email);
    }

}