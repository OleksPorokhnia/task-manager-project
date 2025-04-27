package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import com.alex.project.taskmanagerproject.service.interfaces.ProjectServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements ProjectServiceLayer {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project update(ProjectDto projectDto, int projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<User> users = userRepository.findUsersByNicknameIn(projectDto.getUsers());

        project.setId(projectId);
        project.setTitle(projectDto.getTitle());
        project.setUsers(users);

        return projectRepository.save(project);
    }

    @Override
    public Project create(ProjectDto dto, int projId) {
        return null;
    }

    @Override
    public void deleteAll(List<Integer> ids) {

    }

    @Override
    public Project update(ProjectDto dto, int projId, int id) {
        return null;
    }

    @Override
    public List<Project> getAll(int projId) {
        return List.of();
    }

    @Override
    public void delete(int id) {

    }
}
