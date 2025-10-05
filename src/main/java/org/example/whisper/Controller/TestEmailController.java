package org.example.whisper.Controller;

import org.example.whisper.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestEmailController {

    private final EmailService emailService;

    public TestEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        try {
            emailService.sendVerificationCode(to, "123456");
            return ResponseEntity.ok("Письмо успешно отправлено на " + to);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка отправки: " + e.getMessage());
        }
    }
}
