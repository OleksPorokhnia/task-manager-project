package com.alex.project.taskmanagerproject.util;

import com.alex.project.taskmanagerproject.service.CustomUserDetailsService;
import com.alex.project.taskmanagerproject.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    public JwtHandshakeInterceptor(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if(request instanceof ServletServerHttpRequest servletRequest) {
            String token  = servletRequest.getServletRequest().getParameter("token");
            if(token != null) {
                String username = jwtService.extractUsername(token);
                if(username != null) {
                    CustomUserDetails user = userDetailsService.loadUserByUsername(username);
                    if(jwtService.isTokenValid(token, user)) {
                        attributes.put("user", new StompPrincipal(username));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
