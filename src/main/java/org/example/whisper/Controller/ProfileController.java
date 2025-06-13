package org.example.whisper.Controller;

import org.example.whisper.DTO.UpdateBIODTO;
import org.example.whisper.DTO.UserDTO;
import org.example.whisper.Service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDTO> loadUserInfo(Authentication authentication){
        return profileService.loadUserInfo(authentication);
    }

    @PatchMapping("/updateBio")
    public ResponseEntity<?> updateBio(@RequestBody UpdateBIODTO bioDto,
                                       Authentication authentication){
        return profileService.updateBio(bioDto, authentication);
    }
}
