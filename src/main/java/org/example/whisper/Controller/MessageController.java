package org.example.whisper.Controller;
import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Repository.UserRepository;
import org.example.whisper.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService, UserRepository userRepository){
        this.messageService = messageService;

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
}
