package com.alex.project.taskmanagerproject.service.interfaces;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.entity.Project;

public interface ProjectServiceLayer extends ServiceLayer<Project, ProjectDto>{
    public Project update(ProjectDto dto, int projId);
    public Project create(ProjectDto dto);
}
