package com.alex.project.taskmanagerproject.mappers;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.dto.response.ProjectResponseDto;
import com.alex.project.taskmanagerproject.dto.response.UserResponseDto;
import com.alex.project.taskmanagerproject.entity.Project;

import java.util.List;

public class ProjectMapper {
    public static Project projectMapper(ProjectDto projectDto) {
        Project project = new Project();
        project.setTitle(projectDto.getTitle());
        return project;
    }

    public static ProjectResponseDto convertProjectToDto(Project project) {
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setId(project.getId());
        projectResponseDto.setTitle(project.getTitle());

        List<UserResponseDto> userResponseDtos = project.getUsers().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getNickname(), user.getEmail()))
                .toList();

        projectResponseDto.setUsers(userResponseDtos);

        return projectResponseDto;
    }
}
