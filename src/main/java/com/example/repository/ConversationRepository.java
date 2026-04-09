package com.example.repository;

import com.example.dto.response.ConversationListDTO;
import com.example.model.Conversation;
import com.example.model.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser1AndUser2(User user1, User user2);

    @Query("""
SELECT new com.example.dto.response.ConversationListDTO(
    c.id,

    CASE 
        WHEN c.user1.id = :userId THEN c.user2.id 
        ELSE c.user1.id 
    END,

    CASE 
        WHEN c.user1.id = :userId THEN c.user2.email 
        ELSE c.user1.email 
    END,

    (
        SELECT m.content FROM Message m
        WHERE m.conversation.id = c.id
        AND m.createdAt = (
            SELECT MAX(m2.createdAt) FROM Message m2
            WHERE m2.conversation.id = c.id
        )
    ),

    (
        SELECT MAX(m.createdAt) FROM Message m
        WHERE m.conversation.id = c.id
    ),

    (
        SELECT COUNT(m) FROM Message m
        WHERE m.conversation.id = c.id
        AND m.sender.id != :userId
        AND m.status = com.example.model.MessageStatus.SENT
    ),

    CASE 
        WHEN c.user1.id = :userId THEN c.user2.isOnline 
        ELSE c.user1.isOnline 
    END,

    CASE 
        WHEN c.user1.id = :userId THEN c.user2.lastSeen 
        ELSE c.user1.lastSeen 
    END
)
FROM Conversation c
WHERE c.user1.id = :userId OR c.user2.id = :userId
ORDER BY c.updatedAt DESC
""")
    List<ConversationListDTO> getConversationList(@Param("userId") Long userId);
}
