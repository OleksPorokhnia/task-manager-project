package com.alex.project.taskmanagerproject.services.taskService;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.repository.TaskRepository;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import com.alex.project.taskmanagerproject.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class TaskServiceTaskCreationTest {

    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private TaskService taskService;

    @Test
    void whenTaskValid_thanReturnTask(){
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New title");
        taskDto.setDescription("New description");
        taskDto.setPriority(1);
        taskDto.setStatus("IN_PROGRESS");
        taskDto.setUserUsername("user");
        taskDto.setCreatorUsername("creator");

        User user = new User(); user.setNickname("user");
        User creator = new User(); creator.setNickname("creator");
        Project project = new Project(); project.setId(1);
        Task task = new Task(); task.setId(12);

        when(userRepository.findByNickname("user")).thenReturn(Optional.of(user));
        when(userRepository.findByNickname("creator")).thenReturn(Optional.of(creator));
        when(projectRepository.getById(1)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(taskDto, 1);

        assertNotNull(result);
        assertEquals(12, result.getId());
        verify(taskRepository).save(any(Task.class));
    }
}
