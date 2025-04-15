package com.alex.project.taskmanagerproject.dto;

import jakarta.validation.constraints.Size;

public class ProjectDto {
    @Size(min = 10, max = 100)
    private String title;

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }
}
