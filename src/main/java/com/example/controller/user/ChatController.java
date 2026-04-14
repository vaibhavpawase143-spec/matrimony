package com.example.controller.user;

import com.example.dto.request.ChatMessageDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // ================= 💬 SEND MESSAGE =================
    @PostMapping("/send")
    public ApiResponse<ChatMessageDTO> sendMessage(
            @RequestBody Map<String, Object> request,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Long receiverId = Long.valueOf(request.get("receiverId").toString());
        String content = request.get("content").toString();
        Long replyToMessageId = request.get("replyToMessageId") != null
                ? Long.valueOf(request.get("replyToMessageId").toString())
                : null;

        Message message = chatService.sendMessageByEmail(
                senderEmail,
                receiverId,
                content,
                replyToMessageId
        );

        ChatMessageDTO dto = mapToDTO(message);

        return ApiResponse.<ChatMessageDTO>builder()
                .success(true)
                .message("Message sent successfully")
                .data(dto)
                .build();
    }

    // ================= 📤 FORWARD =================
    @PostMapping("/forward")
    public ApiResponse<ChatMessageDTO> forwardMessage(
            @RequestBody Map<String, Object> request,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Long messageId = Long.valueOf(request.get("messageId").toString());
        Long receiverId = Long.valueOf(request.get("receiverId").toString());

        Message message = chatService.forwardMessage(senderEmail, messageId, receiverId);

        ChatMessageDTO dto = mapToDTO(message);

        return ApiResponse.<ChatMessageDTO>builder()
                .success(true)
                .message("Message forwarded")
                .data(dto)
                .build();
    }

    // ================= 📸 MEDIA =================
    @PostMapping("/send-media")
    public ApiResponse<ChatMessageDTO> sendMedia(
            @RequestBody Map<String, Object> request,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Long receiverId = Long.valueOf(request.get("receiverId").toString());
        String mediaUrl = request.get("mediaUrl").toString();
        String mediaType = request.get("mediaType").toString();

        Message message = chatService.sendMediaMessage(
                senderEmail,
                receiverId,
                mediaUrl,
                mediaType
        );

        ChatMessageDTO dto = mapToDTO(message);

        return ApiResponse.<ChatMessageDTO>builder()
                .success(true)
                .message("Media sent")
                .data(dto)
                .build();
    }

    // ================= ❤️ REACTION =================
    @PutMapping("/reaction")
    public ApiResponse<String> react(@RequestBody Map<String, Object> request) {

        Long messageId = Long.valueOf(request.get("messageId").toString());
        String reaction = request.get("reaction").toString();

        chatService.reactToMessage(messageId, reaction);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Reaction added")
                .data(null)
                .build();
    }

    // ================= 🗑 DELETE =================
    @DeleteMapping("/message/{messageId}")
    public ApiResponse<String> deleteMessage(@PathVariable Long messageId) {

        chatService.deleteMessage(messageId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message deleted")
                .data(null)
                .build();
    }

    // ================= ✔✔ SEEN =================
    @PutMapping("/seen/{conversationId}")
    public ApiResponse<String> markAsSeen(
            @PathVariable Long conversationId,
            Principal principal
    ) {

        String email = principal.getName();
        User user = chatService.getUserByEmail(email);

        chatService.markAsSeen(conversationId, user.getId());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Messages marked as seen")
                .data(null)
                .build();
    }

    // ================= 📄 PAGINATION =================
    @GetMapping("/messages")
    public ApiResponse<Page<ChatMessageDTO>> getMessages(
            @RequestParam Long otherUserId,
            @RequestParam int page,
            @RequestParam int size,
            Principal principal
    ) {

        String email = principal.getName();

        Page<Message> messages = chatService.getMessages(
                chatService.getUserByEmail(email).getId(),
                otherUserId,
                page,
                size
        );

        Page<ChatMessageDTO> dtoPage = messages.map(this::mapToDTO);

        return ApiResponse.<Page<ChatMessageDTO>>builder()
                .success(true)
                .message("Messages fetched")
                .data(dtoPage)
                .build();
    }

    // ================= ✏️ EDIT =================
    @PutMapping("/edit")
    public ApiResponse<String> editMessage(@RequestBody Map<String, Object> request) {

        Long messageId = Long.valueOf(request.get("messageId").toString());
        String content = request.get("content").toString();

        chatService.editMessage(messageId, content);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message updated")
                .data(null)
                .build();
    }

    // ================= 🔥 FINAL MAPPER =================
    private ChatMessageDTO mapToDTO(Message message) {
        return ChatMessageDTO.builder()
                .sender(message.getSender().getEmail())
                .receiver(message.getReceiver().getEmail())

                // ✅ FIXED
                .receiverId(
                        message.getReceiver() != null
                                ? message.getReceiver().getId()
                                : null
                )

                .content(
                        message.getContent() != null
                                ? message.getContent()
                                : "[Media]"
                )

                .type(message.getMessageType())
                .conversationId(message.getConversation().getId())
                .messageId(message.getId())
                .status(message.getStatus().name())

                .timestamp(
                        message.getCreatedAt() != null
                                ? message.getCreatedAt().toString()
                                : null
                )

                .replyToMessageId(
                        message.getReplyTo() != null ? message.getReplyTo().getId() : null
                )
                .replyToContent(
                        message.getReplyTo() != null ? message.getReplyTo().getContent() : null
                )

                .mediaUrl(message.getMediaUrl())
                .mediaType(message.getMediaType())
                .build();
    }
    // ================= 📋 CONVERSATIONS =================
    @GetMapping("/conversations")
    public ApiResponse<?> getConversations(Principal principal) {

        String email = principal.getName();

        return ApiResponse.builder()
                .success(true)
                .message("Conversations fetched")
                .data(chatService.getUserConversationsByEmail(email))
                .build();
    }
}