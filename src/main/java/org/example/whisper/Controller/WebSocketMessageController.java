package org.example.whisper.Controller;

import org.example.whisper.DTO.MessageDTO;
import org.example.whisper.Service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketMessageService webSocketMessageService;

    public WebSocketMessageController(SimpMessagingTemplate messagingTemplate, WebSocketMessageService webSocketMessageService){
        this.messagingTemplate = messagingTemplate;
        this.webSocketMessageService = webSocketMessageService;
    }
    @MessageMapping("/chat.send")
    public void processMessage(@Payload MessageDTO dto) {
        MessageDTO savedMessage = webSocketMessageService.handleWebSocketMessage(dto);

        // Отправляем обновлённое сообщение только в нужный чат
        messagingTemplate.convertAndSend("/topic/chat." + dto.getChatId(), savedMessage);

        // Отдельно отправляем событие обновления для сайдбара (списка чатов)
        messagingTemplate.convertAndSend("/topic/chats", "update");
    }

}
