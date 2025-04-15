package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.dto.UserRegistrationRequest;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        if(userRepository.existsByEmail(userRegistrationRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByNickname(userRegistrationRequest.getNickname())) {
            throw new RuntimeException("Nickname already exists");
        }

        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setNickname(userRegistrationRequest.getNickname());

        userRepository.save(user);
    }
}
