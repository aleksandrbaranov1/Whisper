package org.example.whisper.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class Controller {
    public Controller() {
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String allChats(){
        return "Page with all chats";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String profile(){
        return "Profile page";
    }
}
