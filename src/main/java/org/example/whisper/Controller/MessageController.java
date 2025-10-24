package org.example.whisper.Controller;
import org.example.whisper.DTO.DeleteMessageDTO;
import org.example.whisper.DTO.MarkAsReadRequest;
import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.DTO.ReadConfirmationResponse;
import org.example.whisper.Entity.Message;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService,
                             UserService userService,
                             SimpMessagingTemplate messagingTemplate){
        this.messageService = messageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping(consumes = { "multipart/form-data", "application/json" })
    public ResponseEntity<?> sendMessage(
            @RequestPart(value = "message", required = false) MessageDTO messageDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (messageDTO != null && image != null) {
            messageDTO.setPhoto(image);  // <-- исправлено
        }
        if (messageDTO != null) {
            return messageService.sendMessage(messageDTO, userDetails);
        }

        return ResponseEntity.badRequest().body("Нет данных для отправки сообщения");
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long chatId) {
        return messageService.getChatMessages(chatId);
    }

    @PatchMapping("/mark-read")
    public void markMessagesAsRead(@RequestBody MarkAsReadRequest request,
                                   Authentication auth) {

        String email = auth.getName();
        User user = userService.findByEmail(email);

        messageService.markMessagesAsRead(user.getId(), request.getMessageIds());

        messagingTemplate.convertAndSend(
                "/topic/chats/" + request.getChatId() + "/read",
                new ReadConfirmationResponse(request.getMessageIds())
        );
    }

    @DeleteMapping("/delete/{chatId}/{messageId}")
    private ResponseEntity<Void> deleteMessage(@PathVariable Long chatId,
                                               @PathVariable Long messageId,
                                               Authentication auth) {
        messageService.deleteMessage(chatId, messageId, auth);

        DeleteMessageDTO deleteMessageDTO = new DeleteMessageDTO(messageId, chatId);

        messagingTemplate.convertAndSend(
                "/topic/chats/" + chatId + "/deleted",
                deleteMessageDTO
        );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/edit/{messageId}/{newContent}")
    private ResponseEntity<?> editMessage(@PathVariable Long messageId,
                                          @PathVariable String newContent,
                                          Authentication authentication){
        return messageService.editMessage(messageId, newContent, authentication);
    }
}
