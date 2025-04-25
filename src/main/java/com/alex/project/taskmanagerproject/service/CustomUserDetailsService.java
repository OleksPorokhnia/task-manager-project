package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import com.alex.project.taskmanagerproject.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to load user with username: " + username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getNickname(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                user.getEmail()
        );
    }
}
