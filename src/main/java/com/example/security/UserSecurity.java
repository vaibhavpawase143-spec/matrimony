package com.example.security;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Security helper class for ownership validation.
 * Used in @PreAuthorize expressions.
 */
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    /**
     * Checks if the logged-in user is the owner of the resource.
     *
     * @param userId ID of the user/resource owner
     * @param email  Email of the authenticated user
     * @return true if owner, false otherwise
     */
    public boolean isOwner(Long userId, String email) {
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }
}