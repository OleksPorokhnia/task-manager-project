package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.dto.TaskDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.Task;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.mappers.TaskMapper;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.repository.TaskRepository;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import com.alex.project.taskmanagerproject.service.interfaces.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ServiceLayer<Task, TaskDto> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public Task create(TaskDto taskDto, int projectId) {
        User user = null;
        User creator = null;
        if(taskDto.getUserUsername() != null) {
            user = userRepository.findByNickname(taskDto.getUserUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        if(taskDto.getCreatorUsername() != null){
            creator = userRepository.findByNickname(taskDto.getCreatorUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Project project = projectRepository.getById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        if(taskDto.getStatus() != null || !taskDto.getStatus().isEmpty()){
            task.setStatus(taskDto.getStatus());
        }else{
            task.setStatus("TODO");
        }
        task.setUser(user);
        task.setDeadline(taskDto.getDeadline());
        task.setProject(project);
        task.setCreator(creator);

        return taskRepository.save(task);
    }

    public void deleteAll(List<Integer> taskIds){
        taskRepository.deleteAllById(taskIds);
    }

    public Task update(TaskDto taskDto, int projectId, int taskId) {
        User user = null;
        User creator = null;
        if(taskDto.getUserUsername() != null) {
            user = userRepository.findByNickname(taskDto.getUserUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        if(taskDto.getCreatorUsername() != null){
            creator = userRepository.findByNickname(taskDto.getCreatorUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Project project = projectRepository.getById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setId(taskId);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus()  );
        task.setUser(user);
        task.setDeadline(taskDto.getDeadline());
        task.setProject(project);
        task.setCreator(creator);

        return taskRepository.save(task);
    }

    public List<Task> getAll(int projectId){
        Project project = projectRepository.getById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return taskRepository.getAllByProject(project);
    }

    @Override
    public void delete(int id) {
        taskRepository.deleteById(id);
    }

    public void saveAllTasks(List<TaskDto> taskDtos){
        List<Task> allTasks = taskDtos.stream()
                        .map(task -> taskMapper.fromTaskDtoToTask(task, task.getProject_id()))
                        .toList();
        taskRepository.saveAll(allTasks);
    }
}
