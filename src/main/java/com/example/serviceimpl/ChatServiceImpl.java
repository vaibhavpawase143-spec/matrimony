package com.example.serviceimpl;

import com.example.dto.response.ConversationListDTO;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ChatService;

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
    private final UserBlockRepository userBlockRepository;

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

    // ================= SEND =================

    @Override
    public Message sendMessage(Long senderId, Long receiverId, String content) {

        if (isBlocked(senderId, receiverId)) {
            throw new RuntimeException("Blocked user");
        }

        return createTextMessage(getUser(senderId), getUser(receiverId), content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content) {
        return sendMessageByEmail(senderEmail, receiverId, content, null);
    }

    @Override
    public Message sendMessageByEmail(String senderEmail, Long receiverId, String content, Long replyToId) {

        User sender = getUserByEmail(senderEmail);
        User receiver = getUser(receiverId);

        if (isBlocked(sender.getId(), receiver.getId())) {
            throw new RuntimeException("Blocked user");
        }

        Message replyTo = null;
        if (replyToId != null) {
            replyTo = messageRepository.findById(replyToId)
                    .orElseThrow(() -> new RuntimeException("Reply not found"));
        }

        return createTextMessage(sender, receiver, content, replyTo);
    }

    // ================= MEDIA =================

    @Override
    public Message sendMediaMessage(String email, Long receiverId, String mediaUrl, String mediaType) {

        User sender = getUserByEmail(email);
        User receiver = getUser(receiverId);

        if (isBlocked(sender.getId(), receiver.getId())) {
            throw new RuntimeException("Blocked user");
        }

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();
        m.setSender(sender);
        m.setConversation(c);
        m.setMediaUrl(mediaUrl);
        m.setMediaType(mediaType);
        m.setMessageType("MEDIA");
        m.setCreatedAt(LocalDateTime.now());

        m.setContent(encrypt(getMediaContent(mediaType)));

        return messageRepository.save(m);
    }

    // ================= FORWARD =================

    @Override
    public Message forwardMessage(String email, Long messageId, Long receiverId) {

        User sender = getUserByEmail(email);
        User receiver = getUser(receiverId);

        if (isBlocked(sender.getId(), receiver.getId())) {
            throw new RuntimeException("Blocked user");
        }

        Message original = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();
        m.setSender(sender);
        m.setConversation(c);

        m.setContent(original.getContent()); // already encrypted

        m.setMediaUrl(original.getMediaUrl());
        m.setMediaType(original.getMediaType());
        m.setMessageType("FORWARD");
        m.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(m);
    }

    // ================= GET =================

    @Override
    public Page<Message> getMessages(Long userId, Long otherUserId, int page, int size) {

        if (isBlocked(userId, otherUserId)) {
            throw new RuntimeException("Blocked");
        }

        Conversation c = getConversation(userId, otherUserId);

        Page<Message> p = messageRepository.findByConversation(
                c, PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        p.getContent().forEach(m -> m.setContent(decrypt(m.getContent())));

        return p;
    }

    @Override
    public List<Message> getMessages(Long userId, Long otherUserId) {

        if (isBlocked(userId, otherUserId)) {
            throw new RuntimeException("Blocked");
        }

        Conversation c = getConversation(userId, otherUserId);

        List<Message> list = messageRepository.findByConversationOrderByCreatedAtAsc(c);

        list.forEach(m -> m.setContent(decrypt(m.getContent())));

        return list;
    }

    @Override
    public List<Message> getMessagesByEmail(String email, Long otherUserId) {
        return getMessages(getUserByEmail(email).getId(), otherUserId);
    }

    // ================= EDIT =================

    @Override
    public void editMessage(Long id, String content) {

        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        m.setContent(encrypt(content));

        messageRepository.save(m);
    }

    // ================= DELETE =================

    @Override
    public void deleteMessage(Long id) {
        messageRepository.softDeleteMessage(id);
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
    public void removeReaction(Long id) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        m.setReaction(null);
        messageRepository.save(m);
    }

    // ================= BLOCK =================

    @Override
    public void blockUser(Long blockerId, Long blockedId) {

        if (userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) return;

        UserBlock b = new UserBlock();
        b.setBlockerId(blockerId);
        b.setBlockedId(blockedId);

        userBlockRepository.save(b);
    }

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {
        userBlockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    @Override
    public boolean isBlocked(Long u1, Long u2) {
        return userBlockRepository.existsByBlockerIdAndBlockedId(u1, u2)
                || userBlockRepository.existsByBlockerIdAndBlockedId(u2, u1);
    }

    // ================= HELPER =================

    private Message createTextMessage(User sender, User receiver, String content, Message replyTo) {

        Conversation c = getOrCreateConversation(sender, receiver);

        Message m = new Message();
        m.setSender(sender);
        m.setConversation(c);

        m.setContent(encrypt(content));

        m.setMessageType("TEXT");
        m.setCreatedAt(LocalDateTime.now());
        m.setReplyTo(replyTo);

        return messageRepository.save(m);
    }

    private String getMediaContent(String type) {
        return switch (type.toUpperCase()) {
            case "IMAGE" -> "[Image]";
            case "VIDEO" -> "[Video]";
            case "AUDIO" -> "[Audio]";
            default -> "[Media]";
        };
    }

    private Conversation getOrCreateConversation(User u1, User u2) {
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

    private Conversation getConversation(Long u1, Long u2) {
        return getOrCreateConversation(getUser(u1), getUser(u2));
    }

    // ================= ENCRYPTION =================

    private String encrypt(String data) {
        return data == null ? null : Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decrypt(String data) {
        return data == null ? null : new String(Base64.getDecoder().decode(data));
    }
    @Override
    public List<ConversationListDTO> getUserConversations(Long userId) {
        return conversationRepository.getConversationList(userId);
    }

    @Override
    public List<ConversationListDTO> getUserConversationsByEmail(String email) {
        User user = getUserByEmail(email);
        return conversationRepository.getConversationList(user.getId());
    }

    @Override
    public void markAsDelivered(Long conversationId, Long userId) {
        List<Message> messages = messageRepository.findUndeliveredMessages(conversationId, userId);

        for (Message m : messages) {
            m.setStatus(MessageStatus.DELIVERED);
        }

        messageRepository.saveAll(messages);
    }

    @Override
    public void markAsSeen(Long conversationId, Long userId) {
        messageRepository.markAsSeen(
                conversationId,
                userId,
                MessageStatus.SEEN,
                LocalDateTime.now()
        );
    }
}