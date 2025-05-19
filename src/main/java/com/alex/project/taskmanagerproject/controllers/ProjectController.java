package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.dto.response.ProjectResponseDto;
import com.alex.project.taskmanagerproject.dto.response.UserResponseDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.mappers.ProjectMapper;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import com.alex.project.taskmanagerproject.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public ResponseEntity<?> projectCreation(@RequestBody @Valid ProjectDto project) {
        Project createdProject = null;
        try{
            createdProject = projectService.create(project);
        }catch (Exception e){
            return ResponseEntity.status(401).body("Error during project creation");
        }
        return ResponseEntity.status(201).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable int id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            return ResponseEntity.status(404).body("Project not found");
        }
        return ResponseEntity.status(200).body(ProjectMapper.convertProjectToDto(project.get()));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllProjects(@PathVariable String username) {
        List<Project> projects = projectService.getAllUserProjects(username);
        List<ProjectResponseDto> projectResponseDtos = projects.stream().map(ProjectMapper::convertProjectToDto).toList();
        return ResponseEntity.status(200).body(projectResponseDtos);
    }

}
