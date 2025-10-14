package org.example.whisper.Service;

import org.example.whisper.Configuration.RedisCacheConfig;
import org.example.whisper.Entity.Chat;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatCacheService {
    private final RedisTemplate<String, Object> cacheRedisTemplate;

    public ChatCacheService(RedisTemplate<String, Object> cacheRedisTemplate) {
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    public void cacheChat(Long chatId, Chat chat) {
        cacheRedisTemplate.opsForValue().set("chat:" + chatId, chat);
    }

    public Chat getChatFromCache(Long chatId) {
        return (Chat) cacheRedisTemplate.opsForValue().get("chat:" + chatId);
    }

    public void evictChat(Long chatId) {
        cacheRedisTemplate.delete("chat:" + chatId);
    }

}
