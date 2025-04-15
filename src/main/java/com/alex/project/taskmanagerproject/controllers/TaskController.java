package com.alex.project.taskmanagerproject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project/{projectId}/task")
public class TaskController {

    
    public ResponseEntity<?> getTask(){
        return null;
    }
}
