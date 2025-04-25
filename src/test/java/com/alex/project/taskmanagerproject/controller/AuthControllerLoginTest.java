package com.alex.project.taskmanagerproject.controller;

import com.alex.project.taskmanagerproject.controllers.UserController;
import com.alex.project.taskmanagerproject.dto.UserLoginRequest;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class AuthControllerLoginTest {

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
    void whenValidCredentials_thenReturnsJwt() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("test1@gmail.com", "test1");
        String json = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails("sasha",
                "test1",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "test1@gmail.com");
        when(customUserDetailsService.loadUserByUsername("test1@gmail.com"))
                .thenReturn(customUserDetails);
        when(passwordEncoder.matches("test1", customUserDetails.getPassword()))
                .thenReturn(Boolean.TRUE);

        when(jwtService.generateToken(customUserDetails)).thenReturn("MOCK_JWT");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("MOCK_JWT"));
    }

    @Test
    void whenInvalidPassword_thenDoNotReturnsJwt() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("test1@gmail.com", "test1");
        String json = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails("sasha",
                "test1",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "test1@gmail.com");

        when(customUserDetailsService.loadUserByUsername("test1@gmail.com"))
                .thenReturn(customUserDetails);
        when(passwordEncoder.matches("test4", customUserDetails.getPassword()))
                .thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email or password incorrect"));
    }

    @Test
    void whenInvalidEmail_thenReturnBadRequest() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("test1@gmail.com", "test1");
        String json = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails("sasha",
                "test1",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "test1@gmail.com");

        when(customUserDetailsService.loadUserByUsername("test1@gmail.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));
        when(passwordEncoder.matches("test4", customUserDetails.getPassword()))
                .thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void whenEmailBlank_thenReturnBadRequest() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("", "test1");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email cannot be blank"));
    }

    @Test
    void whenEmailIncorrectType_thenReturnBadRequest() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("test1", "test1");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Incorrect email format"));
    }

    @Test
    void whenPasswordBlank_thenReturnBadRequest() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("test1@gmail.com", "");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password cannot be blank"));
    }

    @Test
    void whenEmailAndPasswordBlank_thenReturnBadRequest() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest("", "");
        String json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password cannot be blank"))
                .andExpect(jsonPath("$.email").value("Email cannot be blank"));
    }
}
