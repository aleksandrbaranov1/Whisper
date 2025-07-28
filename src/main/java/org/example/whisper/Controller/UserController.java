package org.example.whisper.Controller;

import org.example.whisper.Entity.User;
import org.example.whisper.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getUserIdl")
    public ResponseEntity<Long> getUserId(Authentication authentication){
        User user = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok(user.getId());
    }
}
