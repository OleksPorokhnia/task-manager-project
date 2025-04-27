package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.dto.response.ProjectResponseDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.mappers.ProjectMapper;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectControllerStomp {

    @Autowired
    private ProjectService projectService;

    @MessageMapping("/project/{projectId}/update")
    @SendTo("/topic/project/{projectId}/update")
    public ProjectResponseDto updateProject(
            @DestinationVariable int projectId,
            @Payload ProjectDto projectDto
    ){
        Project project = projectService.update(projectDto, projectId);
        return ProjectMapper.convertProjectToDto(project);
    }
}
