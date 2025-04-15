package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    public Project findByTitle(String title);
    public Project getById(int id);
}
