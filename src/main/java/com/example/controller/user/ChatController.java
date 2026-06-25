package com.example.controller.user;

import com.example.dto.request.ChatMessageDTO;
import com.example.dto.request.SendMessageRequestDTO;
import com.example.dto.response.ApiResponse;
import com.example.model.Message;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    // ================= 💬 SEND MESSAGE =================
    @PostMapping("/send")
    public ApiResponse<ChatMessageDTO> sendMessage(
            @RequestBody SendMessageRequestDTO request,
            Principal principal
    ) {

        String senderEmail = principal.getName();

        Long receiverId =
                request.getReceiverId();

        String content =
                request.getContent();

        String mediaUrl =
                request.getMediaUrl();

        String mediaType =
                request.getMediaType();
        Long replyToMessageId =
                request.getReplyToMessageId();


        Message message = chatService.sendMessageByEmail(
                senderEmail,
                receiverId,
                content,
                replyToMessageId
        );


        ChatMessageDTO dto = mapToDTO(message);
// Send live message to receiver
        messagingTemplate.convertAndSendToUser(
                message.getReceiver().getEmail(),
                "/queue/messages",
                dto
        );

// Send live message back to sender
        messagingTemplate.convertAndSendToUser(
                message.getSender().getEmail(),
                "/queue/messages",
                dto
        );
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

    @PutMapping("/messages/{id}/pin")
    public ApiResponse<String> pinMessage(
            @PathVariable Long id
    ) {

        chatService.pinMessage(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message pinned")
                .data(null)
                .build();
    }

    @PutMapping("/messages/{id}/unpin")
    public ApiResponse<String> unpinMessage(
            @PathVariable Long id
    ) {

        chatService.unpinMessage(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message unpinned")
                .data(null)
                .build();
    }
    @PutMapping("/messages/{id}/star")
    public ApiResponse<String> starMessage(
            @PathVariable Long id
    ) {

        chatService.starMessage(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message starred")
                .data(null)
                .build();
    }

    @PutMapping("/messages/{id}/unstar")
    public ApiResponse<String> unstarMessage(
            @PathVariable Long id
    ) {

        chatService.unstarMessage(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Message unstarred")
                .data(null)
                .build();
    }

    @GetMapping("/user/{id}")
    public ApiResponse<?> getChatUser(
            @PathVariable Long id
    ){

        User user = userRepository.findById(id)
                .orElseThrow();

        Map<String,Object> data = new HashMap<>();

        data.put("id", user.getId());
        data.put("otherUserId", user.getId());
        data.put("otherUsername", user.getEmail());
        data.put("isOnline", user.getIsOnline());
        data.put("lastSeen", user.getLastSeen());

        return ApiResponse.builder()
                .success(true)
                .message("User fetched")
                .data(data)
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

                .sender(

                        message.getSender()!=null

                                ?

                                message.getSender().getEmail()

                                :

                                null

                )

                .receiver(

                        message.getReceiver()!=null

                                ?

                                message.getReceiver().getEmail()

                                :

                                null

                )

                .senderId(

                        message.getSender()!=null

                                ?

                                message.getSender().getId()

                                :

                                null

                )

                .receiverId(

                        message.getReceiver()!=null

                                ?

                                message.getReceiver().getId()

                                :

                                null

                )

                .content(

                        message.getContent()!=null

                                ?

                                message.getContent()

                                :

                                "[Media]"

                )

                .type(

                        message.getMessageType()

                )

                .conversationId(

                        message.getConversation()!=null

                                ?

                                message.getConversation().getId()

                                :

                                null

                )

                .messageId(

                        message.getId()

                )

                .status(

                        message.getStatus()!=null

                                ?

                                message.getStatus().name()

                                :

                                null

                )

                .timestamp(

                        message.getCreatedAt()!=null

                                ?

                                message.getCreatedAt().toString()

                                :

                                null

                )

                .replyToMessageId(

                        message.getReplyTo()!=null

                                ?

                                message.getReplyTo().getId()

                                :

                                null

                )

                .replyToContent(

                        message.getReplyTo()!=null

                                ?

                                message.getReplyTo().getContent()

                                :

                                null

                )

                .mediaUrl(
                        message.getMediaUrl()
                )

                .mediaType(
                        message.getMediaType()
                )

                .deletedForEveryone(
                        message.getDeletedForEveryone()
                )

                .deletedForUsers(
                        message.getDeletedForUsers()
                )
                .pinned(
                        message.getPinned()
                )

                .starred(
                        message.getStarred()
                )

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
    @PutMapping("/offline")
    public ResponseEntity<?> offline(Principal principal){

        System.out.println("OFFLINE API CALLED");

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        user.setIsOnline(false);
        user.setLastSeen(LocalDateTime.now());
        user.setLastHeartbeat(null);

        userRepository.save(user);

        System.out.println(
                "USER OFFLINE -> " +
                        user.getEmail()
        );

        return ResponseEntity.ok().build();
    }
    @PutMapping("/ping")
    public ResponseEntity<?> ping(Principal principal){

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        user.setLastSeen(LocalDateTime.now());
        user.setIsOnline(true);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String fileName =

                    UUID.randomUUID()

                            + "_"

                            + file.getOriginalFilename();

            Path path = Paths.get(
                    "uploads/images",
                    fileName
            );

            Files.createDirectories(
                    path.getParent()
            );

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(

                    Map.of(

                            "url",

                            "/uploads/images/" + fileName

                    )

            );

        }
        catch (Exception e){

            return ResponseEntity.badRequest()

                    .body(

                            e.getMessage()

                    );

        }

    }
    @PostMapping("/upload-audio")
    public ResponseEntity<?> uploadAudio(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String fileName =

                    UUID.randomUUID()

                            + "_"

                            + file.getOriginalFilename();

            Path path = Paths.get(
                    "uploads/audio",
                    fileName
            );

            Files.createDirectories(
                    path.getParent()
            );

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(

                    Map.of(

                            "url",

                            "/uploads/audio/" + fileName

                    )

            );

        }
        catch (Exception e){

            return ResponseEntity.badRequest()

                    .body(
                            e.getMessage()
                    );

        }

    }
    @PostMapping("/upload-document")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

            Path path = Paths.get(
                    "uploads/documents",
                    fileName
            );

            Files.createDirectories(
                    path.getParent()
            );

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(
                    Map.of(
                            "url",
                            "/uploads/documents/" + fileName
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }
    }
    @PostMapping("/upload-video")
    public ResponseEntity<?> uploadVideo(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String fileName =

                    UUID.randomUUID()

                            + "_"

                            + file.getOriginalFilename();

            Path path = Paths.get(
                    "uploads/videos",
                    fileName
            );

            Files.createDirectories(
                    path.getParent()
            );

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(

                    Map.of(

                            "url",

                            "/uploads/videos/" + fileName

                    )

            );

        }
        catch (Exception e){

            return ResponseEntity.badRequest()

                    .body(

                            e.getMessage()

                    );

        }

    }
    @DeleteMapping("/messages/{id}/me")
    public ResponseEntity<?> deleteForMe(
            @PathVariable Long id,
            Principal principal
    ){
        User user =
                userRepository
                        .findByEmail(
                                principal.getName()
                        )
                        .orElseThrow();

        chatService.deleteForMe(
                id,
                user.getId()
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/messages/{id}/everyone")
    public ResponseEntity<?> deleteForEveryone(
            @PathVariable Long id,
            Principal principal
    ){
        User user =
                userRepository
                        .findByEmail(
                                principal.getName()
                        )
                        .orElseThrow();

        chatService.deleteForEveryone(
                id,
                user.getId()
        );

        return ResponseEntity.ok().build();
    }
}