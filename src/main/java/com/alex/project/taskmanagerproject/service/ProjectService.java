package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.User;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project updateProject(ProjectDto projectDto, int projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<User> users = userRepository.findUsersByNicknameIn(projectDto.getUsers());

        project.setId(projectId);
        project.setTitle(projectDto.getTitle());
        project.setUsers(users);

        return projectRepository.save(project);
    }
}
