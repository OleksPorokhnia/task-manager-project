package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.exception.ValidationException;
import com.alex.project.taskmanagerproject.mappers.TaskMapper;
import com.alex.project.taskmanagerproject.service.TaskService;
import com.alex.project.taskmanagerproject.service.ringBuffer.TaskRingBufferService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskRingBufferService taskRingBufferService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @MessageMapping("/project/{projectId}/tasks")
    @SendTo("/topic/project/{projectId}/tasks")
    public List<TaskDto> getAllTasks(@DestinationVariable int projectId) {
        List<TaskDto> allTasks = new ArrayList<>(taskRingBufferService.getBufferCopy(projectId));
        allTasks.addAll(taskService.getAll(projectId).stream().map(task -> taskMapper.toTaskDtoFromTask(task)).toList());
        System.out.println(allTasks);
        return allTasks;
    }

    @MessageMapping("/project/{projectId}/task/add")
    @SendTo("/topic/project/{projectId}/task/add")
    public TaskDto addTask(
            @DestinationVariable int projectId,
            @Payload @Valid TaskDto taskDto) {
        return taskRingBufferService.addToBuffer(taskDto, projectId);
    }

    @MessageMapping("/project/{projectId}/task/delete")
    @SendTo("/topic/project/{projectId}/task/delete")
    public List<Integer> deleteAllTasks(
            @DestinationVariable int projectId,
            @Payload List<Integer> taskIds){
        try {
            taskRingBufferService.removeFromBuffer(taskIds);
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
    public TaskDto updateTask(
            @DestinationVariable int projectId,
            @DestinationVariable int taskId,
            @Payload @Valid TaskDto taskDto){
        return taskRingBufferService.updateInBuffer(taskDto, projectId, taskId);
    }
}
