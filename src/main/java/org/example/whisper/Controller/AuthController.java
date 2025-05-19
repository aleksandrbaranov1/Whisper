package org.example.whisper.Controller;

import org.example.whisper.DTO.JwtResponse;
import org.example.whisper.DTO.LoginRequest;
import org.example.whisper.DTO.UserRegistrationDTO;
import org.example.whisper.Security.MyUserDetails;
import org.example.whisper.Security.RegistrationService;
import org.example.whisper.Service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(RegistrationService registrationService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService){
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO request) {
        System.out.println(">>> [DEBUG] Register endpoint called with: " + request.getName());
        registrationService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            e.printStackTrace(); // или логируй через логгер
            return ResponseEntity.status(403).body("Authentication failed: " + e.getMessage());
        }
    }

}
