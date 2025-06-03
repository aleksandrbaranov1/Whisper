package org.example.whisper.Controller;
import org.example.whisper.DTO.MarkAsReadRequest;
import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.UserRepository;
import org.example.whisper.Service.MessageService;
import org.example.whisper.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService,
                             UserRepository userRepository,
                             UserService userService){
        this.messageService = messageService;
        this.userService = userService;
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
    public ResponseEntity<Void> markMessagesAsRead(
            @RequestBody MarkAsReadRequest request,
            Authentication auth) {

        String email = auth.getName();
        User user = userService.findByEmail(email);

        messageService.markMessagesAsRead(user.getId(), request.getMessageIds());

        return ResponseEntity.ok().build();
    }
}
