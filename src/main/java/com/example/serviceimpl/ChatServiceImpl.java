package com.example.serviceimpl;

import com.example.dto.response.ConversationListDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;
import com.example.service.NotificationService;
import com.example.service.UserBlockService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserBlockService userBlockService; // ✅ FIXED
    private final NotificationService notificationService;
    private final MatchRepository matchRepository;

    // ================= USER =================

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public long getUnreadCount(Long conversationId, Long userId) {
        return messageRepository.countUnreadMessages(conversationId, userId);
    }

    // ================= VALIDATION =================

    private void validateMatch(Long u1, Long u2) {

        Long smaller = Math.min(u1, u2);
        Long larger = Math.max(u1, u2);

        boolean exists = matchRepository
                .existsByUser1_IdAndUser2_Id(smaller, larger);

        if (!exists) {
            throw new RuntimeException("❌ Chat not allowed without match");
        }
    }

    // ✅ FINAL FIX (NO ERROR)
    private void validateNotBlocked(Long u1, Long u2) {

        if (userBlockService.isBlocked(u1, u2)) {
            throw new RuntimeException("User is blocked");
        }
    }

    // ================= CONVERSATION =================

    private Conversation getOrCreateConversation(User u1, User u2) {

        if (u1.getId().equals(u2.getId())) {
            throw new RuntimeException("Cannot create conversation with yourself");
        }

        return conversationRepository
                .findByUser1AndUser2(u1, u2)
                .or(() -> conversationRepository.findByUser1AndUser2(u2, u1))
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(u1);
                    c.setUser2(u2);
                    return conversationRepository.save(c);
                });
    }

    private Conversation getConversation(User u1, User u2) {
        return conversationRepository
                .findByUser1AndUser2(u1, u2)
                .or(() -> conversationRepository.findByUser1AndUser2(u2, u1))
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    // ================= CREATE MESSAGE =================

    private Message createTextMessage(User sender, User receiver, String content, Message replyTo) {

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();
        m.setSender(sender);
        m.setConversation(c);
        m.setContent(encrypt(content));
        m.setMessageType("TEXT");
        m.setCreatedAt(LocalDateTime.now());
        m.setReplyTo(replyTo);

        Message saved = messageRepository.save(m);

        notificationService.create(sender.getId(), receiver.getId(), NotificationType.MESSAGE);

        return saved;
    }

    // ================= SEND =================

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        return sendMessageByEmail(getUser(senderId).getEmail(), receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content) {
        return sendMessageByEmail(senderEmail, receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content, Long replyToId) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());

        Message replyTo = null;
        if (replyToId != null) {
            replyTo = messageRepository.findById(replyToId)
                    .orElseThrow(() -> new RuntimeException("Reply not found"));
        }

        Message saved = createTextMessage(sender, receiver, content, replyTo);

        saved.setContent(decrypt(saved.getContent()));

        return saved;
    }

    // ================= MEDIA =================

    @Override
    public Message sendMediaMessage(String email, Long receiverId, String mediaUrl, String mediaType) {

        User sender = getUserByEmail(email);
        User receiver = getUser(receiverId);

        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();
        m.setSender(sender);
        m.setConversation(c);
        m.setMediaUrl(mediaUrl);
        m.setMediaType(mediaType);
        m.setMessageType("MEDIA");
        m.setCreatedAt(LocalDateTime.now());
        m.setContent(encrypt(getMediaContent(mediaType)));

        Message saved = messageRepository.save(m);

        saved.setContent(decrypt(saved.getContent()));

        return saved;
    }

    // ================= GET =================

    @Override
    public List<Message> getMessages(Long userId, Long otherUserId) {

        Conversation c = getConversation(getUser(userId), getUser(otherUserId));

        List<Message> list = messageRepository.findByConversationOrderByCreatedAtAsc(c);

        list.forEach(m -> m.setContent(decrypt(m.getContent())));

        return list;
    }

    @Override
    public List<Message> getMessagesByEmail(String email, Long otherUserId) {
        return getMessages(getUserByEmail(email).getId(), otherUserId);
    }

    @Override
    public Page<Message> getMessages(Long userId, Long otherUserId, int page, int size) {

        Conversation c = getConversation(getUser(userId), getUser(otherUserId));

        Page<Message> p = messageRepository.findByConversation(
                c, PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        p.getContent().forEach(m -> m.setContent(decrypt(m.getContent())));

        return p;
    }

    // ================= STATUS =================

    @Override
    public void markAsDelivered(Long conversationId, Long userId) {
        List<Message> messages = messageRepository.findUndeliveredMessages(conversationId, userId);
        messages.forEach(m -> m.setStatus(MessageStatus.DELIVERED));
        messageRepository.saveAll(messages);
    }

    @Override
    public void markAsSeen(Long conversationId, Long userId) {
        messageRepository.markAsSeen(conversationId, userId, MessageStatus.SEEN, LocalDateTime.now());
    }

    // ================= CONVERSATION =================

    @Override
    public List<ConversationListDTO> getUserConversations(Long userId) {
        return conversationRepository.getConversationList(userId);
    }

    @Override
    public List<ConversationListDTO> getUserConversationsByEmail(String email) {
        return conversationRepository.getConversationList(getUserByEmail(email).getId());
    }

    // ================= REACTION =================

    @Override
    public void reactToMessage(Long id, String reaction) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        m.setReaction(reaction);
        messageRepository.save(m);
    }

    @Override
    public void removeReaction(Long messageId) {
        Message m = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        m.setReaction(null);
        messageRepository.save(m);
    }

    // ================= BLOCK =================

    @Override
    public void blockUser(Long blockerId, Long blockedId) {
        userBlockService.blockUser(blockerId, blockedId);
    }

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {
        userBlockService.unblockUser(blockerId, blockedId);
    }

    @Override
    public boolean isBlocked(Long user1, Long user2) {
        return userBlockService.isBlocked(user1, user2);
    }

    @Override
    public Message forwardMessage(String senderEmail, Long messageId, Long receiverId) {

        // 🔍 Get users
        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        // 🔒 Validation
        validateMatch(sender.getId(), receiver.getId());
        validateNotBlocked(sender.getId(), receiver.getId());

        // 📩 Get original message
        Message original = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Original message not found"));

        // 💬 Get or create conversation
        Conversation conversation = getOrCreateConversation(sender, receiver);

        // 🆕 Create new message
        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setConversation(conversation);

        // 🔁 Copy content (already encrypted in DB)
        newMessage.setContent(original.getContent());

        // 📸 Copy media if exists
        newMessage.setMediaUrl(original.getMediaUrl());
        newMessage.setMediaType(original.getMediaType());

        newMessage.setMessageType("FORWARD");
        newMessage.setCreatedAt(LocalDateTime.now());

        // 💾 Save
        Message saved = messageRepository.save(newMessage);

        // 🔓 Decrypt before sending response
        saved.setContent(decrypt(saved.getContent()));

        return saved;
    }

    // ================= DELETE =================

    @Override
    public void deleteMessage(Long id) {
        messageRepository.softDeleteMessage(id);
    }

    // ================= EDIT =================

    @Override
    public void editMessage(Long id, String content) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        m.setContent(encrypt(content));
        messageRepository.save(m);
    }

    // ================= ENCRYPT =================

    private String encrypt(String data) {
        return data == null ? null : Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decrypt(String data) {
        if (data == null) return null;

        try {
            return new String(Base64.getDecoder().decode(data));
        } catch (Exception e) {
            return data;
        }
    }

    private String getMediaContent(String type) {
        return switch (type.toUpperCase()) {
            case "IMAGE" -> "[Image]";
            case "VIDEO" -> "[Video]";
            case "AUDIO" -> "[Audio]";
            default -> "[Media]";
        };
    }
}