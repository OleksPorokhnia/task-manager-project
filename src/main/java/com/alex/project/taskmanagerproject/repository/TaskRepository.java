package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.Project;
import com.alex.project.taskmanagerproject.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    public void deleteAllByIdIn(List<Integer> ids);
    public List<Task> getAllByProject(Project project);
}
