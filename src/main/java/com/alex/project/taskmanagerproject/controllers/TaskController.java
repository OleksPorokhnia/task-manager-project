package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.Message;
import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.exception.ValidationException;
import com.alex.project.taskmanagerproject.mappers.TaskMapper;
import com.alex.project.taskmanagerproject.service.TaskService;
import com.alex.project.taskmanagerproject.service.UserService;
import com.alex.project.taskmanagerproject.service.notification_service.NotificationProducer;
import com.alex.project.taskmanagerproject.service.ringBuffer.TaskRingBufferService;
import com.alex.project.taskmanagerproject.util.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private TaskRingBufferService taskRingBufferService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;

    @Autowired
    private NotificationProducer notificationProducer;

    @MessageMapping("/project/{projectId}/tasks")
    @SendTo("/topic/project/{projectId}/tasks")
    public List<TaskDto> getAllTasks(@DestinationVariable int projectId) {
        List<TaskDto> allTasks = new ArrayList<>(taskRingBufferService.getBufferCopy(projectId).stream().filter(taskDto -> taskDto.getProject_id() == projectId).toList());
        allTasks.addAll(taskService.getAll(projectId).stream().map(task -> taskMapper.toTaskDtoFromTask(task)).toList());
        System.out.println(allTasks);
        return allTasks;
    }

    @MessageMapping("/client-ready")
    public void onClientReady(Principal principal) {
        // Эмуляция отправки уведомления после того, как клиент подписался
        brokerMessagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/success",
                Map.of("message", "Welcome! You’re subscribed.")
        );
    }

    @MessageMapping("/project/{projectId}/task/add")
    @SendTo("/topic/project/{projectId}/task/add")
    public TaskDto addTask(
            @DestinationVariable int projectId,
            @Payload @Valid TaskDto taskDto,
            Principal principal) {

        System.out.println(principal.getName());

        brokerMessagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/success",
                Map.of("message", "Task created successfully")
        );

        Authentication authentication = (Authentication) principal;

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        notificationProducer.sendNotification(new Message("Task creation", "Successfully created task" + taskDto.getTitle(), projectId, customUserDetails.getEmail()));

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
