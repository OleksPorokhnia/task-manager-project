package com.alex.project.taskmanagerproject.mappers;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.entity.Project;

public class ProjectMapper {
    public static Project projectMapper(ProjectDto projectDto) {
        Project project = new Project();
        project.setTitle(projectDto.getTitle());
        return project;
    }
}
