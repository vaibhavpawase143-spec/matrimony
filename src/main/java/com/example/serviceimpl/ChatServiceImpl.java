package com.example.serviceimpl;

import com.example.dto.response.ConversationListDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepo;
    private final MatchRepository matchRepo;
    private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;
    private final UserBlockRepository blockRepo;

    // ================= SEND MESSAGE =================
    @Override
    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {

        User sender = getUser(senderId);
        User receiver = getUser(receiverId);

        // 🔥 BLOCK CHECK
        if (isBlocked(senderId, receiverId)) {
            throw new RuntimeException("Message not allowed. One of the users has blocked the other.");
        }

        Conversation conversation = getOrCreateConversation(sender, receiver);

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepo.save(conversation);

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content);
        message.setStatus(MessageStatus.SENT);

        return messageRepo.save(message);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content) {

        User sender = userRepo.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        return sendMessage(sender.getId(), receiverId, content);
    }

    // ================= MEDIA =================
    @Override
    @Transactional
    public Message sendMediaMessage(String senderEmail, Long receiverId, String mediaUrl, String mediaType) {

        User sender = userRepo.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User receiver = getUser(receiverId);

        // 🔥 BLOCK CHECK
        if (isBlocked(sender.getId(), receiver.getId())) {
            throw new RuntimeException("Message not allowed. One of the users has blocked the other.");
        }

        Conversation conversation = getOrCreateConversation(sender, receiver);

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setMediaUrl(mediaUrl);
        message.setMediaType(mediaType);
        message.setMessageType("MEDIA");

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepo.save(conversation);

        return messageRepo.save(message);
    }

    // ================= GET =================
    @Override
    public List<Message> getMessages(Long userId, Long otherUserId) {

        Conversation conversation = getConversation(getUser(userId), getUser(otherUserId));

        List<Message> messages = messageRepo.findByConversationOrderByCreatedAtAsc(conversation);

        messages.forEach(m -> {
            if (!m.getSender().getId().equals(userId) && m.getStatus() == MessageStatus.SENT) {
                m.setStatus(MessageStatus.DELIVERED);
            }
        });

        messageRepo.saveAll(messages);

        return messages;
    }

    @Override
    public List<Message> getMessagesByEmail(String email, Long otherUserId) {
        return getMessages(getUserByEmail(email).getId(), otherUserId);
    }

    // ================= CONVERSATION =================
    @Override
    public List<ConversationListDTO> getUserConversations(Long userId) {
        return conversationRepo.getConversationList(userId);
    }

    @Override
    public List<ConversationListDTO> getUserConversationsByEmail(String email) {
        return getUserConversations(getUserByEmail(email).getId());
    }

    // ================= STATUS =================
    @Override
    public void markAsDelivered(Long conversationId, Long userId) {
        List<Message> messages = messageRepo.findUndeliveredMessages(conversationId, userId);
        messages.forEach(m -> m.setStatus(MessageStatus.DELIVERED));
        messageRepo.saveAll(messages);
    }

    @Override
    @Transactional
    public void markAsSeen(Long conversationId, Long userId) {
        messageRepo.markAsSeen(conversationId, userId, MessageStatus.SEEN, LocalDateTime.now());
    }

    // ================= USER =================
    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= UNREAD =================
    @Override
    public long getUnreadCount(Long conversationId, Long userId) {
        return messageRepo.countUnreadMessages(conversationId, userId);
    }

    // ================= DELETE =================
    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepo.softDeleteMessage(messageId);
    }

    // ================= REACTION =================
    @Override
    @Transactional
    public void reactToMessage(Long messageId, String reaction) {
        Message m = messageRepo.findById(messageId).orElseThrow();
        m.setReaction(reaction);
    }

    @Override
    @Transactional
    public void removeReaction(Long messageId) {
        Message m = messageRepo.findById(messageId).orElseThrow();
        m.setReaction(null);
    }

    // ================= BLOCK =================
    @Override
    @Transactional
    public void blockUser(Long blockerId, Long blockedId) {

        if (blockerId.equals(blockedId)) {
            throw new RuntimeException("You cannot block yourself");
        }

        if (blockRepo.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            return;
        }

        UserBlock block = new UserBlock();
        block.setBlockerId(blockerId);
        block.setBlockedId(blockedId);

        blockRepo.save(block);
    }

    @Override
    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        blockRepo.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Override
    public boolean isBlocked(Long user1, Long user2) {
        return blockRepo.existsByBlockerIdAndBlockedId(user1, user2)
                || blockRepo.existsByBlockerIdAndBlockedId(user2, user1);
    }

    // ================= HELPERS =================
    private Conversation getOrCreateConversation(User sender, User receiver) {

        // 🔥 BLOCK CHECK (IMPORTANT)
        if (isBlocked(sender.getId(), receiver.getId())) {
            throw new RuntimeException("Cannot create conversation. User is blocked.");
        }

        User u1 = sender.getId() < receiver.getId() ? sender : receiver;
        User u2 = sender.getId() < receiver.getId() ? receiver : sender;

        if (!matchRepo.findByUser1AndUser2(u1, u2).isPresent()) {
            throw new RuntimeException("Match required");
        }

        return conversationRepo.findByUser1AndUser2(u1, u2)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(u1);
                    c.setUser2(u2);
                    return conversationRepo.save(c);
                });
    }

    private Conversation getConversation(User u1, User u2) {
        User a = u1.getId() < u2.getId() ? u1 : u2;
        User b = u1.getId() < u2.getId() ? u2 : u1;

        return conversationRepo.findByUser1AndUser2(a, b)
                .orElseThrow(() -> new RuntimeException("No conversation"));
    }
}