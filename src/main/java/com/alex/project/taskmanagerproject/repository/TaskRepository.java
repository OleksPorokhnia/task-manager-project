package com.alex.project.taskmanagerproject.repository;

import com.alex.project.taskmanagerproject.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    public void deleteAllByIdIn(List<Integer> ids);
}
