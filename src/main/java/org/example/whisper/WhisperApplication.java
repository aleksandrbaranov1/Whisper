package org.example.whisper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class WhisperApplication {

    public static void main(String[] args) {

        SpringApplication.run(WhisperApplication.class, args);
        System.out.println(new BCryptPasswordEncoder().encode("password1"));
        System.out.println(new BCryptPasswordEncoder().encode("password2"));

    }

}
