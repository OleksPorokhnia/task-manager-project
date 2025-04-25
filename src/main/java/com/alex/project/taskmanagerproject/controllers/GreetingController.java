package com.alex.project.taskmanagerproject.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class GreetingController {

    @MessageMapping("/greet")
    @SendTo("/topic/greetings")
    public GreetingsMessage handle(@Payload String greeting, StompHeaderAccessor accessor) {
        try {
            return new GreetingsMessage("[" + getTimestamp() + "]" + greeting);
        } catch (Exception e) {
            System.out.println("Error processing greeting" + e);
            throw e;
        }
    }

    private String getTimestamp() {
        return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
    }
}
