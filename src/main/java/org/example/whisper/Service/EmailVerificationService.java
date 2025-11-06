package org.example.whisper.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${MAIL_USERNAME}")
    private String fromMail;

    public EmailVerificationService(JavaMailSender mailSender, RedisTemplate<String, String> redisTemplate) {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    public void sendVerificationCode(String email) {
        String code = generateCode();
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(email);
        message.setSubject("Код подтверждения регистрации");
        message.setText("Ваш код: " + code + "\nОн действителен 5 минут.");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return code.equals(savedCode);
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
