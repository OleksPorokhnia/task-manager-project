package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.exception.ValidationException;
import com.alex.project.taskmanagerproject.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @MessageMapping("/project/{projectId}/tasks")
    @SendTo("/topic/project/{projectId}/tasks")
    public List<Task> getAllTasks(@DestinationVariable int projectId) {
        return taskService.getAllTasks(projectId);
    }

    @MessageMapping("/project/{projectId}/task/add")
    @SendTo("/topic/project/{projectId}/task/add")
    public Task addTask(
            @DestinationVariable int projectId,
            @Payload @Valid TaskDto taskDto) {
        return taskService.createTask(taskDto, projectId);
    }

    @MessageMapping("/project/{projectId}/task/delete")
    @SendTo("/topic/project/{projectId}/task/delete")
    public List<Integer> deleteAllTasks(
            @DestinationVariable int projectId,
            @Payload List<Integer> taskIds){
        try {
            taskService.deleteAllSelectedTasks(taskIds);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return taskIds;
    }

    @MessageMapping("/project/getCurrentUser")
    @SendToUser("/queue/current")
    public String handleSomething(Principal principal) {
        return principal.getName();
    }

    @MessageMapping("/project/{projectId}/task/{taskId}/update")
    @SendTo("/topic/project/{projectId}/task/update")
    public Task updateTask(
            @DestinationVariable int projectId,
            @DestinationVariable int taskId,
            @Payload @Valid TaskDto taskDto){
        return taskService.updateTask(taskDto, projectId, taskId);
    }
}
