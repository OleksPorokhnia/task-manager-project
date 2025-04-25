package com.alex.project.taskmanagerproject.controller;


import com.alex.project.taskmanagerproject.controllers.UserController;
import com.alex.project.taskmanagerproject.dto.UserRegistrationRequest;
import com.alex.project.taskmanagerproject.service.CustomUserDetailsService;
import com.alex.project.taskmanagerproject.service.JwtService;
import com.alex.project.taskmanagerproject.service.UserService;
import com.alex.project.taskmanagerproject.util.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class AuthControllerRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void whenValidCredentials_thenReturnJwt() throws Exception {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest("testName", "test1@gmail.com", "password");
        String json = objectMapper.writeValueAsString(registrationRequest);

        doNothing().when(userService).registerUser(registrationRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails(
                "testName",
                "encodedPassword",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "test1@gmail.com"
        );

        when(customUserDetailsService.loadUserByUsername("test1@gmail.com"))
                .thenReturn(customUserDetails);

        when(passwordEncoder.matches(registrationRequest.getPassword(), "encodedPassword"))
                .thenReturn(Boolean.TRUE);

        when(jwtService.generateToken(customUserDetails))
                .thenReturn("MOCK_JWT");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("MOCK_JWT"));
    }
}
