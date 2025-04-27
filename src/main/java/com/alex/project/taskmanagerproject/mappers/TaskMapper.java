package com.alex.project.taskmanagerproject.mappers;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskMapper(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task fromTaskDtoToTask(TaskDto taskDto, int projId) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());
        task.setDeadline(taskDto.getDeadline());

        Project project = projectRepository.findById(projId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        task.setProject(project);

        User creator = userRepository.findByNickname(taskDto.getCreatorUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setCreator(creator);

        if(taskDto.getUserUsername() != null) {
            User user = userRepository.findByNickname(taskDto.getUserUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setUser(user);
        }

        return task;
    }

    public TaskDto toTaskDtoFromTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setPriority(task.getPriority());
        taskDto.setStatus(task.getStatus());
        taskDto.setDeadline(task.getDeadline());
        taskDto.setCreatorUsername(task.getCreator().getNickname());
        if(task.getUser() != null) {
            taskDto.setUserUsername(task.getUser().getNickname());
        }
        return taskDto;
    }
}
