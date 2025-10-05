package org.example.whisper.Service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class VerificationService {
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    public VerificationService(StringRedisTemplate redisTemplate,
                               EmailService emailService){
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }
    public void sendCode(String email) {
        String key = "verify:" + email;

        // Если уже отправляли недавно — не спамим
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("Код уже был отправлен. Подожди несколько минут.");
        }

        // Генерируем 6-значный код
        String code = String.format("%06d", new Random().nextInt(999999));

        // Сохраняем код в Redis с TTL = 10 минут
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(10));

        // Отправляем на email
        emailService.sendVerificationCode(email, code);

        System.out.println(">>> Код подтверждения для " + email + ": " + code);
    }
    public boolean verifyCode(String email, String code) {
        String key = "verify:" + email;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(key); // удаляем после успешной проверки
            return true;
        }
        return false;
    }

}
