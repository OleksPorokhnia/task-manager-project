package com.alex.project.taskmanagerproject.controllers;


import com.alex.project.taskmanagerproject.dto.UserLoginRequest;
import com.alex.project.taskmanagerproject.dto.UserRegistrationRequest;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import com.alex.project.taskmanagerproject.service.CustomUserDetailsService;
import com.alex.project.taskmanagerproject.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String basic(){
        return "Hello World";
    }
    @GetMapping("/csrf")
    @PermitAll
    public ResponseEntity<String> getCsrfToken(HttpServletRequest request) {
        HttpSession session = request.getSession(true); // Retrieve existing session
        if (session != null) {
            System.out.println("Session ID: " + session.getId());
        } else {
            System.out.println("No valid session found!");
        }
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        return ResponseEntity.ok(csrfToken != null ? csrfToken.getToken() : "No CSRF Token");
    }

    @PostMapping("/register")
    @CrossOrigin
    public ResponseEntity<?> registerUser(@RequestBody @Valid
                                              UserRegistrationRequest registrationRequest) {
        try{
            userService.registerUser(registrationRequest);
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public String currentUser(@AuthenticationPrincipal  UserDetails userDetails) {
        return "Current user: " + userDetails.getUsername();
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email or password are invalid");
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user,loginRequest.getPassword(), user.getAuthorities());
        // 3. Установи его в SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 4. Привяжи к текущей сессии
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok("Logged in");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully"));
    }
}
