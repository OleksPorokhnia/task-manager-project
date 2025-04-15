package com.alex.project.taskmanagerproject.controllers;

import com.alex.project.taskmanagerproject.dto.ProjectDto;
import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.mappers.ProjectMapper;
import com.alex.project.taskmanagerproject.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/add")
    public ResponseEntity<?> projectCreation(@RequestBody @Valid ProjectDto project) {
        Project createdProject = null;
        try{
            createdProject = projectRepository.save(ProjectMapper.projectMapper(project));
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
        return ResponseEntity.status(200).body(project);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.status(200).body(projects);
    }
}
