package org.example.whisper.Service;

import org.example.whisper.DTO.UpdateBIODTO;
import org.example.whisper.DTO.UserDTO;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.UserRepository;
import org.example.whisper.Security.MyUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserDTO> loadUserInfo(Authentication authentication){
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserDTO(user));
    }
    public ResponseEntity<?> updateBio(@RequestBody UpdateBIODTO bioDTO,
                                       Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if(!user.getId().equals(bioDTO.getUserId())){
            throw new RuntimeException("Пользователь не имеет доступ к редактированию информации");
        }

        user.setBio(bioDTO.getBio());
        userRepository.save(user);
        return ResponseEntity.ok(new UserDTO(user));
    }
}
