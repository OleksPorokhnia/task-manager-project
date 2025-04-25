package com.alex.project.taskmanagerproject.config;

import com.alex.project.taskmanagerproject.controllers.UserController;
import com.alex.project.taskmanagerproject.service.CustomUserDetailsService;
import com.alex.project.taskmanagerproject.service.JwtService;
import com.alex.project.taskmanagerproject.util.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserController userController;

    @Autowired
    public WebSocketConfiguration(JwtService jwtService, CustomUserDetailsService customUserDetailsService, UserController userController) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userController = userController;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/portfolio")
                .addInterceptors(new JwtHandshakeInterceptor(jwtService, customUserDetailsService))
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
        config.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(4 * 8192);
        registry.setTimeToFirstMessage(3000);
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
