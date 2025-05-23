package org.example.whisper.Controller;

import org.example.whisper.Service.ProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "http://127.0.0.1:8081")
@RestController
@RequestMapping("/api/chats")
public class Controller {
    public Controller() {
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String allChats(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) {
            return "No authentication in context";
        }

        return "User: " + authentication.getName() +
                ", Authorities: " + authentication.getAuthorities().toString();
    }
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String profile(){
        return "Profile page";
    }
}
