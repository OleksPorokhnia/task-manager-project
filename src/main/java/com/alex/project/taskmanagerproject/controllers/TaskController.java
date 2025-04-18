package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project/{projectId}/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@PathVariable int projectId, @RequestBody TaskDto taskDto) {
        Task task = null;
        System.out.println("My project id: " + projectId);
        try{
            task = taskService.createTask(taskDto, projectId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(402).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(task);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAllTasks(@RequestBody List<Integer> taskIds){
        taskService.deleteAllSelectedTasks(taskIds);
        return ResponseEntity.status(200).body("Successfully deleted tasks");
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable int projectId,
            @PathVariable int taskId,
            @RequestBody TaskDto taskDto){

        Task task = null;
        System.out.println("My project id: " + projectId);
        try{
            task = taskService.updateTask(taskDto, projectId, taskId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(402).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(task);
    }
}
