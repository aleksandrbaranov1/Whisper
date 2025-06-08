package org.example.whisper.Controller;
import org.example.whisper.DTO.DeleteMessageDTO;
import org.example.whisper.DTO.MarkAsReadRequest;
import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.DTO.ReadConfirmationResponse;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.UserRepository;
import org.example.whisper.Service.MessageService;
import org.example.whisper.Service.UserService;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService,
                             UserRepository userRepository,
                             UserService userService,
                             SimpMessagingTemplate messagingTemplate){
        this.messageService = messageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO dto,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return messageService.sendMessage(dto, userDetails);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {
        return messageService.getChatMessages(chatId);
    }
    @PatchMapping("/mark-read")
    public void markMessagesAsRead(
            @RequestBody MarkAsReadRequest request,
            Authentication auth) {

        String email = auth.getName();
        User user = userService.findByEmail(email);

        messageService.markMessagesAsRead(user.getId(), request.getMessageIds());

        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getChatId(),
                new ReadConfirmationResponse(request.getMessageIds())
        );
    }

    @DeleteMapping("/delete/{chatId}/{messageId}")
    private ResponseEntity<Void> deleteMessage(@PathVariable Long chatId,
                                               @PathVariable Long messageId,
                                               Authentication auth){
        messageService.deleteMessage(chatId, messageId, auth);

        DeleteMessageDTO deleteMessageDTO = new DeleteMessageDTO(messageId, chatId);

        messagingTemplate.convertAndSend(
                "/topic/chats/" + chatId + "/deleted",
                deleteMessageDTO);
//        messagingTemplate.convertAndSend("/topic/chats/" + chatId + "/deleted", deleteMessageDTO);
        return ResponseEntity.noContent().build();
    }
}
