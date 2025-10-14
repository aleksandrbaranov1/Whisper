package org.example.whisper.Controller;

import org.example.whisper.Entity.Chat;
import org.example.whisper.Service.ChatCacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCacheController {
    private final ChatCacheService chatCacheService;

    public TestCacheController(ChatCacheService chatCacheService) {
        this.chatCacheService = chatCacheService;
    }

    @GetMapping("/cache/test/{chatId}")
    public Chat testCache(@PathVariable Long chatId) {
        return chatCacheService.getChatFromCache(chatId);
    }
}