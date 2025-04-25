package com.alex.project.taskmanagerproject.dto;

import com.alex.project.taskmanagerproject.entity.User;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProjectDto {
    @Size(min = 10, max = 100)
    private String title;

    private List<String> users;

    public ProjectDto() {
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
