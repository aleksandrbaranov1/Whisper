package org.example.whisper.Service;

import jakarta.transaction.Transactional;
import org.example.whisper.DTO.UserRegistrationDTO;
import org.example.whisper.Entity.User;
import org.example.whisper.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(UserRegistrationDTO request){
//        if(userRepository.findByName(request.getName()).isPresent()){
//            throw new RuntimeException("User is already exist");
//        }

        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());

        System.out.println("Saving user: " + user.getName());

        userRepository.save(user);
    }

}
