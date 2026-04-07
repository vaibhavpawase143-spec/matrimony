package com.example.repository;

import com.example.model.Message;
import com.example.model.Conversation;
import com.example.model.MessageStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // ================= BASIC =================

    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    // ================= PAGINATION =================

    Page<Message> findByConversation(Conversation conversation, Pageable pageable);

    // ================= LAST MESSAGE =================

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId
        ORDER BY m.createdAt DESC
    """)
    List<Message> findTopMessageByConversation(
            @Param("conversationId") Long conversationId,
            Pageable pageable
    );

    // ================= DELIVERED =================

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId
        AND m.sender.id != :userId
        AND m.status = 'SENT'
    """)
    List<Message> findUndeliveredMessages(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );

    // ================= UNREAD (OPTIONAL FETCH) =================

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId
        AND m.sender.id != :userId
        AND m.status <> 'SEEN'
    """)
    List<Message> findUnseenMessages(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );

    // ================= UNREAD COUNT =================

    @Query("""
        SELECT COUNT(m) FROM Message m
        WHERE m.conversation.id = :conversationId
        AND m.sender.id != :userId
        AND m.status <> 'SEEN'
    """)
    long countUnreadMessages(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId
    );

    // ================= STRICT UNREAD (ONLY SENT) =================

    @Query("""
        SELECT COUNT(m) FROM Message m
        WHERE m.conversation.id = :conversationId
        AND m.sender.id != :userId
        AND m.status = :status
    """)
    long countByConversationIdAndSenderIdNotAndStatus(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId,
            @Param("status") MessageStatus status
    );

    // ================= 🔥 FINAL SEEN UPDATE (MOST IMPORTANT) =================

    @Modifying
    @Query("""
        UPDATE Message m
        SET m.status = :status,
            m.seenAt = :seenAt
        WHERE m.conversation.id = :conversationId
        AND m.sender.id <> :userId
        AND m.status <> 'SEEN'
    """)
    void markAsSeen(
            @Param("conversationId") Long conversationId,
            @Param("userId") Long userId,
            @Param("status") MessageStatus status,
            @Param("seenAt") LocalDateTime seenAt
    );
    @Modifying
    @Query("""
    UPDATE Message m
    SET m.isDeleted = true
    WHERE m.id = :messageId
""")
    void softDeleteMessage(@Param("messageId") Long messageId);
}