package org.example.whisper.Service;

import org.example.whisper.DTO.UpdateBIODTO;
import org.example.whisper.DTO.UserDTO;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + name));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
    public List<String> searchUsers(@RequestParam String name){
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }

//    public ResponseEntity<?> updateBio(@RequestBody UpdateBIODTO bioDTO,
//                                       Authentication authentication){
//        User user = userRepository.findByEmail(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
//
//        if(!user.getId().equals(bioDTO.getUserId())){
//            throw new RuntimeException("Пользователь не имеет доступ к редактированию информации");
//        }
//
//        user.setBio(bioDTO.getBio());
//        userRepository.save(user);
//        return ResponseEntity.ok(new UserDTO(user));
//    }
}
