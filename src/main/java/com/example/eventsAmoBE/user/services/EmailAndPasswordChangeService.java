package com.example.eventsAmoBE.user.services;

import com.example.eventsAmoBE.user.UserRepository;
import com.example.eventsAmoBE.user.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailAndPasswordChangeService {

    private final UserRepository userRepository;

    public EmailAndPasswordChangeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeEmail(String email, String newEmail){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(newEmail);
    }

    public void changePassword(String email, String newPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(newPassword);
    }
}
