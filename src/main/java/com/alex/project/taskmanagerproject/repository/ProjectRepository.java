package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    public Project findByTitle(String title);
    public Optional<Project> findById(int id);
    public Optional<Project> getById(int id);
}
